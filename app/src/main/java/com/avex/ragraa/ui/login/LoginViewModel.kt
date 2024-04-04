package com.avex.ragraa.ui.login

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.data.LoginRequest
import com.avex.ragraa.network.RagraaApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

val fileName = "studentMarks.html"

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    lateinit var navController: NavHostController

    private val loginRequest = LoginRequest("23L-0655", "5161682922", "")

    private var isError = false

    private var parseComplete = false

    init {
        updateUI()
        RagraaApi.loginRequest = loginRequest
        RagraaApi.updateUI = { updateUI() }
        RagraaApi.viewModelSendRequest = { sendRequest() }
        RagraaApi.afterParse = { afterParse() }
    }

    //Login to flex and save the session ID
    fun loginFlex() {
        RagraaApi.loginFlex()
        updateUI()
    }

    //Fetch data for semid=20241
    private fun sendRequest() {
        RagraaApi.sendRequest()
        updateUI()
    }

    private fun afterParse() {
        println("Parse complete")
        parseComplete = true
        updateUI()
    }

    private fun writeData() {

    }

    fun updateUsername(newUsername: String) {
        usernameOperations(newUsername)
        updateUI()
    }

    fun updatePassword(newPassword: String) {
        if (validatePassword(newPassword))
            loginRequest.password = newPassword
        updateUI()
    }

    //Used to update the token retrieved from CustomChromeWebClient
    fun updateToken(newToken: String) {
        loginRequest.g_recaptcha_response = newToken
    }

    //Update ui with updated state
    private fun updateUI() {
        _uiState.update {
            it.copy(
                username = loginRequest.username,
                password = loginRequest.password,
                isError = isError,
                result = RagraaApi.result,
                loading = RagraaApi.loading,
                isLoggedIn = RagraaApi.isLoggedIn,
                parseComplete = parseComplete
            )
        }
    }

    //A bit vague but there's a whole lot going on in the usernames
    private fun usernameOperations(newUsername: String) {
        var updatedUsername: String = newUsername

        //Replaces the first row of letters with their corresponding number, for convenience
        if (updatedUsername.isNotEmpty())
            updatedUsername = setIndex(
                updatedUsername,
                updatedUsername.length - 1,
                convertChar(updatedUsername.last())
            )

        //Adds a '-' as the 4th letter regardless of what was inputted
        if (updatedUsername.length == 4 && !updatedUsername.contains('-')) {
            updatedUsername = setIndex(updatedUsername, 3, '-')
        }

        //Only updates when follows the format 00L-0000, or when deleting
        if (validateUsername(updatedUsername) || updatedUsername.length < loginRequest.username.length) {
            loginRequest.username = updatedUsername
            isError = false
        } else isError = true

        //Capitalize the third letter
        if (loginRequest.username.length == 3) {
            loginRequest.username =
                setIndex(loginRequest.username, 2, loginRequest.username[2].uppercaseChar())
        }

        //Remove the dash when erasing characters
        if (loginRequest.username.length > updatedUsername.length && updatedUsername.length == 4) {
            updatedUsername.trim('-')
            loginRequest.username = updatedUsername
        }
    }

    //Only lets user update username while inputting values that follow the format ##@-####
    private fun validateUsername(username: String): Boolean {
        if (username.isNotEmpty()) {
            if (!username[0].isDigit()) return false
        } else return true

        if (username.length > 1) {
            if (!username[1].isDigit()) return false
        } else return true

        if (username.length > 2) {
            if (!username[2].isLetter()) return false
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

    //Only for disallowing spaces in password
    private fun validatePassword(password: String): Boolean {
        return !password.contains(' ')
    }

    //Replace the index of a string with a character
    private fun setIndex(string: String, index: Int, value: Char): String {
        val obj = StringBuilder(string)
        obj.setCharAt(index, value)
        return obj.toString()
    }

    //Replaces the first row of letters with their corresponding number, for convenience
    private fun convertChar(letter: Char): Char {
        when (letter) {
            'q' -> return '1'
            'w' -> return '2'
            'e' -> return '3'
            'r' -> return '4'
            't' -> return '5'
            'y' -> return '6'
            'u' -> return '7'
            'i' -> return '8'
            'o' -> return '9'
            'p' -> return '0'
        }
        return letter
    }
}
