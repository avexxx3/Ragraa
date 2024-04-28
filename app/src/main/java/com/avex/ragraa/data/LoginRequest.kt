package com.avex.ragraa.data

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LoginRequest(
    var username: String = "",
    var password: String = "",
    var g_recaptcha_response: String = "",
    @Id var Id:Long = 0
)
