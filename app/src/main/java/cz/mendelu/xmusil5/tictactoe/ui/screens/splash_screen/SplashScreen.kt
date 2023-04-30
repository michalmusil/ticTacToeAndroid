package cz.mendelu.xmusil5.tictactoe.ui.screens.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigation: INavigationRouter,
    maxScreenDuration: Long = 3000,
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
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
    if (progress >= 1.0f){
        isPlaying.value = false
    }

}