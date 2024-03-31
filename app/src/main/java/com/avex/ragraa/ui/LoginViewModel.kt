package com.avex.ragraa.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
import java.util.Arrays


class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var username = "23L-0655"
    private var password = "5161682922"
    var g_recaptcha_response = ""
    private var isError = false
    private var result = ""
    private var loading = false
    private var client = OkHttpClient()
    private var sessionID = ""

    fun updateToken(newToken: String) {
        g_recaptcha_response = newToken
    }

    fun updateUsername(newUsername: String) {
        var updatedUsername: String = newUsername

        //Replaces the first row of letters with their corresponding number, for convenience
        if (updatedUsername.isNotEmpty())
            updatedUsername = setIndex(
                updatedUsername,
                updatedUsername.length - 1,
                convertChar(updatedUsername.last())
            )

        if (updatedUsername.length == 4) {
            updatedUsername = setIndex(updatedUsername, 3, '-')
        }

        //Only updates when follows the format 00L-0000
        if (validateUsername(updatedUsername))
            username = updatedUsername

        //Add dash when autofilling and capitalize the third letter
        if (username.length == 3) {
            username = setIndex(username, 2, username[2].uppercaseChar())
        }

        //Remove the dash when erasing characters
        if (username.length > updatedUsername.length && updatedUsername.length == 4) {
            updatedUsername.trim('-')
            username = updatedUsername
        }

        updateUI()
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        updateUI()
    }

    fun sendRequest() {
        if (loading)
            return

        result = ""
        loading = true
        updateUI()

        val url = "https://flexstudent.nu.edu.pk/Login/Login"

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .addFormDataPart("g-recaptcha-response", g_recaptcha_response)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        var POSTresponse: Response

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                result = "Response timed out"
                loading = false
                updateUI()
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                result = response.body?.string().toString()
                sessionID = response.header("set-cookie").toString().substring(18, 42)
                println(sessionID)
                loading = false
                updateUI()
            }
        }
        )
    }

    fun getData() {
        val url = "https://flexstudent.nu.edu.pk/Student/StudentMarks?semid=20241"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient().newBuilder()
            .cookieJar(object : CookieJar {
                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {}
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    return Arrays.asList(createNonPersistentCookie())
                }
            })
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    println(response.body!!.string())
                }
            }
        })
    }

    private fun validateUsername(username: String): Boolean {
        if (username.isNotEmpty()) {
            if (!username[0].isDigit()) return false
        } else return true

        if (username.length > 1) {
            if (!username[1].isDigit()) return false
        } else return true

        if (username.length > 3) {
            if (username[3] != '-') return false
        } else return true

        if (username.length > 4) {
            if (!username[4].isDigit()) return false
        } else return true

        if (username.length > 5) {
            if (!username[5].isDigit()) return false
        } else return true

        if (username.length > 6) {
            if (!username[6].isDigit()) return false
        } else return true

        if (username.length > 7) {
            if (!username[7].isDigit()) return false
        } else return true

        if (username.length > 8) return false

        return true
    }

    private fun updateUI() {
        _uiState.update {
            it.copy(
                username = username,
                password = password,
                isError = isError,
                result = result,
                loading = loading
            )
        }
    }

    private fun setIndex(string: String, index: Int, value: Char): String {
        val obj = StringBuilder(string)
        obj.setCharAt(index, value)
        return obj.toString()
    }

    private fun convertChar(letter: Char): Char {
        when (letter) {
            'q' -> return '1'
            'w' -> return '2'
            'e' -> return '3'
            'r' -> return '4'
            't' -> return '5'
            'y' -> return '6'
            'u' -> return '7'
            'i' -> return '8'
            'o' -> return '9'
            'p' -> return '0'
        }
        return letter
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
