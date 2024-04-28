package com.avex.ragraa.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.ui.home.HomeScreen
import com.avex.ragraa.ui.marks.MarksScreen
import com.avex.ragraa.ui.login.LoginScreen
import com.avex.ragraa.ui.login.LoginViewModel
import com.avex.ragraa.ui.login.WebViewScreen
import io.objectbox.Box

@Composable
fun FlexApp(
    loginViewModel: LoginViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    loginViewModel.navController = navController


    NavHost(
        navController,
        "home"
    ) {
        composable("home") {
            HomeScreen(navController)
        }

        composable("marks") {
            MarksScreen(navController)
        }

        composable("login") {
            LoginScreen(loginViewModel)
        }

        composable("web") {
            WebViewScreen({loginViewModel.updateCaptcha(it)}, {navController.navigate("login")})
        }
    }
}
