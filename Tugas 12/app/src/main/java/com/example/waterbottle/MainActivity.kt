 package com.example.waterbottle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterBottelTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WaterTrackerScreen()
                }
            }
        }
    }
}

@Composable
fun WaterBottelTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF279EFF),
            background = Color(0xFFF5F5F5),
            surface = Color.White
        ),
        content = content
    )
}

@Composable
fun WaterTrackerScreen() {
    var usedWaterAmount by remember { mutableStateOf(500) }
    val totalWaterAmount = remember { 2500 }
    val waterPercentage = (usedWaterAmount.toFloat() / totalWaterAmount.toFloat()).coerceIn(0f, 1f)

    // Animated background gradient based on water level
    val backgroundAlpha by animateFloatAsState(
        targetValue = waterPercentage * 0.3f,
        animationSpec = tween(1000),
        label = "background_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB).copy(alpha = backgroundAlpha),
                        Color(0xFF4682B4).copy(alpha = backgroundAlpha * 0.5f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            // Header Section
            HeaderSection(
                usedWaterAmount = usedWaterAmount,
                totalWaterAmount = totalWaterAmount,
                waterPercentage = waterPercentage
            )

            // Water Bottle
            WaterBottle(
                totalWaterAmount = totalWaterAmount,
                unit = "ml",
                usedWaterAmount = usedWaterAmount
            )

            // Control Section
            ControlSection(
                usedWaterAmount = usedWaterAmount,
                totalWaterAmount = totalWaterAmount,
                onWaterAmountChange = { newAmount ->
                    usedWaterAmount = newAmount.coerceIn(0, totalWaterAmount)
                }
            )
        }
    }
}

@Composable
fun HeaderSection(
    usedWaterAmount: Int,
    totalWaterAmount: Int,
    waterPercentage: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "💧 Water Tracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0066CC)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress indicator
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Daily Progress",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { waterPercentage },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF279EFF),
                    trackColor = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(waterPercentage * 100).toInt()}% Complete",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun WaterBottle(
    totalWaterAmount: Int,
    unit: String,
    usedWaterAmount: Int
) {
    val waterPercentage = (usedWaterAmount.toFloat() / totalWaterAmount.toFloat()).coerceIn(0f, 1f)

    val animatedWaterLevel by animateFloatAsState(
        targetValue = waterPercentage,
        animationSpec = tween(1000),
        label = "water_level"
    )

    Box(
        modifier = Modifier
            .size(200.dp, 300.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawWaterBottle(
                waterLevel = animatedWaterLevel,
                usedAmount = usedWaterAmount,
                totalAmount = totalWaterAmount,
                unit = unit
            )
        }
    }
}

fun DrawScope.drawWaterBottle(
    waterLevel: Float,
    usedAmount: Int,
    totalAmount: Int,
    unit: String
) {
    val bottleWidth = size.width * 0.6f
    val bottleHeight = size.height * 0.8f
    val bottleX = (size.width - bottleWidth) / 2
    val bottleY = (size.height - bottleHeight) / 2

    // Draw bottle outline
    drawRoundRect(
        color = Color(0xFF0066CC),
        topLeft = Offset(bottleX, bottleY),
        size = Size(bottleWidth, bottleHeight),
        cornerRadius = CornerRadius(20f, 20f),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
    )

    // Draw bottle cap
    val capWidth = bottleWidth * 0.4f
    val capHeight = 30f
    val capX = bottleX + (bottleWidth - capWidth) / 2
    val capY = bottleY - capHeight + 5f

    drawRoundRect(
        color = Color(0xFF0066CC),
        topLeft = Offset(capX, capY),
        size = Size(capWidth, capHeight),
        cornerRadius = CornerRadius(10f, 10f)
    )

    // Draw water inside bottle
    if (waterLevel > 0f) {
        val waterHeight = bottleHeight * waterLevel
        val waterY = bottleY + bottleHeight - waterHeight

        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF4FB3D9),
                    Color(0xFF279EFF)
                )
            ),
            topLeft = Offset(bottleX + 4f, waterY),
            size = Size(bottleWidth - 8f, waterHeight - 4f),
            cornerRadius = CornerRadius(16f, 16f)
        )
    }
}

@Composable
fun ControlSection(
    usedWaterAmount: Int,
    totalWaterAmount: Int,
    onWaterAmountChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$usedWaterAmount ml / $totalWaterAmount ml",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quick add buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickActionButton(
                    text = "+100ml",
                    onClick = { onWaterAmountChange(usedWaterAmount + 100) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    text = "+250ml",
                    onClick = { onWaterAmountChange(usedWaterAmount + 250) },
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    text = "+500ml",
                    onClick = { onWaterAmountChange(usedWaterAmount + 500) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Manual control
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { onWaterAmountChange(usedWaterAmount - 50) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color(0xFFFF6B6B),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Text(
                        text = "-",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Manual Adjust",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = { onWaterAmountChange(usedWaterAmount + 50) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color(0xFF4CAF50),
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add water",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reset button
            OutlinedButton(
                onClick = { onWaterAmountChange(0) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF666666)
                )
            ) {
                Text("Reset Day")
            }
        }
    }
}

@Composable
fun QuickActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF279EFF)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}