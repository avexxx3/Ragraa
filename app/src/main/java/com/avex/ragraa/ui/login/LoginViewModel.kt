package com.avex.ragraa.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.LoginRequest
import com.avex.ragraa.network.RagraaApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    lateinit var navController: NavHostController

    private val loginRequest = LoginRequest("23L-0655", "5161682922", "")

    private var isError:Boolean = false
    private var status:String = ""

    init {
        Datasource.updateUI = {updateUI()}
        RagraaApi.updateStatus = {updateStatus(it)}
        updateUI()
    }

    //Login to flex and save the session ID.
    //Send request to fetch marks and attendance
    fun loginFlex() {
        Log.d("Dev", "Are you fr")
        RagraaApi.loginFlex(loginRequest)
    }

    private fun updateStatus(newStatus:String) {
        status = newStatus
        Log.d("Dev", status)
        updateUI()
    }

    //Used to update the token retrieved from CustomChromeWebClient
    //Navigates back to home screen after fetching it successfully
    fun updateCaptcha(newToken: String) {
        loginRequest.g_recaptcha_response = newToken
        navController.navigate("login")
        loginFlex()
    }

    //Update ui with updated state
    private fun updateUI() {
        _uiState.update {
            it.copy(
                username = loginRequest.username,
                password = loginRequest.password,
                isError = isError,
                status = status
                //loading = RagraaApi.loading,
                //isLoggedIn = RagraaApi.isLoggedIn,
            )
        }
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
