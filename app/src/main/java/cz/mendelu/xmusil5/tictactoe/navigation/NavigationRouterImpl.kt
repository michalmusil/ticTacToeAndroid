package cz.mendelu.xmusil5.tictactoe.navigation

import androidx.navigation.NavController

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
        navController.navigate(Destination.StartupScreen.route)
    }

    override fun toGameScreen(player: String) {
        navController.navigate("Destination.GameScreen.route/${player}")
    }
}