package com.avex.ragraa.network

import android.webkit.WebView
import android.webkit.WebViewClient

var captchaLoaded = false

class CustomWebViewClient(
    val hideLoading: () -> Unit
) : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        hideLoading()
    }
}