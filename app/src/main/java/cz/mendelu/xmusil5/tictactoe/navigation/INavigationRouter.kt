package cz.mendelu.xmusil5.tictactoe.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun setNavController(navController: NavController)
    fun returnBack()

    fun toSplashScreen()
    fun toStartupScreen()
    fun toGameScreen(player: String)
}