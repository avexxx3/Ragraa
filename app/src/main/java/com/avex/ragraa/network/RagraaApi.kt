package com.avex.ragraa.network

import android.util.Log
import com.avex.ragraa.viewmodels.LoginViewModel
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

class RagraaApi(val viewModel: LoginViewModel) {
    var result = ""
    var loading = false
    private var sessionID = ""

    fun loginFlex() {
        if (loading)
            return

        result = ""
        loading = true
        viewModel.updateUI()

        val url = "https://flexstudent.nu.edu.pk/Login/Login"

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", viewModel.username)
            .addFormDataPart("password", viewModel.password)
            .addFormDataPart("g-recaptcha-response", viewModel.gCaptchaResponse)
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
                viewModel.updateUI()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                result = response.body?.string().toString()
                sessionID = response.header("set-cookie").toString().substring(18, 42)
                println(sessionID)
                loading = false
                viewModel.updateUI()
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