package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

sealed class GameUiState{
    object Start: GameUiState()
    object HumanPlayerTurn: GameUiState()
    object ComputerPlayerTurn: GameUiState()
    object HumanPlayerVictory: GameUiState()
    object ComputerPlayerVictory: GameUiState()
}
