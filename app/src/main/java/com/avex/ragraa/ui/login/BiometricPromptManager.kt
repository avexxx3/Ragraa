package com.avex.ragraa.ui.login

import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import com.avex.ragraa.AppCompatActivity

class BiometricPromptManager(
    private val activity: AppCompatActivity = AppCompatActivity
) {
    fun showBiometricPrompt(
        title: String = "Authenticate",
        description: String = "Please authenticate to view the saved password",
        changePasswordVisibility: () -> Unit
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
                Log.d("Dev", "HW_UNAVAILABLE")
                changePasswordVisibility()
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("Dev", "NO_HARDWARE")
                changePasswordVisibility()
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d("Dev", "NONE_ENROLLED")
                changePasswordVisibility()
                return
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.d("Dev", "UNSUPPORTED")
                changePasswordVisibility()
                return
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.d("Dev", "SECURITY_UPDATE_REQUIRED")
                changePasswordVisibility()
                return
            }

            else -> Unit
        }

        val prompt = BiometricPrompt(activity, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                changePasswordVisibility()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("Dev", "AUTH ERROR")
            }

        })

        prompt.authenticate(promptInfo.build())
    }
}