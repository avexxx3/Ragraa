package com.avex.ragraa.ui.login

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.data.LoginRequest
import com.avex.ragraa.network.RagraaApi
import com.avex.ragraa.sharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    lateinit var navController: NavHostController

    private val loginRequest = LoginRequest()
    private var isError: Boolean = false
    private var status: MutableList<String> = mutableListOf("", "", "", "", "")
    private var isOnCredential: Boolean = true
    private var response: Int = 0
    private var isCompleted: Boolean = false
    private var rememberLogin: Boolean = true
    private var expanded: Boolean = false
    private var passwordVisible: Boolean = false

    var flipPassword: Boolean = false

    init {
        Datasource.cacheData()

        loginRequest.rollNo = Datasource.rollNo
        loginRequest.password = Datasource.password

        Datasource.updateLoginUI = { updateUI() }
        RagraaApi.updateStatus = { updateStatus(it.first, it.second) }
        updateUI()
    }

    fun resetData() {
        status = mutableListOf("", "", "", "", "")
        response = 0
        isCompleted = false
    }

    //Login to flex and save the session ID.
    //Send request to fetch marks and attendance
    private fun loginFlex() {
        RagraaApi.loginFlex(loginRequest)
        updateUI()
    }

    fun updatePreference() {
        rememberLogin = !rememberLogin
        updateUI()
    }

    //This will only move to the next screen if status fetches marks successfully
    private fun updateStatus(newStatus: String, index: Int) {
        status[index] = newStatus

        if ((status[4].isEmpty() && status[index].contains("Fetched marks successfully") || status[index].contains(
                "Fetched attendance successfully"
            )) || status[index].contains("Fetched transcript successfully")
        ) response++

        if (response == 2) {
            isCompleted = true
            if (rememberLogin) Datasource.saveLogin()
        }

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
                rollNo = loginRequest.rollNo,
                password = loginRequest.password,
                isError = isError,
                status = status.toString(),
                showButtons = !(status[0].isNotEmpty() || status[1].isNotEmpty()),
                isOnCredential = isOnCredential,
                isCompleted = isCompleted,
                rememberLogin = rememberLogin,
                expanded = expanded,
                passwordVisible = passwordVisible
            )
        }
    }

    fun updateRollNo(newRollNo: String) {
        rollNoOperations(newRollNo)
        updateUI()
    }

    fun updatePassword(newPassword: String) {
        if (validatePassword(newPassword)) loginRequest.password = newPassword
        updateUI()
    }

    fun changePasswordVisibility() {
        passwordVisible = !passwordVisible
        updateUI()
    }

    //A bit vague but there's a whole lot going on in the usernames
    private fun rollNoOperations(newRollNo: String) {
        var updatedRollNo: String = newRollNo

        //Replaces the first row of letters with their corresponding number, for convenience
        if (updatedRollNo.isNotEmpty()) updatedRollNo = setIndex(
            updatedRollNo, updatedRollNo.length - 1, convertChar(updatedRollNo.last())
        )

        //Adds a '-' as the 4th letter regardless of what was inputted
        if (updatedRollNo.length == 4 && !updatedRollNo.contains('-')) {
            updatedRollNo = setIndex(updatedRollNo, 3, '-')
        }

        //Only updates when follows the format 00L-0000, or when deleting
        if (validateRollNo(updatedRollNo) || updatedRollNo.length < loginRequest.rollNo.length) {
            loginRequest.rollNo = updatedRollNo
            isError = false
        } else isError = true

        //Capitalize the third letter
        if (loginRequest.rollNo.length == 3) {
            loginRequest.rollNo =
                setIndex(loginRequest.rollNo, 2, loginRequest.rollNo[2].uppercaseChar())
        }

        //Remove the dash when erasing characters
        if (loginRequest.rollNo.length > updatedRollNo.length && updatedRollNo.length == 4) {
            updatedRollNo.trim('-')
            loginRequest.rollNo = updatedRollNo
        }
    }

    //Only lets user update username while inputting values that follow the format ##@-####
    private fun validateRollNo(rollNo: String): Boolean {
        if (rollNo.isNotEmpty()) {
            if (!rollNo[0].isDigit()) return false
        } else return true

        if (rollNo.length > 1) {
            if (!rollNo[1].isDigit()) return false
        } else return true

        if (rollNo.length > 2) {
            if (!rollNo[2].isLetter()) return false
        } else return true

        if (rollNo.length > 3) {
            if (rollNo[3] != '-') return false
        } else return true

        if (rollNo.length > 4) {
            if (!rollNo[4].isDigit()) return false
        } else return true

        if (rollNo.length > 5) {
            if (!rollNo[5].isDigit()) return false
        } else return true

        if (rollNo.length > 6) {
            if (!rollNo[6].isDigit()) return false
        } else return true

        if (rollNo.length > 7) {
            if (!rollNo[7].isDigit()) return false
        } else return true

        if (rollNo.length > 8) return false

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

    fun showMenu(expandedNew: Boolean = !expanded) {
        expanded = expandedNew
        updateUI()
    }

    fun select(semId: String) {
        Datasource.semId = semId
        sharedPreferences.edit().putString("semId", semId).apply()
        showMenu(false)
    }
}