package com.avex.ragraa.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import retrofit2.Call
import retrofit2.Response


class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var username = ""
    private var password = ""
    private var isError = false

    fun updateUsername(newUsername: String) {
        if (validateUsername(newUsername))
            username = newUsername

        updateUI()
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        updateUI()
    }

    private fun validateUsername(username: String): Boolean {
        if (username.isNotEmpty()) {
            if (!username[0].isDigit()) return false
        } else return true

        if (username.length > 1) {
            if (!username[1].isDigit()) return false
        } else return true

        if (username.length > 2) {
            if (!username[2].isUpperCase()) return false
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
            it.copy(username = username, password = password, isError = isError)
        }
    }
}