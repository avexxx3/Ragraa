package com.avex.ragraa.network

import android.annotation.SuppressLint
import android.util.Log
import com.avex.ragraa.data.Datasource
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

@SuppressLint("StaticFieldLeak")
object RagraaApi {

    var loginRequest: LoginRequest = LoginRequest()
    var afterParse: () -> Unit = {}
    var viewModelSendRequest: () -> Unit = {}
    var updateUI: () -> Unit = {}
    var loading = false
    var result: String = ""
    var isLoggedIn = false

    private var sessionID = ""

    var flexResponse: String = ""

    fun loginFlex() {
        if (loading)
            return

        println("Logging in flex")

        result = "Logging in..."
        isLoggedIn = false
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
                loading = false
                result = "Response to Flex servers timed out."
                Log.d("Network", "Response to Flex servers timed out.")

                updateUI()
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                onLoginResponse(response)
            }
        }
        )
    }

    fun sendRequest() {
        Log.d("Network", "Sending request...")
        loading = true

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
                isLoggedIn = false
                result = "There was an error in fetching the data. Please try again"
                Log.d("Network", "Error in connecting to Flex")
                loading = false
            }

            override fun onResponse(call: Call, response: Response) {
                onMarksResponse(response)
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

    private fun onLoginResponse(response: Response) {


        if (!response.body?.string().toString().contains("\"status\":\"done\"")) {
            result = "Invalid username/password."
            Log.d("Network", "Logged in with invalid credentials")
            isLoggedIn = false
            loading = false
            updateUI()
            return
        }

        isLoggedIn = true
        sessionID = response.header("set-cookie").toString().substring(18, 42)
        result = "Successfully logged in.\nFetching data from servers..."
        updateUI()
        viewModelSendRequest()
        Log.d("Network", "Fetching data from Flex")
    }

    private fun onMarksResponse(response: Response) {
        response.use {
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            Datasource.flexResponse = response.body?.string().toString()

            if (Datasource.flexResponse.contains("recaptcha")) {
                result = "There was an error logging in. Please log-in again."
                isLoggedIn = false
                loading = false
                updateUI()
            }
            result = "Request to flex successful.\nParsing HTML Data..."
            Log.d("Network", "Request to flex successful")
            updateUI()
            Datasource.parseHTML()
            loading = false
            afterParse()
        }

    }


}