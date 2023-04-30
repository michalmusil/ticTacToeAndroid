package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import cz.mendelu.xmusil5.tictactoe.game.board.VictoryPath

sealed class GameUiState{
    class Start: GameUiState()
    class HumanPlayerTurn: GameUiState()
    class ComputerPlayerTurn: GameUiState()
    class HumanPlayerVictory(val victoryPath: VictoryPath): GameUiState()
    class ComputerPlayerVictory(val victoryPath: VictoryPath): GameUiState()
    class Tie: GameUiState()
}
