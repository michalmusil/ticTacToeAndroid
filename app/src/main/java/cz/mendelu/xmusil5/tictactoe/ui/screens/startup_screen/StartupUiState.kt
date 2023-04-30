package cz.mendelu.xmusil5.tictactoe.ui.screens.startup_screen

import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark

sealed class StartupUiState{
    class Start: StartupUiState()
    class GameStarted(val humanPlayerMark: PlayerMark, val startingPlayerMark: PlayerMark): StartupUiState()
}
