package com.avex.ragraa.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.avex.ragraa.R
import com.avex.ragraa.data.Datasource
import com.avex.ragraa.sharedPreferences
import com.avex.ragraa.ui.calculator.CalculatorScreen
import com.avex.ragraa.ui.calculator.CalculatorViewModel
import com.avex.ragraa.ui.calculator.EditCourse
import com.avex.ragraa.ui.home.HomeScreen
import com.avex.ragraa.ui.home.HomeViewModel
import com.avex.ragraa.ui.home.Settings
import com.avex.ragraa.ui.home.SettingsButton
import com.avex.ragraa.ui.login.LoadingScreen
import com.avex.ragraa.ui.login.LoginScreen
import com.avex.ragraa.ui.login.LoginViewModel
import com.avex.ragraa.ui.login.WebViewScreen
import com.avex.ragraa.ui.marks.AttendancePopup
import com.avex.ragraa.ui.marks.MarksScreen
import com.avex.ragraa.ui.marks.MarksViewModel
import com.avex.ragraa.ui.misc.NavBarHeader
import com.avex.ragraa.ui.misc.NavShape
import com.avex.ragraa.ui.misc.NavigationDrawerItem
import com.avex.ragraa.ui.pastpapers.PastPaperFolder
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
    marksViewModel: MarksViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    //The view-models are initialized here for a cheap dependency injection
    // (I was not aware of Koin at the time of writing this)
    loginViewModel.navController = navController
    homeViewModel.navController = navController
    calculatorViewModel.navController = navController
    marksViewModel.navController = navController

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    //If the user doesn't have a saved login then it'll keep you at the login screen until logged in (i hate the word login)
    var CurrentScreen: Screens by remember { mutableStateOf(Datasource.initScreen) }

    ModalNavigationDrawer(drawerState = drawerState,
        gesturesEnabled = (CurrentScreen != Screens.Web && CurrentScreen != Screens.Login) || (Datasource.rollNo.isNotEmpty() && loginViewModel.uiState.collectAsState().value.showButtons && !loginViewModel.uiState.collectAsState().value.isCompleted && CurrentScreen == Screens.Login),
        drawerContent = {
            ModalDrawerSheet(drawerShape = NavShape(0.dp, 0.8f)) {
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 4.dp)
                )

                val listOfItems = listOf(
                    Screens.Home,
                    Screens.Login,
                    Screens.Marks,
                    Screens.Calculator,
                    Screens.PastPapers,
                    Screens.Transcript
                )

                for (screenItem in listOfItems) {
                    NavigationDrawerItem(
                        screenItem.stringRes, screenItem.icon, CurrentScreen.Title, screenItem.Title
                    ) {
                        scope.launch { drawerState.apply { close() } }; navController.navigate(
                        screenItem.Title
                    )
                    }
                }
            }

        }) {

        val navBar = @Composable {
            Box(modifier = Modifier
                .padding(top = 8.dp, start = 12.dp)
                .border(
                    1.dp, MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(8.dp)
                )
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
            ) Screens.Web.Title else CurrentScreen.Title
        ) {

            composable(Screens.Home.Title) {
                CurrentScreen = Screens.Home
                Column {
                    NavBarHeader(R.string.home,
                        trailingIcon = { SettingsButton { homeViewModel.toggleSettings() } }) { navBar() }
                    HomeScreen(viewModel = homeViewModel)
                }
                AnimatedVisibility(
                    homeViewModel.uiState.collectAsState().value.showSettings,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Settings(
                        homeViewModel
                    )
                }
            }

            composable(Screens.Marks.Title) {
                CurrentScreen = Screens.Marks
                val marksUiState by marksViewModel.uiState.collectAsState()
                Column {
                    NavBarHeader(R.string.marks) { navBar() }
                    MarksScreen(marksViewModel)
                }

                AnimatedVisibility(
                    visible = marksUiState.viewingAttendance, enter = fadeIn(), exit = fadeOut()
                ) {
                    AttendancePopup(marksUiState.currentCourse!!) { marksViewModel.showAttendance() }
                }
            }

            composable(Screens.Login.Title) {
                CurrentScreen = Screens.Login
                Column {
                    if (Datasource.rollNo.isNotEmpty() && loginViewModel.uiState.collectAsState().value.showButtons && !loginViewModel.uiState.collectAsState().value.isCompleted) NavBarHeader(
                        R.string.login
                    ) { navBar() }
                    LoginScreen(loginViewModel)
                }
            }

            composable(Screens.Web.Title) {
                CurrentScreen = Screens.Web

                var showCaptcha by remember { mutableStateOf(false) }

                Surface(modifier = Modifier.fillMaxSize()) {
                    WebViewScreen({ loginViewModel.updateCaptcha(it) },
                        { navController.navigate("login") },
                        { showCaptcha = true },
                        { showCaptcha = false })

                    if (!showCaptcha) {
                        LoadingScreen()
                    }
                }
            }

            composable(Screens.Calculator.Title) {
                CurrentScreen = Screens.Calculator
                Column {
                    NavBarHeader(R.string.calculator) { navBar() }
                    CalculatorScreen(calculatorViewModel)
                }
                if (calculatorViewModel.uiState.collectAsState().value.editingCourse != null) {
                    EditCourse(viewModel = calculatorViewModel)
                }
            }

            composable(Screens.Transcript.Title) {
                CurrentScreen = Screens.Transcript

                Column {
                    NavBarHeader(R.string.transcript) { navBar() }
                    Surface { TranscriptScreen(transcriptViewModel) }
                }
            }

            composable(Screens.PastPapers.Title) {
                CurrentScreen = Screens.PastPapers

                Column {
                    NavBarHeader(R.string.past_papers) { navBar() }
                    PastPaperFolder { navController.navigate(Screens.Home.Title) }
                }

            }
        }
    }

    //Shows the update prompt only when a user isn't on any of the below mentioned screens,
    // so as to not disturb while inputting something
    if (!listOf(Screens.Web, Screens.Login, Screens.Calculator).contains(CurrentScreen)) Updater()
}

enum class Screens(val Title: String, val stringRes: Int, val icon: ImageVector) {
    Home("home", R.string.home, Icons.Filled.Home), Marks(
        "marks", R.string.marks, Icons.Filled.Percent
    ),
    Login("login", R.string.login, Icons.AutoMirrored.Filled.Login), Web(
        "web", R.string.refresh, Icons.Filled.Refresh
    ),
    Calculator(
        "calculator", R.string.calculator, Icons.Filled.Grade
    ),
    Transcript(
        "transcript", R.string.transcript, Icons.AutoMirrored.Filled.Notes
    ),
    PastPapers("pastpapers", R.string.past_papers, Icons.Filled.Map)
}