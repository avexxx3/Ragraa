package com.avex.ragraa.ui.login

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.avex.ragraa.R
import com.avex.ragraa.network.CustomWebChromeClient
import com.avex.ragraa.network.CustomWebViewClient
import com.avex.ragraa.network.captchaLoaded

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    updateCaptcha: (String) -> Unit,
    navLogin: () -> Unit,
    showCaptcha: () -> Unit,
    hideCaptcha: () -> Unit
) {
    BackHandler {
        navLogin()
        captchaLoaded = false
    }

    val backgroundHex = if (isSystemInDarkTheme()) "#11140F" else "#F8FAF0"

    val webViewClient = CustomWebViewClient(backgroundHex) { showCaptcha() }

    val webChromeClient = CustomWebChromeClient({ updateCaptcha(it) }, { hideCaptcha() })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            stringResource(R.string.please_solve_the_captcha_to_continue),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp)
        )

        AndroidView(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background), factory = { context ->
            WebView(context).apply {
                this.webViewClient = webViewClient
                this.webChromeClient = webChromeClient
                settings.javaScriptEnabled = true
                settings.setSupportZoom(true)
            }
        }) { webView ->
            if (!captchaLoaded) webView.loadUrl("https://flexstudent.nu.edu.pk/Login")
        }
    }
}