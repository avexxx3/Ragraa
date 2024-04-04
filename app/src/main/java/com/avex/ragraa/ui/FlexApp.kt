package com.avex.ragraa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.ui.home.HomeScreen
import com.avex.ragraa.ui.login.LoginScreen
import com.avex.ragraa.ui.login.LoginViewModel
import com.avex.ragraa.ui.login.WebViewScreen

@Composable
fun FlexApp(
    loginViewModel: LoginViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    loginViewModel.navController = navController

    if (loginViewModel.uiState.collectAsState().value.parseComplete) {
        navController.navigate("home")
    }

    NavHost(
        navController,
        "login"
    ) {
        composable("login") {
            LoginScreen(loginViewModel, navController)
        }

        composable("web") {
            WebViewScreen(loginViewModel, navController)
        }

        composable("home") {
            HomeScreen()
        }
    }
}
