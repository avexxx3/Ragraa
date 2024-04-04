package com.avex.ragraa.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isError: Boolean = false,
    val isLoggedIn: Boolean = false,
    val loading: Boolean = false,
    val result: String = "",
    val parseComplete: Boolean = false
) {
}