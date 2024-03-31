package com.avex.ragraa.ui

import android.webkit.ConsoleMessage
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.avex.ragraa.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        Spacer(modifier = Modifier.weight(0.3f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = null
            )
            Text(
                text = "agraa",
                color = Color(0xFFAEF18C),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.offset(x = (-37).dp)
            )
        }

        Spacer(modifier = Modifier.weight(0.2f))

        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = uiState.value.username,
            onValueChange = { viewModel.updateUsername(it) },
            singleLine = true,
            leadingIcon = { Icon(imageVector = Icons.Filled.Person, contentDescription = null) },
            isError = uiState.value.isError,
            textStyle = MaterialTheme.typography.bodyLarge,
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Next)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large)),
            label = { Text(text = "Username") },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color.LightGray
            )
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }

        OutlinedTextField(
            value = uiState.value.password,
            onValueChange = { viewModel.updatePassword(it) },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                viewModel.sendRequest()
            },
            leadingIcon = { Icon(imageVector = Icons.Filled.VpnKey, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        tint = if (passwordVisible) Color.LightGray else Color.DarkGray
                    )
                }
            },
            shape = CutCornerShape(topEnd = 10.dp, bottomStart = 10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color.LightGray
            )
        )

        if (uiState.value.loading) CircularProgressIndicator(
            modifier = Modifier.padding(
                bottom = dimensionResource(
                    id = R.dimen.padding_medium
                )
            )
        )
        else Button(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            onClick = { navController.navigate("web") }
        ) {
            Text("Login", style = MaterialTheme.typography.bodyLarge)
        }

        Text(uiState.value.result, color = Color.White, textAlign = TextAlign.Center)

        Button(
            modifier = Modifier
                .padding(horizontal = dimensionResource(id = R.dimen.padding_large))
                .fillMaxWidth(),
            onClick = { viewModel.getData() }
        ) {
            Text("Sem", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun WebViewScreen(
    viewModel: LoginViewModel,
    navController: NavHostController,
) {
    BackHandler {
        navController.navigate("home")
    }

    val webViewClient: CustomWebViewClient = CustomWebViewClient()
    val webChromeClient: CustomWebChromeClient =
        CustomWebChromeClient(returnHome = { navController.navigate("home") },
            updateToken = { viewModel.updateToken(it) },
            startLogin = { viewModel.sendRequest() })

    val url = "Temp"
    val cookieString = ""
    CookieManager.getInstance().setCookie(url, cookieString)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                this.webViewClient = webViewClient
                this.webChromeClient = webChromeClient
                settings.javaScriptEnabled = true
                settings.setSupportZoom(true)

            }
        }
    ) { webView ->
        webView.loadUrl("https://flexstudent.nu.edu.pk/Login")
    }
}

var loaded = false

class CustomWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        val otherURL = "<html>\n" +
                "    <head>\n" +
                "        <script src=\"https://www.google.com/recaptcha/api.js?onload=onloadCallback\"></script>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "    <div id=\"captcha\" style=\"display:flex;justify-content:center;align-items:center;overflow:hidden;padding:20px;\"> </div>\n" +
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

        if (!loaded) view?.loadDataWithBaseURL(url, otherURL, "text/html", "UTF-8", null);
        loaded = true
    }
}

class CustomWebChromeClient(
    val returnHome: () -> Unit,
    val updateToken: (String) -> Unit,
    val startLogin: () -> Unit
) : WebChromeClient() {
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        val message = consoleMessage?.message()
        if (message?.startsWith("koubilgicaptchatoken:") == true) {
            returnHome()
            val token = message.substring(21);
            updateToken(token)
            loaded = false
            startLogin()
        }

        return super.onConsoleMessage(consoleMessage)
    }
}