package com.avex.ragraa.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.sharedPreferences
import com.avex.ragraa.ui.attendance.AttendanceScreen
import com.avex.ragraa.ui.calculator.CalculatorScreen
import com.avex.ragraa.ui.calculator.CalculatorViewModel
import com.avex.ragraa.ui.home.HomeScreen
import com.avex.ragraa.ui.home.HomeViewModel
import com.avex.ragraa.ui.login.LoginScreen
import com.avex.ragraa.ui.login.LoginViewModel
import com.avex.ragraa.ui.login.WebViewScreen
import com.avex.ragraa.ui.marks.MarksScreen
import com.avex.ragraa.ui.misc.NavShape
import com.avex.ragraa.ui.theme.sweetie_pie
import com.avex.ragraa.ui.transcript.TranscriptScreen
import com.avex.ragraa.ui.transcript.TranscriptViewModel
import com.avex.ragraa.ui.updater.Updater
import kotlinx.coroutines.launch

@Composable
fun FlexApp(
    loginViewModel: LoginViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel(),
    calculatorViewModel: CalculatorViewModel = viewModel(),
    transcriptViewModel: TranscriptViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    //The view-models are initialized here for a cheap dependency injection
    // (I was not aware of Koin at the time of writing this)
    loginViewModel.navController = navController
    homeViewModel.navController = navController
    calculatorViewModel.navController = navController

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //If the user doesn't have a saved login then it'll keep you at the login screen until logged in (i hate the word login)
    var currentScreen by remember { mutableStateOf(if (Datasource.rollNo.isEmpty()) "login" else "home") }

    ModalNavigationDrawer(drawerState = drawerState,
        gesturesEnabled = !(currentScreen == "web" || (Datasource.rollNo.isEmpty() && currentScreen == "login")),
        drawerContent = {
            ModalDrawerSheet(drawerShape = NavShape(0.dp, 0.8f)) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 4.dp)
                )
                NavigationDrawerItem(label = { Text(text = stringResource(R.string.home)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Home, contentDescription = null
                        )
                    },
                    selected = currentScreen == "home",
                    onClick = {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        "home"
                    )
                    })
                NavigationDrawerItem(label = { Text(text = stringResource(R.string.login)) },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null
                        )
                    },
                    selected = currentScreen == "login",
                    onClick = {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        "login"
                    )
                    })
                NavigationDrawerItem(label = { Text(text = stringResource(R.string.marks)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Percent, contentDescription = null
                        )
                    },
                    selected = currentScreen == "marks",
                    onClick = {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        "marks"
                    )
                    })
                NavigationDrawerItem(label = { Text(text = stringResource(R.string.attendance)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Checklist, contentDescription = null
                        )
                    },
                    selected = currentScreen == "attendance",
                    onClick = {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        "attendance"
                    )
                    })
                NavigationDrawerItem(label = { Text(text = stringResource(R.string.calculator)) },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Grade, contentDescription = null
                        )
                    },
                    selected = currentScreen == "calculator",
                    onClick = {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        "calculator"
                    )
                    })
                NavigationDrawerItem(label = { Text(text = stringResource(R.string.transcript)) },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = null
                        )
                    },
                    selected = currentScreen == "transcript",
                    onClick = {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        "transcript"
                    )
                    })
            }

        }) {
        val navBar = @Composable {
            Box(modifier = Modifier
                .padding(top = 8.dp, start = 12.dp)
                .border(1.dp, Color.White, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
                .clickable { scope.launch { drawerState.apply { open() } } }) {
                Icon(
                    imageVector = Icons.Filled.Diamond,
                    tint = sweetie_pie,
                    contentDescription = null
                )
            }
            Spacer(Modifier.padding(vertical = 4.dp))
        }

        NavHost(
            navController, if (sharedPreferences.getBoolean(
                    "startupRefresh", false
                )
            ) "web" else if (Datasource.rollNo.isEmpty()) "login" else "home"
        ) {
            composable("home") {
                currentScreen = "home"
                HomeScreen(viewModel = homeViewModel, navBar = navBar)
            }

            composable("marks") {
                currentScreen = "marks"
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        navBar();Text(
                        stringResource(R.string.marks),
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 12.dp)
                    )
                    }
                    MarksScreen(navController)
                }
            }

            composable("login") {
                currentScreen = "login"
                LoginScreen(loginViewModel, navBar)
            }

            composable("web") {
                currentScreen = "web"
                WebViewScreen(
                    { loginViewModel.updateCaptcha(it) },
                    { navController.navigate("login") })
            }

            composable("attendance") {
                currentScreen = "attendance"
                AttendanceScreen(navBar = navBar)
            }

            composable("calculator") {
                currentScreen = "calculator"
                CalculatorScreen(calculatorViewModel, navBar = navBar)
            }

            composable("transcript") {
                currentScreen = "transcript"
                TranscriptScreen(navBar = navBar, navController = navController)
            }
        }
    }

    //Shows the update prompt only when a user isn't on any of the below mentioned screens,
    // so as to not disturb while inputting something
    if (!listOf("web", "login", "calculator").contains(currentScreen)) Updater()
}
