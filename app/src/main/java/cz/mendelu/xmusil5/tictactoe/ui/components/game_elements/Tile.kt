package cz.mendelu.xmusil5.tictactoe.ui.components.game_elements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import cz.mendelu.xmusil5.tictactoe.ui.utils.UiConstants

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Tile(
    containedMark: PlayerMark?,
    enabled: Boolean,
    size: Dp,
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
            .border(width = borderWidth, color = borderColor)
            .padding(5.dp)
    ){
        containedMark?.let { mark ->
            var visible by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(true){
                visible = true
            }
            AnimatedVisibility(
                visible = visible,
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
                        .size((size.value * 0.8).dp)
                )
            }
        }
    }
}