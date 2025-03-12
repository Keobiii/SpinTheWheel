package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.pageBackground
import com.example.myapplication.ui.theme.yelloowww
import com.google.common.io.Files.append
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

fun generateRandomCounts(): Pair<Int, Int> {
    val diamondCount = Random.nextInt(1, 4)
    val bombCount = 9 - diamondCount
    return Pair(bombCount, diamondCount)
}

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val (bombCount, diamondCount) = generateRandomCounts()

                var imageResList by remember { mutableStateOf(getCustomRandomImages(R.drawable.diamond, R.drawable.bomb, diamondCount, bombCount)) }
                val revealedList = remember { mutableStateListOf<Boolean>().apply {
                    addAll(List(imageResList.size) { false })
                } }

                val totalCount = diamondCount + bombCount
                Log.i("Bomb Count: ", bombCount.toString())
                Log.i("Diamond Count: ", diamondCount.toString())
                Log.i("Total Count: ", totalCount.toString())

                val constraints = ConstraintSet {
                    val headerContent = createRefFor("headerContent")
                    val mainContent = createRefFor("mainContent")
                    val footerContent = createRefFor("footerContent")

                    constrain(headerContent) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }

                    constrain(mainContent) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }

                    constrain(footerContent) {
                        top.linkTo(mainContent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                }

                ConstraintLayout(
                    constraints,
                    modifier = Modifier.fillMaxSize().background(pageBackground).padding(16.dp)
                ) {

//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .fillMaxHeight(0.13f)
//                                    .layoutId("headerContent")
//                                    .padding(10.dp)
//                                    .border(1.dp, Color.Blue),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Text(
//                                    text = buildAnnotatedString {
//                                        withStyle(
//                                            style = SpanStyle(
//                                                color = yelloowww,
//                                                fontSize = 32.sp,
//                                                textDecoration = TextDecoration.None
//                                            )
//                                        ) {
//                                            append("S")
//                                        }
//                                        append("cratch")
//                                        withStyle(
//                                            style = SpanStyle(
//                                                color = yelloowww,
//                                                fontSize = 32.sp,
//                                                textDecoration = TextDecoration.None
//                                            )
//                                        ) {
//                                            append("C")
//                                        }
//                                        append("ard")
//                                    },
//                                    modifier = Modifier.padding(start = 20.dp),
//                                    color =  MaterialTheme.colorScheme.background,
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    fontStyle = FontStyle.Italic,
//                                    textAlign = TextAlign.Center,
//                                )
//
//
//                            }
//
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxHeight(0.5f)
//                                    .fillMaxWidth()
//                                    .background(pageBackground)
//                                    .layoutId("mainContent")
//                                    .border(1.dp, Color.Yellow)
//                            ) {
//                                LazyVerticalGrid(
//                                    columns = GridCells.Fixed(3),
//                                    modifier = Modifier
//                                        .fillMaxHeight()
//                                        .border(1.dp, Color.Red),
//                                    horizontalArrangement = Arrangement.Center,
//                                    verticalArrangement = Arrangement.Center,
//                                    userScrollEnabled = false,
//                                    content = {
//                                        items(9) { index ->
//                                            ScracthCardScreen(
//                                                baseImage = imageResList[index],
//                                                overlayImage = R.drawable.coin,
//                                            )
//                                        }
//                                    }
//                                )
//                            }
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .fillMaxHeight(0.13f)
//                            .layoutId("footerContent")
//                            .padding(10.dp)
//                            .border(1.dp, Color.Blue),
//                    ) {
//                        Text(
//                            "Footer"
//                        )
//
//
//                    }

                    val items = listOf(
                        WheelSegment("Item 1", Color.Red, Color.White),
                        WheelSegment("Item 2", Color.Green, Color.White),
                        WheelSegment("Item 3", Color.Blue, Color.White),
                        WheelSegment("Item 4", Color.Yellow, Color.White),
                        WheelSegment("Item 5", Color.Cyan, Color.White)
                    )

                    // State to manage the winner index and rotation
                    var winnerIndex by remember { mutableStateOf<Int?>(null) }
                    var spinning by remember { mutableStateOf(false) }

                    // Function to start the spin
                    fun startSpin() {
                        spinning = true
                        // Randomly pick the winner index (simulate a random spin)
                        winnerIndex = (items.indices).random()

                        // Log the winner index after the spin stops (delay here to simulate the stopping)
                        kotlinx.coroutines.GlobalScope.launch {
                            delay(5000) // The duration of the spin
                            spinning = false
                            Log.d("SpinWheel", "Wheel stopped on: ${items[winnerIndex!!].text}")
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Button to start the spin
                        Button(onClick = { startSpin() }) {
                            Text("Spin Wheel")
                        }

                        // Spin wheel composable
                        SpinWheel(
                            modifier = Modifier.size(300.dp),
                            items = items,
                            winnerIndex = winnerIndex,
                            spinDuration = 5000,
                            spinAction = { startSpin() }
                        )
                    }


                }

//                Surface(modifier = Modifier.fillMaxSize()) {
//                    Column(
//                        modifier = Modifier.background(pageBackground).padding(16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Top
//                    )  {
//
//                        Box(
//                            modifier = Modifier
//                                .fillMaxHeight(0.49f)
//                                .fillMaxWidth()
//                                .background(pageBackground)
//                                .layoutId("mainContent")
//                                .border(1.dp, Color.Yellow)
//                        ) {
//
//
//
//                        }
//
//
//
//
//
//
//
//                    }
//                }
            }
        }
    }
}



//@Composable
//fun MessageCard(author: String, body: String) {
//    Row(modifier = Modifier.padding(16.dp)) {
//        Image(
//            painter = painterResource(R.drawable.profile),
//            contentDescription = null,
//            modifier = Modifier
//                .size(40.dp)
//                .clip(CircleShape)
//                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
//        )
//
//        Spacer(modifier = Modifier.width(8.dp))
//        var isExpanded by remember { mutableStateOf(false) }
//
//        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
//            Text(
//                text = author,
//                color = MaterialTheme.colorScheme.secondary,
//                style = MaterialTheme.typography.titleSmall
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Surface(
//                shape = MaterialTheme.shapes.medium,
//                shadowElevation = 1.dp,
//            ) {
//                Text(
//                    text = body,
//                    modifier = Modifier.padding(all = 4.dp),
//                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
//                    style = MaterialTheme.typography.bodyMedium,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//        }
//    }
//}


fun getCustomRandomImages(image1: Int, image2: Int, count1: Int, count2: Int): List<Int> {
    // Creating a new list
    val imageList = mutableListOf<Int>()

    // Store the specific count of images in the list
    repeat(count1) { imageList.add(image1) }
    repeat(count2) { imageList.add(image2) }

    // Shuffle to randomize the order
    return imageList.shuffled()
}

@Composable
fun ScracthCardScreen(
    baseImage: Int,
    overlayImage: Int,
) {

    val baseImage = ImageBitmap.imageResource(id = baseImage)
    val overlayImage = ImageBitmap.imageResource(id = overlayImage)
    val currentPathState = remember { mutableStateOf(DraggedPath(path = Path())) }
    val movedOffsetState = remember { mutableStateOf<Offset?>(null) }

        ScratchCard(
            overlayImage = overlayImage,
            baseImage = baseImage,
            movedOffset = movedOffsetState.value,
            onMovedoffset =  { x, y ->
                movedOffsetState.value = Offset(x, y)
            },
            currentPath = currentPathState.value.path,
            currentPathThickness = currentPathState.value.width,

        )

}