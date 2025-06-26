package com.example.waterbottel

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun WatterBottle(
    modifier: Modifier = Modifier,
    totalWaterAmount: Int,
    unit: String,
    usedWaterAmount: Int,
    waterWavesColor: Color = Color(0xff279EFF),
    bottleColor: Color = Color.White,
    capColor: Color = Color(0xFF0065B9)
) {
    val waterPercentage = animateFloatAsState(
        targetValue = (usedWaterAmount.toFloat() / totalWaterAmount.toFloat()).coerceIn(0f, 1f),
        label = "Water Waves animation",
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
    ).value

    val usedWaterAmountAnimation = animateIntAsState(
        targetValue = usedWaterAmount,
        label = "Used water amount animation",
        animationSpec = tween(durationMillis = 1000)
    ).value

    // Wave animation for more realistic water effect
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )

    // Bubble animation
    val bubbleOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "bubble_offset"
    )

    Box(
        modifier = modifier
            .width(200.dp)
            .height(600.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val capWidth = size.width * 0.55f
            val capHeight = size.height * 0.13f

            // Draw bottle shadow
            drawBottleShadow(size)

            // Draw the bottle body
            val bodyPath = createBottlePath(size)

            clipPath(path = bodyPath) {
                // Draw bottle background with gradient
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            bottleColor,
                            bottleColor.copy(alpha = 0.9f)
                        )
                    ),
                    size = size,
                    topLeft = Offset(0f, 0f)
                )

                // Draw water with animated waves
                drawAnimatedWater(
                    waterPercentage = waterPercentage,
                    waveOffset = waveOffset,
                    waterColor = waterWavesColor,
                    size = size
                )

                // Draw bubbles if there's water
                if (waterPercentage > 0.1f) {
                    drawBubbles(
                        waterPercentage = waterPercentage,
                        bubbleOffset = bubbleOffset,
                        size = size
                    )
                }
            }

            // Draw bottle outline
            drawPath(
                path = bodyPath,
                color = Color.Gray.copy(alpha = 0.3f),
                style = Stroke(width = 2.dp.toPx())
            )

            // Draw water level indicators
            drawWaterLevelIndicators(size, waterPercentage)

            // Draw the bottle cap with gradient
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        capColor.copy(alpha = 1f),
                        capColor.copy(alpha = 0.8f)
                    )
                ),
                size = Size(capWidth, capHeight),
                topLeft = Offset(size.width / 2 - capWidth / 2f, 0f),
                cornerRadius = CornerRadius(45f, 45f)
            )

            // Draw cap highlight
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                ),
                size = Size(capWidth * 0.8f, capHeight * 0.4f),
                topLeft = Offset(
                    size.width / 2 - (capWidth * 0.8f) / 2f,
                    capHeight * 0.1f
                ),
                cornerRadius = CornerRadius(20f, 20f)
            )
        }

        // Water amount text with enhanced styling
        val text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = if (waterPercentage > 0.5f) bottleColor else waterWavesColor,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(usedWaterAmountAnimation.toString())
            }
            withStyle(
                style = SpanStyle(
                    color = if (waterPercentage > 0.5f) bottleColor else waterWavesColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append(" ")
                append(unit)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text)
        }
    }
}

private fun createBottlePath(size: Size): Path {
    return Path().apply {
        val width = size.width
        val height = size.height

        moveTo(width * 0.3f, height * 0.1f)
        lineTo(width * 0.3f, height * 0.2f)
        quadraticBezierTo(
            0f, height * 0.3f,
            0f, height * 0.4f
        )
        lineTo(0f, height * 0.95f)
        quadraticBezierTo(
            0f, height,
            width * 0.05f, height
        )
        lineTo(width * 0.95f, height)
        quadraticBezierTo(
            width, height,
            width, height * 0.95f
        )
        lineTo(width, height * 0.4f)
        quadraticBezierTo(
            width, height * 0.3f,
            width * 0.7f, height * 0.2f
        )
        lineTo(width * 0.7f, height * 0.1f)
        close()
    }
}

