package cz.mendelu.xmusil5.tictactoe.game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Game(
    val board: Board,
    val startingMark: PlayerMark,
    val humanPlayer: Player,
    val computerPlayer: Player,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val state: MutableState<GameState> = when{
        computerPlayer.mark == startingMark -> {
            mutableStateOf(GameState.ComputerPlayerTurn)
        }
        else -> {
            mutableStateOf(GameState.HumanPlayerTurn)
        }
    }
) {
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
            throw IllegalStateException()
        }
    }

    private fun makeComputerPlayerTurn(){

        // make the turn.. adjust the board

        if (checkForGameEnd()){
            return
        }
        state.value = GameState.HumanPlayerTurn
    }

    private fun checkForVictoriousPlayer(): Player?{
        return null
    }

    private fun isTie(): Boolean{
        return false
    }

    private fun isTileIndexFree(tileIndex: Int): Boolean{
        return !board.getFreeTileIndexes().contains(tileIndex)
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
            val humanPlayer = Player(playerType = PlayerType.HUMAN, mark = humanPlayerMark)
            val oppositePlayerMark = PlayerMark.values().first{
                it != humanPlayerMark
            }
            val computerPlayer = Player(playerType = PlayerType.COMPUTER, mark = oppositePlayerMark)
            val startingMark = PlayerMark.X

            return Game(
                board = board,
                startingMark = startingMark,
                humanPlayer = humanPlayer,
                computerPlayer = computerPlayer,
            )
        }
    }

}