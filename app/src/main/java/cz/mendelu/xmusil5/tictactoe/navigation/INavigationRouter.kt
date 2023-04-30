package cz.mendelu.xmusil5.tictactoe.navigation

import androidx.navigation.NavController
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark

interface INavigationRouter {
    fun getNavController(): NavController
    fun setNavController(navController: NavController)
    fun returnBack()

    fun toSplashScreen()
    fun toStartupScreen()
    fun toGameScreen(humanPlayerMark: PlayerMark, startingMark: PlayerMark)
}