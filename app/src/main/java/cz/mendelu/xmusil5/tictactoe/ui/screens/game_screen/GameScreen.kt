package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import android.widget.Space
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.game.PlayerMark
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import cz.mendelu.xmusil5.tictactoe.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.tictactoe.ui.theme.red

@Composable
fun GameScreen(
    humanPlayerMark: PlayerMark,
    startingPlayerMark: PlayerMark,
    navigation: INavigationRouter,
    viewModel: GameViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsState()
    uiState.value.let {
        when(it){
            GameUiState.Start -> {
                LaunchedEffect(it){
                    viewModel.initializeGame(
                        humanPlayerMark = humanPlayerMark,
                        startingPlayerMark = startingPlayerMark
                    )
                }
            }
            else -> {
                GameScreenContent(
                    viewModel = viewModel,
                    navigation = navigation
                )
            }
        }
    }
}


@Composable
fun GameScreenContent(
    viewModel: GameViewModel,
    navigation: INavigationRouter
){
    val uiState = viewModel.uiState.collectAsState()
    val tileBoard = viewModel.boardTiles.collectAsState()

    val topText = when{
        uiState.value is GameUiState.HumanPlayerTurn -> stringResource(id = R.string.yourTurn)
        uiState.value is GameUiState.ComputerPlayerTurn -> stringResource(id = R.string.oponentsTurn)
        uiState.value is GameUiState.Tie -> stringResource(id = R.string.itsTie)
        uiState.value is GameUiState.HumanPlayerVictory -> stringResource(id = R.string.youHaveWon)
        uiState.value is GameUiState.ComputerPlayerVictory -> stringResource(id = R.string.youHaveLost)
        else -> ""
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = topText,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            CustomButton(
                text = stringResource(id = R.string.concede),
                backgroundColor = red,
                textColor = MaterialTheme.colorScheme.background,
                onClick = {
                    navigation.toStartupScreen()
                })
        }
    }

}