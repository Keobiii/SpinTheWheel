package com.example.myapplication.Games

import android.view.MotionEvent
import androidx.annotation.OptIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.myapplication.Dialog.CustomCardDialog
import com.example.myapplication.DraggedPath
import com.example.myapplication.R
import com.example.myapplication.ui.theme.yelloowww
import kotlin.random.Random

fun generateRandomCounts(): Pair<Int, Int> {
    val diamondCount = Random.nextInt(1, 4)
    val bombCount = 9 - diamondCount
    return Pair(bombCount, diamondCount)
}


@OptIn(UnstableApi::class)
@Composable
fun ScratchWin() {
    var showDialog by remember { mutableStateOf(false) }
    var resetTrigger by remember { mutableStateOf(0) }


    val (bombCount, diamondCount) = remember(resetTrigger) {
        generateRandomCounts()
    }


    val imageResList by remember(resetTrigger) {
        mutableStateOf(
            getCustomRandomImages(
                R.drawable.diamond,
                R.drawable.bomb,
                diamondCount,
                bombCount
            )
        )
    }


    val revealedList = remember(resetTrigger) { mutableStateListOf<Boolean>().apply { addAll(List(imageResList.size) { false }) } }
    val scratchPoints = remember(resetTrigger) { List(imageResList.size) { mutableStateListOf<Offset>() } }


    fun resetGame() {
        resetTrigger++
    }

    val totalCount = diamondCount + bombCount
    Log.i("Bomb Count: ", bombCount.toString())
    Log.i("Diamond Count: ", diamondCount.toString())
    Log.i("Total Count: ", totalCount.toString())

    // Show dialog when all items are revealed
    if (showDialog) {
        CustomCardDialog(
            onDismiss = { showDialog = false },
            fontFamily = FontFamily.SansSerif,
            title = if (bombCount > 6) "Talo" else "Panalo",
            description = if (bombCount > 6) "Nice try! Don’t give up, try again and you’ll get there!" else "You won! Want to bet again?"
        )

//        resetGame()
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .border(1.dp, Color.Red)
                .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            userScrollEnabled = false
        ) {
            items(9) { index ->
                ScracthCardScreen(
                    baseImage = imageResList[index], // Use the image list based on bomb/diamond count
                    overlayImage = R.drawable.coin,
                    index = index,
                    resetTrigger,
                    revealedList = revealedList,
                    onRevealChanged = { idx, revealed ->
                        // Check if all cards are revealed
                        if (revealed && revealedList.all { it }) {
                            // All cards revealed, perform your action
                            Log.i("Scratch", "All items revealed!")
                            showDialog = true
                        }
                    },
                    scratchPoints = scratchPoints[index]
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
                .size(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    resetGame()
                },
            colors = CardDefaults.cardColors(yelloowww)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Game",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }

}



@kotlin.OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScracthCardScreen(
    baseImage: Int,
    overlayImage: Int,
    index: Int,
    resetTrigger: Int,
    revealedList: MutableList<Boolean>,
    onRevealChanged: (Int, Boolean) -> Unit,
    scratchPoints: SnapshotStateList<Offset>
) {
    val imageee = baseImage
    val baseImage = ImageBitmap.imageResource(id = baseImage)
    val overlayImage = ImageBitmap.imageResource(id = overlayImage)

    val currentPathState = remember(resetTrigger) { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffsetState = remember(resetTrigger) { mutableStateOf<Offset?>(null) }

    val currentPath = currentPathState.value.path
    val currentPathThickness = currentPathState.value.width

//    val onMovedoffset = { x, y ->
//        movedOffsetState.value = Offset(x, y)
//    }

//    ScratchCard(
//        overlayImage = overlayImage,
//        baseImage = baseImage,
//        movedOffset = movedOffsetState.value,
//        onMovedoffset = { x, y ->
//            movedOffsetState.value = Offset(x, y)
//        },
//        currentPath = currentPathState.value.path,
//        currentPathThickness = currentPathState.value.width,
//        index = index,
//        revealedList = revealedList,
//        onRevealChanged = onRevealChanged,
//        scratchPoints = scratchPoints
//    )



    Canvas(
        modifier = Modifier
            .width(10.dp)
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
                        movedOffsetState.value = Offset(it.x, it.y)
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

        movedOffsetState.value?.let {
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

fun getRandomBombDiamondImages(diamond: Int, bomb: Int): List<Int> {
    val imageList = mutableListOf<Int>()

    val diamondCount = Random.nextInt(1, 4)
    val bombCount = 9 - diamondCount

    repeat(diamondCount) { imageList.add(diamond) }
    repeat(bombCount) { imageList.add(bomb) }


    return imageList.shuffled()
}