package cz.mendelu.xmusil5.tictactoe.game

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.ui.theme.oColor
import cz.mendelu.xmusil5.tictactoe.ui.theme.xColor

enum class PlayerMark(val symbol: String, val iconId: Int, val color: Color) {
    X(symbol = "X", iconId = R.drawable.ic_x, color = xColor),
    O(symbol = "O", iconId = R.drawable.ic_o, color = oColor);

    companion object {
        fun getBySymbol(symbol: String): PlayerMark {
            return PlayerMark.values().first {
                it.symbol == symbol
            }
        }
    }
}