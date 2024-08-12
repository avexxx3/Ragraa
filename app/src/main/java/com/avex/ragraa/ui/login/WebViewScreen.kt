package com.avex.ragraa.ui.login

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.avex.ragraa.R
import com.avex.ragraa.context
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.network.CustomWebChromeClient
import com.avex.ragraa.network.CustomWebViewClient

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    updateCaptchaToken: (String) -> Unit,
    navLogin: () -> Unit,
    updateLoading: (Boolean) -> Unit,
    loading: Boolean
) {
    LaunchedEffect(context) {
        updateLoading(true)
    }

    BackHandler {
        navLogin()
    }

    val backgroundHex = if (Datasource.darkTheme) "#11140F" else "#F8FAF0"

    val webViewClient = CustomWebViewClient {
        updateLoading(false)
    }

    val webChromeClient = CustomWebChromeClient { updateCaptchaToken(it) }

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

        val url = "https://flexstudent.nu.edu.pk/Login"

        AndroidView(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background), factory = { context ->
            WebView(context).apply {
                this.webViewClient = webViewClient
                this.webChromeClient = webChromeClient
                settings.javaScriptEnabled = true
                settings.blockNetworkImage = true
                settings.setSupportZoom(true)
            }
        }) { webView ->
            if (loading) {
                webView.loadUrl(url)
            } else {
                val captchaSnippet =
                    "<html>\n    <head>\n        <script src=\"https://www.google.com/recaptcha/api.js?onload=onloadCallback\"></script>\n    </head>\n    <body bgColor=\"$backgroundHex\">\n    <div id=\"captcha\" style=\"display:flex;justify-content:center;align-items:center;overflow:hidden;padding:100px;\"> </div>\n    </body>\n    <script type=\"text/javascript\">\n        function onloadCallback()\n        {\n            grecaptcha.render(\"captcha\", {\n                \"sitekey\" : \"6LeMxrMZAAAAAJEK1UwUc0C-ScFUyJy07f8YN70S\",\n                \"callback\" : function(response) {\n                    console.log(\"koubilgicaptchatoken:\"+response)\n                }\n            })\n        }\n    </script>\n</html>"

                webView.loadDataWithBaseURL(url, captchaSnippet, "text/html", "UTF-8", null).apply {
                    webView.settings.blockNetworkImage = false
                }
            }
        }
    }
}