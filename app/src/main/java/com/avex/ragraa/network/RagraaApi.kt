package com.avex.ragraa.network

import android.util.Log
import com.avex.ragraa.data.LoginRequest
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
import java.util.concurrent.TimeUnit

class RagraaApi(private val loginRequest: LoginRequest, private val updateUI: () -> Unit) {
    var result = ""
    var loading = false
    var isLoggedIn = false
    private var sessionID = ""

    fun loginFlex() {
        if (loading)
            return

        result = ""
        loading = true
        updateUI()

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
                result = "Response timed out"
                loading = false
                updateUI()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                result = if (response.body?.string().toString()
                        .contains("\"status\":\"done\"")
                ) "Success" else "Failed"

                if (response.isSuccessful) {
                    if (response.header("set-cookie")!!.length > 42)
                        sessionID = response.header("set-cookie").toString().substring(18, 42)
                }
                isLoggedIn = sessionID.isNotEmpty()

                println(sessionID)
                loading = false
                updateUI()
            }
        }
        )
    }

    fun sendRequest() {
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
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    Log.d("Network", "Response from Flex successful")

                    println(response.body!!.string())
                }
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