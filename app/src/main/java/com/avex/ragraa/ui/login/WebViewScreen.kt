package com.avex.ragraa.ui.login

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.avex.ragraa.network.CustomWebChromeClient
import com.avex.ragraa.network.CustomWebViewClient
import com.avex.ragraa.network.captchaLoaded

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    updateCaptcha: (String) -> Unit, navLogin: () -> Unit, showCaptcha:() -> Unit, hideCaptcha: () -> Unit
) {
    BackHandler {
        navLogin()
        hideCaptcha()
        captchaLoaded = false
    }

    val webViewClient = CustomWebViewClient {showCaptcha()}

    val webChromeClient = CustomWebChromeClient ({ updateCaptcha(it) }, {hideCaptcha()})

    AndroidView(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),factory = { context ->
        WebView(context).apply {
            this.webViewClient = webViewClient
            this.webChromeClient = webChromeClient
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
        }
    }) { webView ->
        webView.loadUrl("https://flexstudent.nu.edu.pk/Login")
        captchaLoaded = false
    }
}