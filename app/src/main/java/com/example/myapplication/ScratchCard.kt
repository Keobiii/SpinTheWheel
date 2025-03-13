package com.example.myapplication

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.pageBackground

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScratchCard(
    overlayImage: ImageBitmap,
    baseImage: ImageBitmap,
    modifier: Modifier = Modifier,
    movedOffset: Offset?,
    onMovedoffset: (Float, Float) -> Unit,
    currentPath: Path,
    currentPathThickness: Float,
    index: Int, // Add the index for the card
    revealedList: MutableList<Boolean>, // Add the revealedList as a parameter
    onRevealChanged: (Int, Boolean) -> Unit, // Add a callback to notify when revealed state changes
    scratchPoints: MutableList<Offset> // Track points
) {

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clipToBounds()
            .padding(5.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        currentPath.moveTo(it.x, it.y)
                        scratchPoints.clear() // Start fresh on a new touch
                        scratchPoints.add(Offset(it.x, it.y))
                    }
                    MotionEvent.ACTION_MOVE -> {
                        onMovedoffset(it.x, it.y)
                        scratchPoints.add(Offset(it.x, it.y)) // Add the new point to the list
                    }
                }
                true
            },
    ) {

        val canvasWidth = size.width.toInt()
        val canvasHeight = size.height.toInt()
        val imageSize = IntSize(canvasWidth, canvasHeight)

        drawImage(
            image = overlayImage,
            dstSize = imageSize
        )

        movedOffset?.let {
            currentPath.addOval(oval = Rect(it, currentPathThickness))
        }

        clipPath(
            path = currentPath,
            clipOp = ClipOp.Intersect
        ) {
            drawImage(
                image = baseImage,
                dstSize = imageSize
            )
        }


        if (isCardRevealed(scratchPoints)) {
            if (!revealedList[index]) {
                revealedList[index] = true
                onRevealChanged(index, true)
            }
        }
    }
}

fun isCardRevealed(scratchPoints: MutableList<Offset>): Boolean {
    val threshold = 10

    return scratchPoints.size >= threshold
}









