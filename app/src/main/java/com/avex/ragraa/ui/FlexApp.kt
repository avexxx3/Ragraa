package com.avex.ragraa.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun FlexApp(
    loginViewModel: LoginViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        "home"
    ) {
        composable("home") {
            LoginScreen(loginViewModel, navController)
        }

        composable("web") {
            WebViewScreen(loginViewModel, navController)
        }
    }
}