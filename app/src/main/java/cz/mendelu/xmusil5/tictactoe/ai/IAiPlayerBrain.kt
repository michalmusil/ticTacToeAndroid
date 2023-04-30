package cz.mendelu.xmusil5.tictactoe.ai

interface IAiPlayerBrain {

    suspend fun makeDecision(boardState: IntArray): Int

}