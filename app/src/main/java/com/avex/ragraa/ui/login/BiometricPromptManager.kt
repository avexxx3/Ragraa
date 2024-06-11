package com.avex.ragraa.ui.login

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.avex.ragraa.AppCompatActivity
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class BiometricPromptManager(
    private val activity: AppCompatActivity = AppCompatActivity
) {

    private val resultChannel = Channel<BiometricResult>()
    var promptResults = resultChannel.receiveAsFlow()

    fun showBiometricPrompt(
        title: String, description: String
    ) {
        val manager = BiometricManager.from(activity)

        val authenticators = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        } else BIOMETRIC_STRONG

        val promptInfo =
            BiometricPrompt.PromptInfo.Builder().setTitle(title).setDescription(description)
                .setAllowedAuthenticators(authenticators).setConfirmationRequired(false)

        if (Build.VERSION.SDK_INT < 30) promptInfo.setNegativeButtonText("Cancel")

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
                return
            }

            else -> Unit
        }

        val prompt = BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("Dev", "Auth failed")
                resultChannel.trySend(BiometricResult.AuthenticationFailed)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("Dev", "Auth success")
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("Dev", "Auth success")
                resultChannel.trySend(BiometricResult.AuthenticationSuccess)
            }

        })

        prompt.authenticate(promptInfo.build())
    }

    sealed interface BiometricResult {
        data object AuthenticationFailed : BiometricResult
        data object AuthenticationSuccess : BiometricResult
    }
}