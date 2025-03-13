package com.example.myapplication

import androidx.annotation.OptIn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.myapplication.Dialog.CustomCardDialog

@OptIn(UnstableApi::class)
@Composable
fun ScratchWin() {
    var showDialog by remember { mutableStateOf(false) }


    var resetTrigger by remember { mutableStateOf(0) }

    val (bombCount, diamondCount) = generateRandomCounts()

    // random image based on the logic implemented
    val imageResList by remember { mutableStateOf(getCustomRandomImages(R.drawable.diamond, R.drawable.bomb, diamondCount, bombCount)) }


    val revealedList = remember { mutableStateListOf<Boolean>().apply {
        addAll(List(imageResList.size) { false })
    } }


    val scratchPoints = remember { List(imageResList.size) { mutableStateListOf<Offset>() } }


    fun resetGame() {
        resetTrigger++
    }

    val totalCount = diamondCount + bombCount
    Log.i("Bomb Count: ", bombCount.toString())
    Log.i("Diamond Count: ", diamondCount.toString())
    Log.i("Total Count: ", totalCount.toString())

    if (showDialog) {
        CustomCardDialog(
            onDismiss = { showDialog = false },
            fontFamily = FontFamily.SansSerif,
            title = if (bombCount >= 6) "Talo" else "Panalo",
            description = if (bombCount >= 6) " " else ""
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxHeight()
            .border(1.dp, Color.Red),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
        userScrollEnabled = false,
        content = {
            items(9) { index ->
                ScracthCardScreen(
                    baseImage = imageResList[index],
                    overlayImage = R.drawable.coin,
                    index = index,
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
    )
}
@Composable
fun ScracthCardScreen(
    baseImage: Int,
    overlayImage: Int,
    index: Int,
    revealedList: MutableList<Boolean>,
    onRevealChanged: (Int, Boolean) -> Unit,
    scratchPoints: SnapshotStateList<Offset>
) {
    val baseImage = ImageBitmap.imageResource(id = baseImage)
    val overlayImage = ImageBitmap.imageResource(id = overlayImage)

    val currentPathState = remember { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffsetState = remember { mutableStateOf<Offset?>(null) }


    ScratchCard(
        overlayImage = overlayImage,
        baseImage = baseImage,
        movedOffset = movedOffsetState.value,
        onMovedoffset = { x, y ->
            movedOffsetState.value = Offset(x, y)
        },
        currentPath = currentPathState.value.path,
        currentPathThickness = currentPathState.value.width,
        index = index,
        revealedList = revealedList,
        onRevealChanged = onRevealChanged,
        scratchPoints = scratchPoints
    )
}