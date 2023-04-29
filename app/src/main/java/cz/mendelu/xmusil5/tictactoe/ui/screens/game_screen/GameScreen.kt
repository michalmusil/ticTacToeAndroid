package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter

@Composable
fun GameScreen(
    humanPlayerMark: PlayerMark,
    startingPlayerMark: PlayerMark,
    navigation: INavigationRouter,
    viewModel: GameViewModel = hiltViewModel()
){

}