package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import androidx.lifecycle.ViewModel
import cz.mendelu.xmusil5.tictactoe.game.Game
import cz.mendelu.xmusil5.tictactoe.game.GameState
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(

): ViewModel() {
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    val uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.Start)
    var boardTiles: MutableStateFlow<Array<PlayerMark?>> = MutableStateFlow(arrayOfNulls(1))

    private lateinit var game: Game

    fun initializeGame(humanPlayerMark: PlayerMark, startingPlayerMark: PlayerMark){
        val newGame = Game.startStandardGame(
            humanPlayerMark = humanPlayerMark
        )
        game = newGame
        boardTiles = game.boardTiles

        coroutineScope.launch {
            game.state.collectLatest {
                when(it){
                    GameState.ComputerPlayerTurn -> uiState.value = GameUiState.ComputerPlayerTurn
                    GameState.ComputerPlayerWon -> uiState.value = GameUiState.ComputerPlayerVictory
                    GameState.HumanPlayerTurn -> uiState.value = GameUiState.HumanPlayerTurn
                    GameState.HumanPlayerWon -> uiState.value = GameUiState.HumanPlayerVictory
                    GameState.Tie -> uiState.value = GameUiState.Tie
                }
            }
        }
    }

}