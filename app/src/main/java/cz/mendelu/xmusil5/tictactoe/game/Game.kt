package cz.mendelu.xmusil5.tictactoe.game

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class Game(
    private val board: Board,
    private val numberOfMarksToWin: Int,
    private val startingMark: PlayerMark,
    val humanPlayer: Player,
    private val computerPlayer: Player,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
) {
    val boardTiles: MutableStateFlow<Array<PlayerMark?>> = board.tiles
    val numberOfHorizontalTiles: Int = board.sizeX
    val numberOfVerticalTiles: Int = board.sizeY
    val state: MutableStateFlow<GameState> = when{
        computerPlayer.mark == startingMark -> {
            MutableStateFlow(GameState.ComputerPlayerTurn)
        }
        else -> {
            MutableStateFlow(GameState.HumanPlayerTurn)
        }
    }

    init {
        if (state.value is GameState.ComputerPlayerTurn){
            makeComputerPlayerTurn()
        }
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
                state.value = GameState.ComputerPlayerTurn
            }
        } else {
            throw UnsupportedOperationException()
        }
    }

    fun isTileAtIndexEmpty(index: Int): Boolean{
        return board.getFreeTileIndexes().contains(index)
    }

    private fun makeComputerPlayerTurn(){

        // TEST WITH RANDOM MOVE
        val freeIndexes1 = board.getFreeTileIndexes()
        val oponentDecision = freeIndexes1.get(
            Random.nextInt(from = 0, until = freeIndexes1.size)
        )



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
        state.value = GameState.HumanPlayerTurn
    }






    private fun checkForVictoriousPlayer(): Player?{
        var victoriousPlayerType: PlayerType? = null
        val victoryFromRows = board.checkRowsForVictoriousMark(numberOfMarksToWin)
        victoryFromRows?.let {
            victoriousPlayerType = getPlayerTypeFromMark(it)
        }
        val victoryFromColumns = board.checkColumnsForVictoriousMark(numberOfMarksToWin)
        victoryFromColumns?.let {
            victoriousPlayerType = getPlayerTypeFromMark(it)
        }
        val victoryFromDiagonals = board.checkDiagonalsForVictoriousMark(numberOfMarksToWin)
        victoryFromDiagonals?.let {
            victoriousPlayerType = getPlayerTypeFromMark(it)
        }
        when(victoriousPlayerType){
            PlayerType.HUMAN -> return humanPlayer
            PlayerType.COMPUTER -> return computerPlayer
            null -> return null
        }
    }

    private fun getPlayerTypeFromMark(mark: PlayerMark): PlayerType{
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

    private fun checkForGameEnd(): Boolean{
        val victoriousPlayer = checkForVictoriousPlayer()
        if (victoriousPlayer != null) {
            when(victoriousPlayer.playerType){
                PlayerType.HUMAN -> {
                    state.value = GameState.HumanPlayerWon
                }
                PlayerType.COMPUTER -> {
                    state.value = GameState.ComputerPlayerWon
                }
            }
            return true
        }
        if (isTie()){
            state.value = GameState.Tie
            return true
        }
        return false
    }





    companion object{
        fun startStandardGame(humanPlayerMark: PlayerMark): Game{
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
            )
        }
    }

}