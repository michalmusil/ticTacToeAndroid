package cz.mendelu.xmusil5.tictactoe.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen.GameScreen
import cz.mendelu.xmusil5.tictactoe.ui.screens.splash_screen.SplashScreen
import cz.mendelu.xmusil5.tictactoe.ui.screens.startup_screen.StartupScreen


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
                StartupScreen(navigation = navigation)
            }

            composable(Destination.GameScreen.route + "?humanPlayerSymbol={humanPlayerSymbol}?startingMarkSymbol={startingMarkSymbol}",
                arguments = listOf(
                    navArgument("humanPlayerSymbol") {
                        type = NavType.StringType
                    },
                    navArgument("startingMarkSymbol") {
                        type = NavType.StringType
                    }
                )) {
                val humanPlayerSymbol = it.arguments?.getString("humanPlayerSymbol")
                val startingMarkSymbol = it.arguments?.getString("startingMarkSymbol")

                val humanPlayerMark = PlayerMark.getBySymbol(humanPlayerSymbol!!)
                val startingSymbolMark = PlayerMark.getBySymbol(startingMarkSymbol!!)
                GameScreen(
                    humanPlayerMark = humanPlayerMark,
                    startingPlayerMark = startingSymbolMark,
                    navigation = navigation
                )
            }
        }
    }
}
