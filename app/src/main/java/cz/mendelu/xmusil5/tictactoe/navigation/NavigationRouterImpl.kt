package cz.mendelu.xmusil5.tictactoe.navigation

import androidx.navigation.NavController
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark

class NavigationRouterImpl: INavigationRouter {

    private lateinit var navController: NavController

    private fun emptyBackstackAndNavigate(destination: Destination){
        navController.backQueue.clear()
        navController.navigate(destination.route)
    }

    override fun setNavController(navController: NavController){
        this.navController = navController
    }

    override fun getNavController(): NavController {
        return this.navController
    }

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun toSplashScreen() {
        navController.navigate(Destination.SplashScreen.route)
    }

    override fun toStartupScreen() {
        emptyBackstackAndNavigate(Destination.StartupScreen)
    }

    override fun toGameScreen(humanPlayerMark: PlayerMark, startingMark: PlayerMark) {
        navController.navigate(
            "${Destination.GameScreen.route}?humanPlayerSymbol=${humanPlayerMark.symbol}?startingMarkSymbol=${startingMark.symbol}"
        )
    }
}