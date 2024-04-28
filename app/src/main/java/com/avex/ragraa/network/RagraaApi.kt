package com.avex.ragraa.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import com.avex.ragraa.context
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.LoginRequest
import com.avex.ragraa.data.MyObjectBox
import com.avex.ragraa.data.imageByteArray
import com.avex.ragraa.data.imageByteArray_
import io.objectbox.kotlin.boxFor
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit


object RagraaApi {
    var sessionID = ""
    var updateStatus:(String) -> Unit = {}

    fun loginFlex(loginRequest: LoginRequest) {
        val url = "https://flexstudent.nu.edu.pk/Login/Login"

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", loginRequest.username)
            .addFormDataPart("password", loginRequest.password)
            .addFormDataPart("g-recaptcha-response", loginRequest.g_recaptcha_response)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
            .newBuilder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                updateStatus("Response to Flex servers timed out.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.body?.string().toString().contains("\"status\":\"done\"")) {
                    updateStatus("Invalid credentials or recaptcha. Please login again")
                    return
                }
                updateStatus("Logged in successfully")
                sessionID = response.header("set-cookie").toString().substring(18, 42)

                if(MyObjectBox.builder().androidContext(context).build().boxFor<imageByteArray>().query(imageByteArray_.rollNo.equal(loginRequest.username)).build().find().isEmpty())
                    fetchImage(loginRequest.username)

                fetchAttendance()
                fetchMarks()
                // TODO fetchAttendance()
            }
        }
        )
    }

    private fun fetchMarks() {
        updateStatus("Sending request to /Student/StudentMarks...")

        val url = "https://flexstudent.nu.edu.pk/Student/StudentMarks?semid=20241"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient().newBuilder()
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf(createNonPersistentCookie())
                }
            })

            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                updateStatus("Error in connecting to flex")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Datasource.marksResponse = response.body?.string().toString()

                if (Datasource.marksResponse.contains("recaptcha")) {
                    updateStatus("Error logging in")
                }

                Datasource.parseMarks()
                updateStatus("Fetched marks successfully")
            }
        })
    }

    private fun fetchAttendance() {
        updateStatus("Sending request to /Student/StudentAttendance...")

        val url = "https://flexstudent.nu.edu.pk/Student/StudentAttendance?semid=20241"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient().newBuilder()
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf(createNonPersistentCookie())
                }
            })

            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                updateStatus("Error in connecting to flex")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Datasource.attendanceResponse = response.body?.string().toString()

                //TODO Run if-check directly on the response and move assignment down

                if (Datasource.marksResponse.contains("recaptcha")) {
                    updateStatus("Error logging in")
                }
                Datasource.parseAttendance()
                updateStatus("Fetched attendance successfully")
            }
        })
    }

    private fun fetchImage(rollNo: String) {
        updateStatus("Fetching profile picture")
        val request = Request.Builder()
            .url("https://flexstudent.nu.edu.pk/Login/GetImage")
            .build()

        val client = OkHttpClient().newBuilder()
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return listOf(createNonPersistentCookie())
                }
            })

            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                updateStatus("Error connecting to flex")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                updateStatus("Fetched profile picture successfully")

                val box = MyObjectBox.builder().androidContext(context).build().boxFor<imageByteArray>()

                    val inputStream = response.body?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    globalBitmap = bitmap
                    val bos = ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    val bArray = bos.toByteArray();
                    box.put(imageByteArray(bArray, rollNo, 0))
                    Log.d("Dev", "Creating user")
            }
        })
    }

    private fun createNonPersistentCookie(): Cookie {
        return Cookie.Builder()
            .domain("flexstudent.nu.edu.pk")
            .path("/")
            .name("ASP.NET_SessionId")
            .value(sessionID)
            .httpOnly()
            .build()
    }

}

var globalBitmap:Bitmap? = null