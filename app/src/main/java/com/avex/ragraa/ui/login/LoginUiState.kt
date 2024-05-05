package com.avex.ragraa.ui.login

data class LoginUiState(
    val rollNo: String = "",
    val password: String = "",
    val isError: Boolean = false, //Highlight error when entering username
    val status: String = "",
    val isOnCredential:Boolean = true,
    val isCompleted:Boolean = false,
    val rememberLogin:Boolean = true,
    val expanded:Boolean = false
    )