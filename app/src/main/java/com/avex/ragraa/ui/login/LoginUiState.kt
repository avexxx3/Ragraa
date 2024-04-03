package com.avex.ragraa.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isError: Boolean = false,
    val isLoggedIn: Boolean = false,
    val result: String = "",
    val loading: Boolean = false
)