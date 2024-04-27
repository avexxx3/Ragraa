package com.avex.ragraa.network

import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

var captchaLoaded = false

class CustomWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val otherURL = "<html>\n" +
                "    <head>\n" +
                "        <script src=\"https://www.google.com/recaptcha/api.js?onload=onloadCallback\"></script>\n" +
                "    </head>\n" +
                "    <body>\n" +
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

        if (!captchaLoaded) view?.loadDataWithBaseURL(url, otherURL, "text/html", "UTF-8", null)
        captchaLoaded = true
    }
}

class CustomWebChromeClient (val updateCaptcha:(String) -> Unit)
    : WebChromeClient() {
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {

        val message = consoleMessage?.message()
        if (message?.startsWith("koubilgicaptchatoken:") == true) {
            val captchaToken = message.substring(21)
            updateCaptcha(captchaToken)
        }

        return super.onConsoleMessage(consoleMessage)
    }
}