package com.avex.ragraa.network

import android.provider.ContactsContract.Data
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.Datasource.saveImage
import com.avex.ragraa.data.LoginRequest
import com.avex.ragraa.sharedPreferences
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


object RagraaApi {
    var sessionID = ""
    var updateStatus: (String) -> Unit = {}

    fun loginFlex(loginRequest: LoginRequest) {
        updateStatus("Logging in")

        val url = "https://flexstudent.nu.edu.pk/Login/Login"

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", loginRequest.rollNo.ifEmpty { Datasource.rollNo })
            .addFormDataPart("password", loginRequest.password.ifEmpty { Datasource.password })
            .addFormDataPart("g-recaptcha-response", loginRequest.g_recaptcha_response).build()

        val request = Request.Builder().url(url).post(requestBody).build()

        val client = OkHttpClient().newBuilder().connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                updateStatus("Response to Flex servers timed out.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.body?.string().toString().contains("\"status\":\"done\"")) {
                    updateStatus("Error: Invalid credentials or recaptcha. Please login again")
                    return
                }
                updateStatus("Logged in successfully")
                sessionID = response.header("set-cookie").toString().substring(18, 42)

                Datasource.rollNo = loginRequest.rollNo
                Datasource.password = loginRequest.password
                fetchImage(loginRequest.rollNo)
                fetchAttendance()
                fetchMarks()
            }
        })
    }

    private fun fetchMarks() {
        updateStatus("Fetching marks")

        val url = "https://flexstudent.nu.edu.pk/Student/StudentMarks?semid=20${Datasource.semId}"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient().newBuilder().cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return listOf(createNonPersistentCookie())
            }
        })

            .connectTimeout(1, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                updateStatus("Error: Failed to connect to flex")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Datasource.marksResponse = response.body?.string().toString()

                if (Datasource.marksResponse.contains("recaptcha")) {
                    updateStatus("Error: Failed to log in")
                }

                Datasource.date = SimpleDateFormat(
                    "dd MMM, HH:mm", Locale.getDefault()
                ).format(Calendar.getInstance().time)

                sharedPreferences.edit().putString("date", Datasource.date).apply()

                Datasource.parseMarks()
                updateStatus("Fetched marks successfully")
            }
        })
    }

    private fun fetchAttendance() {
        updateStatus("Fetching attendance")

        val url =
            "https://flexstudent.nu.edu.pk/Student/StudentAttendance?semid=20${Datasource.semId}"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient().newBuilder().cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return listOf(createNonPersistentCookie())
            }
        })

            .connectTimeout(1, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                updateStatus("Error: Failed to connect to flex")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Datasource.attendanceResponse = response.body?.string().toString()

                //TODO Run if-check directly on the response and move assignment down

                if (Datasource.attendanceResponse.contains("recaptcha")) {
                    updateStatus("Error: Failed to log in")
                }

                Datasource.parseAttendance()
                updateStatus("Fetched attendance successfully, fetching marks")
            }
        })
    }

    private fun fetchImage(rollNo: String) {
        updateStatus("Fetching profile picture")
        val request = Request.Builder().url("https://flexstudent.nu.edu.pk/Login/GetImage").build()

        val client = OkHttpClient().newBuilder().cookieJar(object : CookieJar {
            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return listOf(createNonPersistentCookie())
            }
        })

            .connectTimeout(1, TimeUnit.MINUTES).writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                updateStatus("Error connecting to flex")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                updateStatus("Fetched profile picture successfully, Fetching attendance")
                saveImage(response, rollNo)
            }
        })
    }

    private fun createNonPersistentCookie(): Cookie {
        return Cookie.Builder().domain("flexstudent.nu.edu.pk").path("/").name("ASP.NET_SessionId")
            .value(sessionID).httpOnly().build()
    }

}