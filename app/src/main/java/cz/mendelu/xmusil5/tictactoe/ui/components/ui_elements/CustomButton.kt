package cz.mendelu.xmusil5.tictactoe.ui.components.ui_elements

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.xmusil5.tictactoe.ui.theme.disabledColor
import cz.mendelu.xmusil5.tictactoe.ui.theme.shadowColor
import cz.mendelu.xmusil5.tictactoe.ui.utils.UiConstants
import cz.mendelu.xmusil5.tictactoe.ui.utils.customShadowPercentage

@Composable
fun CustomButton(
    text: String,
    iconId: Int? = null,
    tintIcon: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    textColor: Color = MaterialTheme.colorScheme.onSecondary,
    textSize: TextUnit = 17.sp,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
){
    val animationDuration = 150
    val cornerRadius = UiConstants.RADIUS_CAPSULE_PERCENTAGE
    val displayedBackgroundColor by animateColorAsState(
        targetValue = if (enabled) backgroundColor else disabledColor,
        animationSpec = tween(animationDuration)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .customShadowPercentage(
                color = shadowColor,
                borderRadiusPercentage = cornerRadius,
                spread = 0.dp,
                blurRadius = 5.dp,
                offsetY = 2.dp
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(displayedBackgroundColor)
            .clickable{
                if (enabled){
                    onClick()
                }
            }
            .padding(vertical = 10.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
        ) {
            iconId?.let {
                if (tintIcon) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it),
                        contentDescription = null,
                        tint = textColor,
                        modifier = Modifier
                            .size((textSize.value * 1.5).dp)
                            .padding(end = 5.dp)
                    )
                } else {
                    Image(
                        imageVector = ImageVector.vectorResource(id = it),
                        contentDescription = null,
                        modifier = Modifier
                            .size((textSize.value * 1.5).dp)
                            .padding(end = 5.dp)
                    )
                }
            }
            if (text.isNotBlank()) {
                Text(
                    text = text,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    lineHeight = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = textSize,
                )
            }
        }
    }
}