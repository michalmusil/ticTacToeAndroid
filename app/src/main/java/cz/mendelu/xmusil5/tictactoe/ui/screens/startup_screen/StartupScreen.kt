package cz.mendelu.xmusil5.tictactoe.ui.screens.startup_screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import cz.mendelu.xmusil5.tictactoe.ui.utils.UiConstants

@Composable
fun StartupScreen(
    navigation: INavigationRouter,
    viewModel: StartupViewModel = hiltViewModel()
){
    val uiState = viewModel.state.collectAsState()
    uiState.value.let {
        when(it){
            is StartupUiState.Start -> {
                StartupScreenContent(viewModel = viewModel)
            }
            is StartupUiState.GameStarted -> {
                LaunchedEffect(it){
                    navigation.toGameScreen(
                        humanPlayerMark = it.humanPlayerMark,
                        startingMark = it.startingPlayerMark
                    )
                }
            }
        }
    }
}

@Composable
fun StartupScreenContent(
    viewModel: StartupViewModel
){
    val chosenPlayerMark = viewModel.chosenPlayerMark.collectAsState()
    val startingPlayerMark = viewModel.startingMark.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                PlayerMark.values().forEach {
                    PlayerMarkOption(
                        mark = it,
                        chosenPlayerMark = chosenPlayerMark.value,
                        startingPlayerMark = startingPlayerMark.value,
                        onClick = {
                            viewModel.chosenPlayerMark.value = it
                        }
                    )
                }
            }
        }

    }
    
}

@Composable
fun PlayerMarkOption(
    mark: PlayerMark,
    chosenPlayerMark: PlayerMark?,
    startingPlayerMark: PlayerMark,
    onClick: (PlayerMark) -> Unit
){
    val size = 100.dp
    val cornerRadius = UiConstants.RADIUS_SMALL
    val selectedColor by animateColorAsState(
        targetValue = if (mark == chosenPlayerMark) MaterialTheme.colorScheme.primary else Color.Transparent,
        animationSpec = tween(UiConstants.ANIMATION_DURATION_SHORT)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(16.dp)
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable {
                onClick(mark)
            }
            .background(MaterialTheme.colorScheme.surface)
            .border(width = 5.dp, color = selectedColor, shape = RoundedCornerShape(cornerRadius))
            .padding(10.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = mark.iconId),
                contentDescription = mark.symbol,
                tint = mark.color,
                modifier = Modifier
                    .size((size.value*0.7).dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
            ){
                if (startingPlayerMark == mark){
                    Text(
                        text = stringResource(id = R.string.starting),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}