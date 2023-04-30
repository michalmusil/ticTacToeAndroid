package cz.mendelu.xmusil5.tictactoe.game.board

import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark

data class VictoryPath(
    val playerMark: PlayerMark,
    val lineOrientation: LineOrientation,
    val pathIndexes: IntArray
)
