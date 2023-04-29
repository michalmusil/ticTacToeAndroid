package cz.mendelu.xmusil5.tictactoe.ui.screens.startup_screen

import androidx.lifecycle.ViewModel
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(

): ViewModel() {

    val state: MutableStateFlow<StartupUiState> = MutableStateFlow(StartupUiState.Start())

    val chosenPlayerMark: MutableStateFlow<PlayerMark?> = MutableStateFlow(null)
    val startingMark: MutableStateFlow<PlayerMark> = MutableStateFlow(PlayerMark.X)
}