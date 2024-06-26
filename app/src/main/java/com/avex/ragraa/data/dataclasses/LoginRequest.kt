package com.avex.ragraa.data.dataclasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class LoginRequest(
    var rollNo: String = "",
    var password: String = "",
    var g_recaptcha_response: String = "",
    @Id var Id: Long = 0
)
