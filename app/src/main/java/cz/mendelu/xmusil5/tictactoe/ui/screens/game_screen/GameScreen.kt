package cz.mendelu.xmusil5.tictactoe.ui.screens.game_screen

import android.view.Gravity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.game.board.LineOrientation
import cz.mendelu.xmusil5.tictactoe.game.board.VictoryPath
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import cz.mendelu.xmusil5.tictactoe.ui.components.game_elements.Tile
import cz.mendelu.xmusil5.tictactoe.ui.components.ui_elements.CustomButton
import cz.mendelu.xmusil5.tictactoe.ui.components.ui_elements.GradientText
import cz.mendelu.xmusil5.tictactoe.ui.theme.onRed
import cz.mendelu.xmusil5.tictactoe.ui.theme.red
import cz.mendelu.xmusil5.tictactoe.ui.theme.shadowColor
import cz.mendelu.xmusil5.tictactoe.ui.utils.UiConstants
import cz.mendelu.xmusil5.tictactoe.ui.utils.customShadow

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
    val humanPlayerMark = viewModel.humanPlayerMark.value

    var topText by remember{
        mutableStateOf("")
    }
    var currentTurnMark by remember {
        mutableStateOf<PlayerMark?>(null)
    }
    val humanTurnText = stringResource(id = R.string.yourTurn)
    val computerTurnText = stringResource(id = R.string.oponentsTurn)
    LaunchedEffect(uiState.value){
        uiState.value.let {
            when(it) {
                is GameUiState.HumanPlayerTurn -> {
                    topText = humanTurnText
                    currentTurnMark = humanPlayerMark
                }
                is GameUiState.ComputerPlayerTurn -> {
                    topText = computerTurnText
                    currentTurnMark = PlayerMark.values().firstOrNull {
                        it != humanPlayerMark
                    }
                }
                else -> {
                    topText = ""
                    currentTurnMark = null
                }
            }
        }
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
            GradientText(
                text = topText,
                textSize = 30.sp
            )
            currentTurnMark?.let {
                Icon(
                    imageVector = ImageVector.vectorResource(id = it.iconId),
                    contentDescription = null,
                    tint = it.color,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(50.dp))
        
        GameBoard(
            viewModel = viewModel
        )

        GameEndPopup(
            navigation = navigation,
            viewModel = viewModel
        )

        Spacer(modifier = Modifier.weight(1f))

        CustomButton(
            text = stringResource(id = R.string.concede),
            backgroundColor = red,
            iconId = R.drawable.ic_close,
            textColor = onRed,
            onClick = {
                navigation.toStartupScreen()
            }
        )

        Spacer(modifier = Modifier.height(40.dp))
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
                        var lineToDraw by remember{
                            mutableStateOf<LineOrientation?>(null)
                        }
                        LaunchedEffect(victoryPath){
                            victoryPath?.let {
                                if (it.pathIndexes.contains(index)){
                                    lineToDraw = it.lineOrientation
                                }
                            }
                        }

                        Tile(
                            containedMark = tile,
                            enabled = playerInputEnabled && tileIsEmpty,
                            size = sizeOfTile,
                            lineToDraw = lineToDraw,
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


@Composable
fun GameEndPopup(
    navigation: INavigationRouter,
    viewModel: GameViewModel
){
    val uiState = viewModel.uiState.collectAsState()

    var visible by rememberSaveable {
        mutableStateOf(false)
    }
    var mainText by rememberSaveable {
        mutableStateOf("")
    }

    val victoryText = stringResource(id = R.string.youHaveWon)
    val lossText = stringResource(id = R.string.youHaveLost)
    val tieText = stringResource(id = R.string.itsTie)
    LaunchedEffect(uiState.value){
        uiState.value.let {
            when(it){
                is GameUiState.ComputerPlayerVictory -> {
                    visible = true
                    mainText = lossText
                }
                is GameUiState.HumanPlayerVictory -> {
                    visible = true
                    mainText = victoryText
                }
                is GameUiState.Tie -> {
                    visible = true
                    mainText = tieText
                }
                else -> {
                    visible = false
                    mainText = ""
                }
            }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = UiConstants.ANIMATION_DURATION_SHORT)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = UiConstants.ANIMATION_DURATION_SHORT)
        )
    ) {
        val cornerRadius = UiConstants.RADIUS_SMALL
        Dialog(
            onDismissRequest = {
                // Dialog is not cancellable
            }
        ) {
            // setting gravity of the dialog to the bottom of the screen
            val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
            dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(cornerRadius))
                        .customShadow(
                            color = shadowColor,
                            borderRadius = cornerRadius,
                            spread = 0.dp,
                            blurRadius = 5.dp,
                            offsetY = 2.dp
                        )
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    GradientText(
                        text = mainText,
                        textSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(90.dp)
                    ){
                        GameEndAnimation(
                            viewModel = viewModel
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomButton(
                        text = stringResource(id = R.string.playAgain),
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        iconId = R.drawable.ic_play,
                        textColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            viewModel.uiState.value = GameUiState.Start()
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    CustomButton(
                        text = stringResource(id = R.string.backToMenu),
                        backgroundColor = MaterialTheme.colorScheme.primary,
                        iconId = R.drawable.ic_menu,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            visible = false
                            navigation.toStartupScreen()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }

        }
    }
}


@Composable
fun GameEndAnimation(
    viewModel: GameViewModel
){
    val uiState = viewModel.uiState.collectAsState()
    var show by remember {
        mutableStateOf(false)
    }
    var compositionId by remember {
        mutableStateOf<Int?>(null)
    }

    LaunchedEffect(uiState.value) {
        uiState.value.let {
            when (it) {
                is GameUiState.ComputerPlayerVictory -> {
                    show = true
                    compositionId = R.raw.lost
                }
                is GameUiState.HumanPlayerVictory -> {
                    show = true
                    compositionId = R.raw.trophy
                }
                is GameUiState.Tie -> {
                    show = true
                    compositionId = R.raw.retry
                }
            }
        }
    }
    compositionId?.let {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(it)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = 1
        )
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}