private fun DrawScope.drawBottleShadow(size: Size) {
    val shadowPath = Path().apply {
        val width = size.width
        val height = size.height

        moveTo(width * 0.32f, height * 0.12f)
        lineTo(width * 0.32f, height * 0.22f)
        quadraticBezierTo(
            width * 0.02f, height * 0.32f,
            width * 0.02f, height * 0.42f
        )
        lineTo(width * 0.02f, height * 0.97f)
        quadraticBezierTo(
            width * 0.02f, height * 1.02f,
            width * 0.07f, height * 1.02f
        )
        lineTo(width * 0.97f, height * 1.02f)
        quadraticBezierTo(
            width * 1.02f, height * 1.02f,
            width * 1.02f, height * 0.97f
        )
        lineTo(width * 1.02f, height * 0.42f)
        quadraticBezierTo(
            width * 1.02f, height * 0.32f,
            width * 0.72f, height * 0.22f
        )
        lineTo(width * 0.72f, height * 0.12f)
        close()
    }

    drawPath(
        path = shadowPath,
        color = Color.Black.copy(alpha = 0.1f)
    )
}

private fun DrawScope.drawAnimatedWater(
    waterPercentage: Float,
    waveOffset: Float,
    waterColor: Color,
    size: Size
) {
    val waterWavesYPosition = (1 - waterPercentage) * size.height

    val wavesPath = Path().apply {
        moveTo(0f, waterWavesYPosition)

        // Create wave effect
        val waveHeight = 15f
        val waveLength = size.width / 3f

        for (x in 0..size.width.toInt() step 1) {
            val waveY = waterWavesYPosition +
                    waveHeight * sin((x / waveLength) * 2 * PI + waveOffset).toFloat()

            if (x == 0) {
                moveTo(x.toFloat(), waveY)
            } else {
                lineTo(x.toFloat(), waveY)
            }
        }

        lineTo(size.width, size.height)
        lineTo(0f, size.height)
        close()
    }

    drawPath(
        path = wavesPath,
        brush = Brush.verticalGradient(
            colors = listOf(
                waterColor.copy(alpha = 0.8f),
                waterColor.copy(alpha = 1f)
            )
        )
    )
}

private fun DrawScope.drawBubbles(
    waterPercentage: Float,
    bubbleOffset: Float,
    size: Size
) {
    val bubbleColor = Color.White.copy(alpha = 0.6f)
    val waterTop = (1 - waterPercentage) * size.height
    val waterHeight = size.height - waterTop

    // Draw several bubbles at different positions
    for (i in 0..4) {
        val bubbleX = size.width * (0.2f + i * 0.15f)
        val bubbleY = waterTop + waterHeight * ((bubbleOffset + i * 0.2f) % 1f)
        val bubbleRadius = (3f + i * 2f) * (1f - (bubbleOffset + i * 0.2f) % 1f)

        if (bubbleY < size.height - 20f) {
            drawCircle(
                color = bubbleColor,
                radius = bubbleRadius,
                center = Offset(bubbleX, bubbleY)
            )
        }
    }
}

private fun DrawScope.drawWaterLevelIndicators(size: Size, waterPercentage: Float) {
    val indicatorColor = Color.Gray.copy(alpha = 0.3f)
    val levels = listOf(0.25f, 0.5f, 0.75f, 1f)

    levels.forEach { level ->
        val y = size.height * (1 - level)
        val alpha = if (waterPercentage >= level) 0.6f else 0.2f

        drawLine(
            color = indicatorColor.copy(alpha = alpha),
            start = Offset(size.width * 0.75f, y),
            end = Offset(size.width * 0.85f, y),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@Preview
@Composable
fun EnhancedWaterBottlePreview() {
    WatterBottle(
        totalWaterAmount = 2500,
        unit = "ml",
        usedWaterAmount = 1200
    )
}