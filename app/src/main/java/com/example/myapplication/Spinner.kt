package com.example.myapplication

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun Spinner() {
    val items = listOf(
        WheelSegment("0", Color.Green, Color.White),
        WheelSegment("32", Color.Red, Color.White),
        WheelSegment("15", Color.Black, Color.White),
        WheelSegment("19", Color.Red, Color.White),
        WheelSegment("4", Color.Black, Color.White),
        WheelSegment("21", Color.Red, Color.White),
        WheelSegment("2", Color.Black, Color.White),
        WheelSegment("25", Color.Red, Color.White),
        WheelSegment("17", Color.Black, Color.White),
        WheelSegment("34", Color.Red, Color.White),
        WheelSegment("6", Color.Black, Color.White),
        WheelSegment("27", Color.Red, Color.White),
        WheelSegment("13", Color.Black, Color.White),
        WheelSegment("36", Color.Red, Color.White),
        WheelSegment("11", Color.Black, Color.White),
        WheelSegment("30", Color.Red, Color.White),
        WheelSegment("8", Color.Black, Color.White),
        WheelSegment("23", Color.Red, Color.White),
        WheelSegment("10", Color.Black, Color.White),
        WheelSegment("5", Color.Red, Color.White),
        WheelSegment("24", Color.Black, Color.White),
        WheelSegment("16", Color.Red, Color.White),
        WheelSegment("33", Color.Black, Color.White),
        WheelSegment("1", Color.Red, Color.White),
        WheelSegment("20", Color.Black, Color.White),
        WheelSegment("14", Color.Red, Color.White),
        WheelSegment("31", Color.Black, Color.White),
        WheelSegment("9", Color.Red, Color.White),
        WheelSegment("22", Color.Black, Color.White),
        WheelSegment("18", Color.Red, Color.White),
        WheelSegment("29", Color.Black, Color.White),
        WheelSegment("7", Color.Red, Color.White),
        WheelSegment("28", Color.Black, Color.White),
        WheelSegment("12", Color.Red, Color.White),
        WheelSegment("35", Color.Black, Color.White),
        WheelSegment("3", Color.Red, Color.White),
        WheelSegment("26", Color.Black, Color.White)
    )


    var rotation by remember { mutableStateOf(0f) }
    var selectedIndex by remember { mutableStateOf(-1) }

    fun startSpin() {
        val randomSpin = (360 * 5) + (0..359).random().toFloat()
        rotation += randomSpin
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            startSpin()
        }) {
            Text("Spin Wheel")
        }

        SpinWheel(
            modifier = Modifier.size(300.dp),
            items = items,
            rotation = rotation,
            duration = 5000,
            onSegmentSelected = { selectedText ->
                // Handle the selected segment
                Log.i("Selected Segment: ", selectedText)
            }
        )

        // Display the selected number
        Text(
            text = "Selected: ${if (selectedIndex >= 0) items[selectedIndex].text else "None"}",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

fun getCustomRandomImages(image1: Int, image2: Int, count1: Int, count2: Int): List<Int> {
    val imageList = mutableListOf<Int>()


    repeat(count1) { imageList.add(image1) }
    repeat(count2) { imageList.add(image2) }


    return imageList.shuffled()
}
