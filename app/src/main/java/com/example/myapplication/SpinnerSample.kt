package com.example.myapplication

import android.graphics.Paint
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class WheelSegment(val text: String, val backgroundBrush: Color, val textColor: Color)

@Composable
fun SpinWheel(
    modifier: Modifier = Modifier,
    items: List<WheelSegment>,
    rotation: Float,
    duration: Int,
    onSegmentSelected: (String) -> Unit // Callback to handle the selected segment
) {
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(durationMillis = duration, easing = EaseOutCirc),
        label = "Spin Wheel rotation value"
    )

    val segmentAngleSize = 360f / items.size

    // Calculate the angle where the arrow points
    val selectedAngle = (animatedRotation % 360 + 360) % 360 // Normalize angle
    val selectedSegmentIndex = ((selectedAngle + segmentAngleSize / 2) % 360) / segmentAngleSize

    // Call the onSegmentSelected callback to notify which segment is selected
    onSegmentSelected(items[selectedSegmentIndex.toInt()].text)

    Canvas(modifier = modifier) {
        rotate(animatedRotation) {
            items.forEachIndexed { index, item ->
                rotate(segmentAngleSize * index) {
                    drawArc(
                        color = item.backgroundBrush,
                        startAngle = -90f,
                        sweepAngle = segmentAngleSize,
                        useCenter = true
                    )
                }
            }

            items.forEachIndexed { index, item ->
                rotate(segmentAngleSize * index + segmentAngleSize / 2) {
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            item.text,
                            size.width / 2,
                            size.height / 12,
                            Paint().apply {
                                color = item.textColor.toArgb()
                                textAlign = Paint.Align.CENTER
                                textSize = 32f
                                isAntiAlias = true
                            }
                        )
                    }
                }
            }

            // Draw the outer circle
            drawCircle(
                color = Color.Black,
                radius = size.width / 2,
                style = Stroke(4.dp.toPx())
            )
        }

        // Pointer Arrow
        val path = Path().apply {
            moveTo(size.width / 2 - 20f, -10f)
            lineTo(size.width / 2, 80f)
            lineTo(size.width / 2 + 20f, -10f)
            close()
        }
        drawPath(path, color = Color.DarkGray)
    }
}





//@Composable
//fun SpinWheelWithButton() {
//    val items = listOf(
//        WheelSegment("Item 1", Color.Red, Color.White),
//        WheelSegment("Item 2", Color.Green, Color.White),
//        WheelSegment("Item 3", Color.Blue, Color.White),
//        WheelSegment("Item 4", Color.Yellow, Color.White),
//        WheelSegment("Item 5", Color.Cyan, Color.White)
//    )
//
//    // State to manage the winner index and rotation
//    var winnerIndex by remember { mutableStateOf<Int?>(null) }
//    var spinning by remember { mutableStateOf(false) }
//
//    // Function to start the spin
//    fun startSpin() {
//        spinning = true
//        // Randomly pick the winner index (simulate a random spin)
//        winnerIndex = (items.indices).random()
//
//        // Log the winner index after the spin stops (delay here to simulate the stopping)
//        kotlinx.coroutines.GlobalScope.launch {
//            delay(5000) // The duration of the spin
//            spinning = false
//            Log.d("SpinWheel", "Wheel stopped on: ${items[winnerIndex!!].text}")
//        }
//    }
//
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        // Button to start the spin
//        Button(onClick = { startSpin() }) {
//            Text("Spin Wheel")
//        }
//
//        // Spin wheel composable
//        SpinWheel(
//            modifier = Modifier.size(300.dp),
//            items = items,
//            winnerIndex = winnerIndex,
//            spinDuration = 5000,
//            spinAction = { startSpin() }
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    SpinWheelWithButton()
//}
