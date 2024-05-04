package com.avex.ragraa.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.ui.home.HomeScreen
import com.avex.ragraa.ui.home.HomeViewModel
import com.avex.ragraa.ui.marks.MarksScreen
import com.avex.ragraa.ui.login.LoginScreen
import com.avex.ragraa.ui.login.LoginViewModel
import com.avex.ragraa.ui.login.WebViewScreen

@Composable
fun FlexApp(
    loginViewModel: LoginViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    loginViewModel.navController = navController
    homeViewModel.navController = navController

    Datasource.cacheData()
    loginViewModel.init()
    homeViewModel.init()

    NavHost(
        navController,
        if(Datasource.rollNo.isEmpty()) "login" else "home"
    ) {
        composable("home") {
            HomeScreen(homeViewModel)
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
