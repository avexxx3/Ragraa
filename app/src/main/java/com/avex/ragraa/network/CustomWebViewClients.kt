package com.avex.ragraa.network

import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

var captchaLoaded = false

class CustomWebViewClient(
    val backgroundHex: String,
    val showCaptcha: () -> Unit
) : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Log.d("Dev", "Webview page loaded: $captchaLoaded")

        val otherURL =
            "<html>\n" +
                    "    <head>\n" +
                    "        <script src=\"https://www.google.com/recaptcha/api.js?onload=onloadCallback\"></script>\n" +
                    "    </head>\n" +
                    "    <body bgColor=\"$backgroundHex\">\n" +
                    "    <div id=\"captcha\" style=\"display:flex;justify-content:center;align-items:center;overflow:hidden;padding:100px;\"> </div>\n" +
                    "    </body>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "        function onloadCallback()\n" +
                    "        {\n" +
                    "            grecaptcha.render(\"captcha\", {\n" +
                    "                \"sitekey\" : \"6LeMxrMZAAAAAJEK1UwUc0C-ScFUyJy07f8YN70S\",\n" +
                    "                \"callback\" : function(response) {\n" +
                    "                    console.log(\"koubilgicaptchatoken:\"+response)\n" +
                    "                }\n" +
                    "            })\n" +
                    "        }\n" +
                    "    </script>\n" +
                    "</html>"

        if (!captchaLoaded) {
            captchaLoaded = true
            Log.d("Dev", "Load the otherURL")
            view?.loadDataWithBaseURL(url, otherURL, "text/html", "UTF-8", null)
        }

        showCaptcha()
    }
}