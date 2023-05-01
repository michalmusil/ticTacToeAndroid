package cz.mendelu.xmusil5.tictactoe.ui.screens.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import cz.mendelu.xmusil5.tictactoe.ui.theme.oColor
import cz.mendelu.xmusil5.tictactoe.ui.theme.xColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigation: INavigationRouter,
    maxScreenDuration: Long = 4000,
){
    val animationIsPlaying = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(animationIsPlaying.value){
        if (!animationIsPlaying.value){
            navigation.toStartupScreen()
        }
    }
    LaunchedEffect(true){
        // A failsafe if the animation has an unexpected error
        delay(maxScreenDuration)
        if (animationIsPlaying.value){
            navigation.toStartupScreen()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SplashAnimation(
                isPlaying = animationIsPlaying
            )
        }
    }
}

@Composable
fun SplashAnimation(
    isPlaying: MutableState<Boolean>
){

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.opening)
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        speed = 1.5f
    )
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            keyPath = arrayOf(
                "Livello forma 1",
                "Ellisse 1",
                "Traccia 1"
            ),
            value = oColor.toArgb()
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            keyPath = arrayOf(
                "Livello forma 2",
                "Forma 1",
                "Traccia 1"
            ),
            value = xColor.toArgb(),
        ),
        rememberLottieDynamicProperty(
            property = LottieProperty.STROKE_COLOR,
            keyPath = arrayOf(
                "Livello forma 3",
                "Forma 1",
                "Traccia 1"
                ),
            value = xColor.toArgb(),
        )
    )


    LottieAnimation(
        composition = composition,
        progress = { progress },
        dynamicProperties = dynamicProperties
    )
    if (progress >= 1.0f){
        isPlaying.value = false
    }

}