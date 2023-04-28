package cz.mendelu.xmusil5.tictactoe.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.xmusil5.tictactoe.ui.screens.splash_screen.SplashScreen


@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter,
    startDestination: String
) {
    var navigationInitialized by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        navigation.setNavController(navController)
        navigationInitialized = true
    }

    if (navigationInitialized) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Destination.SplashScreen.route) {
                SplashScreen(navigation = navigation)
            }

            composable(Destination.StartupScreen.route) {

            }

            composable(Destination.GameScreen.route + "/{playerIdentifier}",
                arguments = listOf(
                    navArgument("playerIdentifier") {
                        type = NavType.StringType
                    }
                )) {
                val playerIdentifier = it.arguments?.getString("playerIdentifier")

            }
        }
    }
}
