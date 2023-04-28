package cz.mendelu.xmusil5.tictactoe.ui.screens.splash_screen

import android.Manifest
import android.os.Build
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cz.mendelu.xmusil5.tictactoe.R
import cz.mendelu.xmusil5.tictactoe.navigation.INavigationRouter
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigation: INavigationRouter,
    screenDuration: Long = 2000,
){
    val targetLogoSize = remember {
        220f
    }
    val currentLogoSize = remember {
        Animatable(0f)
    }

    LaunchedEffect(true){
        currentLogoSize.animateTo(
            targetValue = targetLogoSize,
            animationSpec = tween(
                durationMillis = (screenDuration/2).toInt(),
                easing = {
                    OvershootInterpolator(1.7f).getInterpolation(it)
                }
            )
        )
    }
    LaunchedEffect(true){
        delay(screenDuration)
        navigation.toStartupScreen()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(currentLogoSize.value.dp)
        )
    }
}