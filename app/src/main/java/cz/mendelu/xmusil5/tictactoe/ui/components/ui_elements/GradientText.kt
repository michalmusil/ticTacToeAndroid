package cz.mendelu.xmusil5.tictactoe.ui.components.ui_elements

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.tictactoe.ui.theme.brunoAce
import cz.mendelu.xmusil5.tictactoe.ui.theme.oColor
import cz.mendelu.xmusil5.tictactoe.ui.theme.xColor

@OptIn(ExperimentalTextApi::class)
@Composable
fun GradientText(
    text: String,
    textSize: TextUnit = 23.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    fontFamily: FontFamily = brunoAce,
    modifier: Modifier = Modifier,
){
    val gradientColors = listOf(oColor, xColor)
    Text(
        text = text,
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = gradientColors
            ),
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = textSize
        ),
        modifier = Modifier
    )
}