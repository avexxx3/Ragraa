package com.avex.ragraa.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request


class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var username = ""
    private var password = ""
    private var g_captcha_response = ""
    private var isError = false
    private var result = ""


    fun updateUsername(newUsername: String) {
        if (validateUsername(newUsername))
            username = newUsername

        val obj = StringBuilder(username)

        if (username.length > 2)
            obj.setCharAt(2, username[2].uppercaseChar())

        username = obj.toString()

        updateUI()

    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        updateUI()
    }

    fun sendRequest() {
        val client = OkHttpClient()

        val url = "https://flexstudent.nu.edu.pk/Login/Login"

        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .addFormDataPart("g-captcha-response", g_captcha_response)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        println("test")

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                result = "Response timed out"
                updateUI()
                e.printStackTrace()
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    result = response.body?.string().toString()
                    updateUI()
                    println("success")
                }
            }

        }
        )

        println("next")

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
            it.copy(username = username, password = password, isError = isError, result = result)
        }
    }
}