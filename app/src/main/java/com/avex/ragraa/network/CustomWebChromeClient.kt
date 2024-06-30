package com.avex.ragraa.network

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient

class CustomWebChromeClient(val updateCaptcha: (String) -> Unit, val hideCaptcha: () -> Unit) :
    WebChromeClient() {
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {

        val message = consoleMessage?.message()

        Log.d("Dev", message!!)

        if (message.startsWith("koubilgicaptchatoken:")) {
            val captchaToken = message.substring(21)
            captchaLoaded = false
            updateCaptcha(captchaToken)
            hideCaptcha()
        }

        return super.onConsoleMessage(consoleMessage)
    }
}