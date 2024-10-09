package com.avex.ragraa.network

import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class CustomWebChromeClient(
    val updateCaptchaToken: (String) -> Unit
) : WebChromeClient() {
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        if (consoleMessage == null) return super.onConsoleMessage(null)

        val message = consoleMessage.message()

        if (message.startsWith("koubilgicaptchatoken:")) {
            val captchaToken = message.substring(21)
            updateCaptchaToken(captchaToken)
        }

        return super.onConsoleMessage(consoleMessage)
    }
}