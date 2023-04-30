package cz.mendelu.xmusil5.tictactoe.game

import cz.mendelu.xmusil5.tictactoe.game.board.VictoryPath

sealed class GameState(){
    class HumanPlayerTurn: GameState()
    class ComputerPlayerTurn: GameState()
    class HumanPlayerWon(val victoryPath: VictoryPath): GameState()
    class ComputerPlayerWon(val victoryPath: VictoryPath): GameState()
    class Tie: GameState()
}
