package com.avex.ragraa.data

data class LoginRequest(
    var username: String,
    var password: String,
    var g_recaptcha_response: String
)
