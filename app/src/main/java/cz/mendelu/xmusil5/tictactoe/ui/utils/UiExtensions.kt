package cz.mendelu.xmusil5.tictactoe.ui.utils

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Shadow modifier for any composable object
fun Modifier.customShadow(
    color: Color = Color.Black,
    borderRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = borderRadius.toPx(),
                radiusY = borderRadius.toPx(),
                paint
            )
        }
    }
)

// Shadow modifier for any composable object
fun Modifier.customShadowPercentage(
    color: Color = Color.Black,
    borderRadiusPercentage: Int,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = Math.min(size.width, size.height) * (borderRadiusPercentage/100f),
                radiusY = Math.min(size.width, size.height) * (borderRadiusPercentage/100f),
                paint
            )
        }
    }
)

fun Modifier.fadeEdges(
    edges: Edges,
    backgroundColor: Color,
    fadeWidth: Float = 70f
) = this.then(
    graphicsLayer {
        alpha = 0.99f
    }.then(
        drawWithContent {
            drawContent()
            val colorsStart = listOf(Color.Transparent, backgroundColor)
            val colorsEnd = listOf(backgroundColor, Color.Transparent)
            if (edges == Edges.VERTICAL || edges == Edges.ALL){
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = colorsStart,
                        startY = 0f,
                        endY = fadeWidth
                    ),
                    blendMode = BlendMode.DstIn
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = colorsEnd,
                        startY = size.height - fadeWidth,
                        endY = size.height
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
            if (edges == Edges.HORIZONTAL || edges == Edges.ALL){
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = colorsStart,
                        startX = 0f,
                        endX = fadeWidth
                    ),
                    blendMode = BlendMode.DstIn
                )
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = colorsEnd,
                        startX = size.width - fadeWidth,
                        endX = size.width
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
        }
    )
)