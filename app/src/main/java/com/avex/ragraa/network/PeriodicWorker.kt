package com.avex.ragraa.network

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.avex.ragraa.MainActivity
import com.avex.ragraa.R
import com.avex.ragraa.data.CaptchaSolver
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.dataclasses.LoginRequest
import com.avex.ragraa.network.RagraaApi.fetchMarks
import com.avex.ragraa.network.RagraaApi.sessionID
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class PeriodicWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    /**
     * Creates and sends a push notification to the user.
     * Handles notification channel creation for Android Oreo and above.
     */
    private fun sendNotification(title: String, message: String?) {
        val channelId = "flex_updates"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Flex Updates"
            val descriptionText = "Notifications for new marks and attendance"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent to open the app when notification is clicked
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Support long messages
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Use a unique ID based on time to avoid overwriting previous notifications
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    private fun marksChecked(success: Boolean) {
        if (!success) return

        if(Datasource.newAdditions.isEmpty())
            return

        var message = "New marks added for"

        if (Datasource.newAdditions.size == 1) {
            val addition = Datasource.newAdditions.first()
            message += " ${addition.first}'s ${addition.second}"
            if (addition.third.isNotEmpty()) message += " (${addition.third})"
        }
        else {
            message += ":"
            val courses = Datasource.newAdditions.groupBy { it.first }
            val size = courses.size
            var counter = 0

            for(course in courses) {
                message += "\n${course.key}'s "
                for(section in course.value) {
                    message += section.second
                    if (section.third.isNotEmpty()) message += " (${section.third})"
                    if(section != course.value.last())
                        message += ", "
                }

                if(++counter != size)
                    message += ","
            }

            message += '.'
        }

        sendNotification("New Marks Uploaded!", message)
    }

    override suspend fun doWork(): Result {
        return try {
            val key = CaptchaSolver.solveCaptcha()
            if (key.isFailure) return Result.retry()

            val loginReq = LoginRequest(Datasource.rollNo, Datasource.password, key.getOrThrow())

            val url = "https://flexstudent.nu.edu.pk/Login/Login"

            val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", loginReq.rollNo)
                .addFormDataPart("password", loginReq.password)
                .addFormDataPart("g-recaptcha-response", loginReq.g_recaptcha_response).build()

            val request = Request.Builder().url(url).post(requestBody).build()

            val client = OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build()

            // Synchronous call to keep the worker alive during network request
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!responseBody.contains("\"status\":\"done\"")) {
                if (responseBody.contains("\"msg\":\"Incorrect Recaptcha.\""))
                    throw(Error("Invalid recaptcha. Please check the key."))
                else
                    throw(Error("Invalid credentials."))
            }

            // Extract session cookie
            val cookie = response.header("set-cookie").toString()
            if (cookie.length >= 42) {
                sessionID = cookie.substring(18, 42)
            }

            Datasource.marksParsed = false

            // Wait for fetchMarks to complete before returning worker result
            val latch = CountDownLatch(1)
            fetchMarks { success ->
                marksChecked(success)
                latch.countDown()
            }
            latch.await(2, TimeUnit.MINUTES)

            Result.success()
        } catch (e: Exception) {
            sendNotification("Error!", e.message)
            Result.failure()
        }
    }
}
