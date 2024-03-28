package com.avex.ragraa.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun FlexApp(
    loginViewModel: LoginViewModel = viewModel(),
    navController : NavHostController = rememberNavController()
) {
    LoginScreen(loginViewModel)
}