package cz.mendelu.xmusil5.tictactoe.game

import cz.mendelu.xmusil5.tictactoe.ai.IAiPlayerBrain
import cz.mendelu.xmusil5.tictactoe.game.board.Board
import cz.mendelu.xmusil5.tictactoe.game.board.VictoryPath
import cz.mendelu.xmusil5.tictactoe.game.player.Player
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.random.Random

class Game(
    private val board: Board,
    private val numberOfMarksToWin: Int,
    private val startingMark: PlayerMark,
    val humanPlayer: Player,
    private val computerPlayer: Player,
    private val computerPlayerBrain: IAiPlayerBrain,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    val boardTiles: MutableStateFlow<Array<PlayerMark?>> = board.tiles
    val numberOfHorizontalTiles: Int = board.sizeX
    val numberOfVerticalTiles: Int = board.sizeY
    val state: MutableStateFlow<GameState> = when{
        computerPlayer.mark == startingMark -> {
            MutableStateFlow(GameState.ComputerPlayerTurn())
        }
        else -> {
            MutableStateFlow(GameState.HumanPlayerTurn())
        }
    }

    init {
        coroutineScope.launch {
            observeForComputerPlayer()
        }
    }

    suspend fun observeForComputerPlayer(){
        while (state.value is GameState.ComputerPlayerTurn || state.value is GameState.HumanPlayerTurn){
            if (state.value is GameState.ComputerPlayerTurn){
                makeComputerPlayerTurn()
            }
            delay(1000)
        }
    }




    fun makeHumanPlayerTurn(indexOfBoardTile: Int){
        if (state.value is GameState.HumanPlayerTurn){
            if(isTileIndexFree(indexOfBoardTile)){
                board.setTile(
                    index = indexOfBoardTile,
                    mark = humanPlayer.mark
                )
                if (checkForGameEnd()){
                    return
                }
                state.value = GameState.ComputerPlayerTurn()
            }
        } else {
            throw UnsupportedOperationException()
        }
    }

    fun isTileAtIndexEmpty(index: Int): Boolean{
        return board.getFreeTileIndexes().contains(index)
    }

    private suspend fun makeComputerPlayerTurn(){
        val currentBoardTiles = board.getEncodedStateRepresentation(
            humanPlayerMark = humanPlayer.mark
        )
        val oponentDecision = computerPlayerBrain.makeDecision(currentBoardTiles)



        if(isTileIndexFree(oponentDecision)){
            board.setTile(
                index = oponentDecision,
                mark = computerPlayer.mark
            )
        } else {
            val freeIndexes = board.getFreeTileIndexes()
            val selectedIndex = freeIndexes.get(
                Random.nextInt(from = 0, until = freeIndexes.size)
            )
            board.setTile(
                index = selectedIndex,
                mark = computerPlayer.mark
            )
        }

        if (checkForGameEnd()){
            return
        }
        state.value = GameState.HumanPlayerTurn()
    }




    private fun checkForGameEnd(): Boolean{
        val victoryPath = checkForVictoryPath()
        victoryPath?.let {
            val victoriousPlayerType = getPlayerTypeFromMark(it.playerMark)
            when(victoriousPlayerType){
                PlayerType.HUMAN -> {
                    state.value = GameState.HumanPlayerWon(it)
                }
                PlayerType.COMPUTER -> {
                    state.value = GameState.ComputerPlayerWon(it)
                }
            }
            return true
        }
        if (isTie()){
            state.value = GameState.Tie()
            return true
        }
        return false
    }

    private fun checkForVictoryPath(): VictoryPath?{
        val victoryFromRows = board.checkRowsForVictoryPath(numberOfMarksToWin)
        victoryFromRows?.let {
            return it
        }
        val victoryFromColumns = board.checkColumnsForVictoryPath(numberOfMarksToWin)
        victoryFromColumns?.let {
            return it
        }
        val victoryFromDiagonals = board.checkDiagonalsForVictoriousMark(numberOfMarksToWin)
        victoryFromDiagonals?.let {
            return it
        }
        return null
    }

    private fun getPlayerTypeFromMark(mark: PlayerMark): PlayerType {
        if (computerPlayer.mark == mark){
            return PlayerType.COMPUTER
        } else {
            return PlayerType.HUMAN
        }
    }

    private fun isTie(): Boolean{
        return board.getFreeTileIndexes().isEmpty()
    }

    private fun isTileIndexFree(tileIndex: Int): Boolean{
        return board.getFreeTileIndexes().contains(tileIndex)
    }





    companion object{
        fun startStandardGame(humanPlayerMark: PlayerMark, computerPlayerBrain: IAiPlayerBrain): Game{
            val board = Board(sizeX = 3, sizeY = 3)
            val numberOfMarksToWin = 3
            val humanPlayer = Player(playerType = PlayerType.HUMAN, mark = humanPlayerMark)
            val oppositePlayerMark = PlayerMark.values().first{
                it != humanPlayerMark
            }
            val computerPlayer = Player(playerType = PlayerType.COMPUTER, mark = oppositePlayerMark)
            val startingMark = PlayerMark.X

            return Game(
                board = board,
                numberOfMarksToWin = numberOfMarksToWin,
                startingMark = startingMark,
                humanPlayer = humanPlayer,
                computerPlayer = computerPlayer,
                computerPlayerBrain = computerPlayerBrain
            )
        }

        fun startCustomGame(
            width: Int,
            height: Int,
            numberOfMarksToWin: Int,
            humanPlayerMark: PlayerMark,
            startingMark: PlayerMark,
            computerPlayerBrain: IAiPlayerBrain
        ): Game{
            val maxDimension = max(width, height)
            if (width <= 0 || height <= 0 || numberOfMarksToWin > maxDimension){
                throw IllegalStateException()
            }

            val board = Board(sizeX = width, sizeY = height)
            val humanPlayer = Player(playerType = PlayerType.HUMAN, mark = humanPlayerMark)
            val oppositePlayerMark = PlayerMark.values().first{
                it != humanPlayerMark
            }
            val computerPlayer = Player(playerType = PlayerType.COMPUTER, mark = oppositePlayerMark)

            return Game(
                board = board,
                numberOfMarksToWin = numberOfMarksToWin,
                startingMark = startingMark,
                humanPlayer = humanPlayer,
                computerPlayer = computerPlayer,
                computerPlayerBrain = computerPlayerBrain
            )
        }
    }

}