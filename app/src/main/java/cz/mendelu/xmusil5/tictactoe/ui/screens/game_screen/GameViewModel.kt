package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(

): ViewModel() {
    val uiState: MutableStateFlow<GameUiState> = MutableStateFlow(GameUiState.Start)

}