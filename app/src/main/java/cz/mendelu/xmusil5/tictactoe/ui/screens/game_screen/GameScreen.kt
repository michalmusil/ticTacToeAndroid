package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.game.Game
import cz.mendelu.xmusil5.tictactoe.game.board.VictoryPath
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import cz.mendelu.xmusil5.tictactoe.ui.components.game_elements.Tile
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
            is GameUiState.Start -> {
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
                }
            )
        }
        
        Spacer(modifier = Modifier.height(50.dp))
        
        GameBoard(viewModel = viewModel)
    }
}

@Composable
fun GameBoard(
    viewModel: GameViewModel
){
    var boardSize: IntSize? by remember {
        mutableStateOf(IntSize.Zero)
    }

    val uiState = viewModel.uiState.collectAsState()
    val boardTiles = viewModel.boardTiles.collectAsState()

    val playerInputEnabled = when(uiState.value){
        is GameUiState.HumanPlayerTurn -> true
        else -> false
    }

    var victoryPath by remember{
        mutableStateOf<VictoryPath?>(null)
    }
    uiState.value.let {
        LaunchedEffect(uiState.value){
            when(it){
                is GameUiState.HumanPlayerVictory -> {
                    victoryPath = it.victoryPath
                }
                is GameUiState.ComputerPlayerVictory -> {
                    victoryPath = it.victoryPath
                }
                else -> {
                    // do nothing
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .onSizeChanged {
                boardSize = it
                boardSize!!.height
            }
            .fillMaxWidth()
            .aspectRatio(1f)
    ){
        boardSize?.let {
            val numberOfVerticalTiles = viewModel.getBoardDimensions().first
            val sizeOfTile = with(LocalDensity.current){
                (it.width / numberOfVerticalTiles).toDp()
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(numberOfVerticalTiles),
                userScrollEnabled = false,
                content = {
                    items(boardTiles.value.size) { index ->
                        val tile = boardTiles.value.get(index)
                        val tileIsEmpty = tile == null
                        Tile(
                            containedMark = tile,
                            enabled = playerInputEnabled && tileIsEmpty,
                            size = sizeOfTile,
                            onClick = {
                                viewModel.playAtIndex(index)
                            }
                        )
                    }
                }
            )
        }
    }
}