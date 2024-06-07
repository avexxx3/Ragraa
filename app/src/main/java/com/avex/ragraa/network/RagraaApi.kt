package com.avex.ragraa.network

import android.util.Log
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.Datasource.marksResponse
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
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit


object RagraaApi {
    var sessionID = ""
    var updateStatus: (Pair<String, Int>) -> Unit = {}

    fun loginFlex(loginRequest: LoginRequest) {
        updateStatus(Pair("Logging in..", 0))

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
                updateStatus(Pair("Error: Response to Flex servers timed out.", 0))
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.body?.string().toString().contains("\"status\":\"done\"")) {
                    updateStatus(
                        Pair(
                            "Error: Invalid credentials or recaptcha. Please login again",
                            0
                        )
                    )
                    return
                }
                updateStatus(Pair("Logged in successfully", 0))
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
        updateStatus(Pair("Fetching marks..", 3))

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
                updateStatus(Pair("Error: Failed to connect to flex", 0))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                marksResponse = response.body?.string().toString()

                if (marksResponse.contains("recaptcha")) {
                    updateStatus(Pair("Error: Failed to log in", 0))
                }

                Datasource.date = SimpleDateFormat(
                    "dd MMM, HH:mm", Locale.getDefault()
                ).format(Calendar.getInstance().time)

                sharedPreferences.edit().putString("date", Datasource.date).apply()

                if (Jsoup.parse(marksResponse).body().getElementsByClass("GrandtotalColumn")
                        .isNotEmpty()
                ) {
                    val menuItems = Jsoup.parse(marksResponse).body()
                        .getElementsByClass("m-menu__item  m-menu__item--submenu").html()
                    val transcriptItem =
                        menuItems.substring(menuItems.indexOf("/Student/Transcript"))
                    val transcriptURL = transcriptItem.substring(0, transcriptItem.indexOf('\"'))
                    fetchTranscript(transcriptURL)
                }

                updateStatus(Pair("Fetched marks successfully", 3))

                Datasource.parseMarks()
            }
        })
    }

    private fun fetchAttendance() {
        updateStatus(Pair("Fetching attendance..", 2))

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
                updateStatus(Pair("Error: Failed to connect to flex", 0))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Datasource.attendanceResponse = response.body?.string().toString()

                if (Datasource.attendanceResponse.contains("recaptcha")) {
                    updateStatus(Pair("Error: Failed to log in", 0))
                }

                Datasource.parseAttendance()
                updateStatus(Pair("Fetched attendance successfully", 2))
            }
        })
    }

    private fun fetchImage(rollNo: String) {
        updateStatus(Pair("Fetching profile picture..", 1))
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
                updateStatus(Pair("Error: Failed to connect to flex", 0))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                updateStatus(Pair("Fetched profile picture successfully", 1))
                saveImage(response, rollNo)
            }
        })
    }

    private fun fetchTranscript(transcriptURL: String) {
        updateStatus(Pair("Fetching transcript..", 4))

        val url = "https://flexstudent.nu.edu.pk$transcriptURL"

        Log.d("Dev", url)

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
                updateStatus(Pair("Error: Failed to connect to flex", 0))
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                Datasource.transcriptResponse = response.body?.string().toString()

                if (Datasource.transcriptResponse.contains("recaptcha")) {
                    updateStatus(Pair("Error: Failed to log in", 0))
                }

                Datasource.parseTranscript()
                updateStatus(Pair("Fetched transcript successfully", 4))
            }
        })
    }

    private fun createNonPersistentCookie(): Cookie {
        return Cookie.Builder().domain("flexstudent.nu.edu.pk").path("/").name("ASP.NET_SessionId")
            .value(sessionID).httpOnly().build()
    }

}