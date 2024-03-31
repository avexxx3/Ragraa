package com.avex.ragraa.viewmodels

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isError: Boolean = false,
    val result: String = "",
    val loading: Boolean = false
)