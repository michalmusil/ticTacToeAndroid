package cz.mendelu.xmusil5.tictactoe.ui.components.game_elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.tictactoe.game.board.LineOrientation
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark
import cz.mendelu.xmusil5.tictactoe.ui.utils.UiConstants

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Tile(
    containedMark: PlayerMark?,
    enabled: Boolean,
    size: Dp,
    lineToDraw: LineOrientation?,
    onClick: () -> Unit
){
    val borderWidth = 3.dp
    val borderColor = MaterialTheme.colorScheme.primary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clickable {
                if (enabled) {
                    onClick()
                }
            }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = borderWidth, color = borderColor)
        )
        containedMark?.let { mark ->
            var markVisible by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(true){
                markVisible = true
            }
            AnimatedVisibility(
                visible = markVisible,
                enter = scaleIn(
                    animationSpec = tween(durationMillis = UiConstants.ANIMATION_DURATION_SHORT)
                ),
                exit = scaleOut(
                    animationSpec = tween(durationMillis = UiConstants.ANIMATION_DURATION_SHORT)
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = mark.iconId),
                    contentDescription = null,
                    tint = mark.color,
                    modifier = Modifier
                        .padding(5.dp)
                        .size((size.value * 0.8).dp)
                )
            }

            lineToDraw?.let {
                LineDrawingCanvas(
                    lineToDraw = it,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LineDrawingCanvas(
    lineToDraw: LineOrientation,
    color: Color
){
    val strokeWidth = 25f

    var lineVisible by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(true){
        lineVisible = true
    }
    AnimatedVisibility(
        visible = lineVisible,
        enter = scaleIn(
            animationSpec = tween(durationMillis = UiConstants.ANIMATION_DURATION_SHORT)
        ),
        exit = scaleOut(
            animationSpec = tween(durationMillis = UiConstants.ANIMATION_DURATION_SHORT)
        )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val width = size.width
            val height = size.height
            when (lineToDraw) {
                LineOrientation.HORIZONTAL -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = height / 2),
                        end = Offset(x = width, y = height / 2),
                        strokeWidth = strokeWidth
                    )
                }
                LineOrientation.VERTICAL -> {
                    drawLine(
                        color = color,
                        start = Offset(x = width / 2, y = 0f),
                        end = Offset(x = width / 2, y = height),
                        strokeWidth = strokeWidth
                    )
                }
                LineOrientation.DIAGONAL_UP -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = height),
                        end = Offset(x = width, y = 0f),
                        strokeWidth = strokeWidth
                    )
                }
                LineOrientation.DIAGONAL_DOWN -> {
                    drawLine(
                        color = color,
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = width, y = height),
                        strokeWidth = strokeWidth
                    )
                }
            }
        }
    }
}