package cz.mendelu.xmusil5.tictactoe.game

sealed class GameState(){
    object HumanPlayerTurn: GameState()
    object ComputerPlayerTurn: GameState()
    object HumanPlayerWon: GameState()
    object ComputerPlayerWon: GameState()
    object Tie: GameState()
}
