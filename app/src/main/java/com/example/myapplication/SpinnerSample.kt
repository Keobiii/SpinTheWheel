package com.example.myapplication

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
    outlineSizeDp: Dp = 4.dp,
    outlineColor: Color = Color.Black,
    backgroundColor: Color = Color.Transparent,
    minimumFullRotations: Int = 10,
    winnerIndex: Int? = null,
    spinDuration: Int = 5000,
    spinAction: (() -> Unit)? = null
) {
    require(items.isNotEmpty()) { "Require at least one item!" }

    val numberOfItems by remember { mutableStateOf(items.size) }
    val segmentAngleSize by remember { mutableStateOf(360f / numberOfItems) }
    val middleOfSegmentAngleSize by remember { mutableStateOf(segmentAngleSize / 2) }
    val borderSize = with(LocalDensity.current) { outlineSizeDp.toPx() }
    val numberOfPreSpinsAngle by remember { mutableStateOf(minimumFullRotations * 360) }
    val textMeasurer = rememberTextMeasurer()

    val winnerFullRotateAngle by remember(winnerIndex) { mutableStateOf(if (winnerIndex != null) (numberOfPreSpinsAngle + (segmentAngleSize * (items.size - (winnerIndex + 1))) + middleOfSegmentAngleSize) else 0f) }

    val rotationAngle by animateFloatAsState(
        targetValue = winnerFullRotateAngle,
        animationSpec = tween(spinDuration, easing = EaseOutCirc),
        label = "Spin Wheel rotation value"
    )

    Canvas(modifier = modifier) {
        rotate(rotationAngle) {
            drawCircle(color = backgroundColor, radius = size.width / 2 - borderSize)
            items.forEachIndexed { index, s ->
                rotate(segmentAngleSize * index) {
                    drawArc(
                        color = s.backgroundBrush,  // Convert Color to Brush here
                        -90f,
                        segmentAngleSize,
                        true,
                        topLeft = Offset(-borderSize, borderSize)
                    )
                }
            }
            items.forEachIndexed { index, s ->
                rotate(segmentAngleSize * index) {
                    drawLine(
                        color = outlineColor,
                        start = Offset(size.width / 2, size.height / 2),
                        end = Offset(size.width / 2, 0f + borderSize),
                        strokeWidth = borderSize
                    )
                }
                rotate(segmentAngleSize * index + middleOfSegmentAngleSize) {
                    drawText(
                        textMeasurer,
                        s.text,
                        topLeft = Offset(
                            (size.width / 2) - ((size.width / 4) / items.size),
                            (size.height / 2) / 4
                        ),
                        style = TextStyle(
                            color = s.textColor,
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(0f, 0f),
                                blurRadius = 8f,
                            ),
                            fontSize = ((size.width / 4) / items.size).sp,
                        )
                    )
                }
            }
            drawCircle(
                color = outlineColor,
                radius = size.width / 2 - borderSize / 2,
                style = Stroke(borderSize)
            )
        }
        val path = Path().apply {
            moveTo(size.width / 2 - 20f, -10f)
            lineTo(size.width / 2, 80f)
            lineTo(size.width / 2 + 20f, -10f)
            close()
        }
        drawPath(path, color = Color.Red) // TODO: Add pointer arrow
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

