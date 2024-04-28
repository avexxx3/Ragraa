package com.avex.ragraa.ui.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isError: Boolean = false, //Highlight error when entering username
    val status: String = "",
    val isOnCredential:Boolean = true,
    ) {
}