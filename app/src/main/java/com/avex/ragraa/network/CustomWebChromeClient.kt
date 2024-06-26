package com.avex.ragraa.network

import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class CustomWebChromeClient(val updateCaptcha: (String) -> Unit, val hideCaptcha: () -> Unit) : WebChromeClient() {
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {

        val message = consoleMessage?.message()
        if (message?.startsWith("koubilgicaptchatoken:") == true) {
            val captchaToken = message.substring(21)
            captchaLoaded = false
            updateCaptcha(captchaToken)
            hideCaptcha()
        }

        return super.onConsoleMessage(consoleMessage)
    }
}