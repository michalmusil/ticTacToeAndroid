package cz.mendelu.xmusil5.tictactoe.navigation

sealed class Destination(val route: String){
    object SplashScreen: Destination("splash")
    object StartupScreen: Destination("startup")
    object GameScreen: Destination("game")
}