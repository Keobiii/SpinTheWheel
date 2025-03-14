package com.example.myapplication.Games

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.myapplication.Dialog.CustomCardDialog
import com.example.myapplication.R
import com.example.myapplication.ui.theme.eggGray
import com.example.myapplication.ui.theme.pageBackground
import com.example.myapplication.ui.theme.yelloowww

fun getRandomImagebyCount(image1: Int, image2: Int, count1: Int, count2: Int): List<Int> {
    // Creating a new list
    val imageList = mutableListOf<Int>()

    // Store the specific count of images in the list
    repeat(count1) { imageList.add(image1) }
    repeat(count2) { imageList.add(image2) }

    // Shuffle to randomize the order
    return imageList.shuffled()
}

@Composable
fun Mining(index: Int, title: String) {
    var imageResList by remember { mutableStateOf(getRandomImagebyCount(R.drawable.diamond_2, R.drawable.bomb_2, 20, 5)) }
    var diamondCount by remember { mutableStateOf(0) }
    var bombCount by remember { mutableStateOf(0) }

    var resetTrigger by remember { mutableStateOf(0) }

    var totalBomb by remember { mutableStateOf(5) }

    var betVal by remember { mutableStateOf("0") }

    var showDialog by remember { mutableStateOf(false) }

    var enableClick by remember { mutableStateOf(true) }

    val revealedList = remember(resetTrigger) { mutableStateListOf<Boolean>().apply {
        addAll(List(imageResList.size) { false })
    } }


    fun onReveal(imageRes: Int) {
        if (imageRes == R.drawable.diamond_2) {
            diamondCount++
        } else if (imageRes == R.drawable.bomb_2) {
            bombCount++
            totalBomb -= 1
        }
    }
    Log.i("Bomb Count: ", bombCount.toString())
    Log.i("Diamond Count: ", diamondCount.toString())


    // Reset when all bombs are found
    fun resetGame() {
        bombCount = 0
        diamondCount = 0
        totalBomb = 5
        imageResList = getCustomRandomImages(R.drawable.diamond_2, R.drawable.bomb_2, 20, 5)
        resetTrigger++
        enableClick = true
    }

    if (showDialog) {
        CustomCardDialog(
            onDismiss = { showDialog = false },
            fontFamily = FontFamily.SansSerif,
            title = if (bombCount == 5) "Game Over" else "You Won",
            description = if (bombCount == 5) "Try not to pick bombs next time!" else "You just won! Do you want to play again?"
        )
    }

    // Trigger the dialog when conditions are met
    LaunchedEffect(bombCount, diamondCount) {
        if (bombCount == 5 || diamondCount == 20) {
            showDialog = true
            enableClick = false
        }
    }



    val constraints = ConstraintSet {
        val headerContent = createRefFor("headerContent")
        val headMainContent = createRefFor("headMainContent")
        val mainContent = createRefFor("mainContent")
        val belowMain = createRefFor("belowMain")
        val footerContent = createRefFor("footerContent")

        constrain(headerContent) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(headMainContent) {
            top.linkTo(headerContent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(mainContent.top)
        }

        constrain(mainContent) {
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }

        constrain(belowMain) {
            top.linkTo(mainContent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(footerContent) {
            top.linkTo(belowMain.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }


    }

    Column(
        modifier = Modifier.fillMaxSize().background(pageBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .border(1.dp, Color.Red)
                    .fillMaxWidth(0.8f)
                    .background(pageBackground),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,
                userScrollEnabled = false,
                content = {
                    items(imageResList.size) { index ->
                        BoxesUI(
                            imageRes = imageResList[index],
                            resetTrigger,
                            enableClick = enableClick,
                            onReveal = {
                                if (!revealedList[index]) {
                                    revealedList[index] = true
                                    onReveal(it)
                                }
                            }
                        )
                    }
                }
            )

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

//    ConstraintLayout(
//        constraints,
//        modifier = Modifier.fillMaxSize().background(pageBackground).padding(16.dp)
//    ) {
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.13f)
//                .layoutId("headerContent")
//                .padding(10.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
////            Box(
////                modifier = Modifier.fillMaxWidth(0.3f)
////            ) {
////                Image(
////                    painter = painterResource(id = R.drawable.diamong_strike),
////                    contentDescription = "",
////                    contentScale = ContentScale.Crop
////                )
////            }
//
////            Text(
////
////                text = title,
////                fontSize = 22.sp,
////                fontFamily = FontFamily.SansSerif,
////                fontWeight = FontWeight.Bold,
////                color = MaterialTheme.colorScheme.onBackground
////            )
//            Text(
//                text = buildAnnotatedString {
//                    withStyle(
//                        style = SpanStyle(
//                            color = yelloowww,
//                            fontSize = 20.sp,
//                            textDecoration = TextDecoration.None
//                        )
//                    ) {
//                        append("D")
//                    }
//                    append("iamond ")
//                    withStyle(
//                        style = SpanStyle(
//                            color = yelloowww,
//                            fontSize = 20.sp,
//                            textDecoration = TextDecoration.None
//                        )
//                    ) {
//                        append("S")
//                    }
//                    append("trike")
//                },
//                modifier = Modifier.padding(start = 20.dp),
//                color =  MaterialTheme.colorScheme.onBackground,
//                fontSize = 16.sp,
//                fontFamily = FontFamily.SansSerif,
//                fontWeight = FontWeight.Bold,
//                fontStyle = FontStyle.Italic,
//                textAlign = TextAlign.Center,
//            )
//
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.07f)
//                .clip(RoundedCornerShape(30.dp))
//                .background(eggGray)
//                .layoutId("headMainContent"),
//            contentAlignment = Alignment.Center
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth().padding(10.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = "1x",
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Normal
//                )
//                Text(
//                    text = "2x",
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Normal
//                )
//                Text(
//                    text = "4x",
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Normal
//                )
//                Text(
//                    text = "6x",
//                    fontSize = 18.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )
//                Text(
//                    text = "8x",
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Normal
//                )
//                Text(
//                    text = "10x",
//                    fontSize = 14.sp,
//                    fontFamily = FontFamily.SansSerif,
//                    fontWeight = FontWeight.Normal
//                )
//
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .fillMaxHeight(0.49f)
//                .fillMaxWidth()
//                .background(pageBackground)
//                .layoutId("mainContent")
//                .border(1.dp, Color.Yellow)
//        ) {
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(5),
//                modifier = Modifier
//                    .fillMaxHeight(),
//                horizontalArrangement = Arrangement.Center,
//                verticalArrangement = Arrangement.Center,
//                userScrollEnabled = false,
//                content = {
//                    items(imageResList.size) { index ->
//                        BoxesUI(
//                            imageRes = imageResList[index],
//                            resetTrigger,
//                            enableClick = enableClick,
//                            onReveal = {
//                                if (!revealedList[index]) {
//                                    revealedList[index] = true
//                                    onReveal(it)
//                                }
//                            }
//                        )
//                    }
//                }
//            )
//        }
//
//        Box(
//            modifier = Modifier
//                .wrapContentWidth()
//                .fillMaxHeight(0.1f)
//                .layoutId("belowMain"),
//            contentAlignment = Alignment.Center
//        ) {
//            LazyHorizontalGrid(
//                rows = GridCells.Fixed(1),
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .fillMaxWidth(),
//                horizontalArrangement = if (totalBomb <= 3) Arrangement.SpaceEvenly else Arrangement.SpaceBetween,
//                verticalArrangement = Arrangement.Center,
//                userScrollEnabled = false,
//                content = {
//                    items(totalBomb) { index ->
//                        Box(
//                            modifier = Modifier.size(27.dp)
//                        ) {
//                            Image(
//                                modifier = Modifier.fillMaxSize(),
//                                painter = painterResource(id = R.drawable.bomb),
//                                contentDescription = "Bomb Icon"
//                            )
//                        }
//                    }
//
//                }
//            )
//        }
//
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(0.16f)
//                .layoutId("footerContent"),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Card(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .weight(0.3f)
//                    .padding(end = 5.dp, top = 5.dp, bottom = 5.dp),
//                colors = CardDefaults.cardColors(Color.Transparent)
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize().padding(5.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Bet",
//                        fontFamily = FontFamily.SansSerif,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
////                    Text(
////                        text = "$ 10",
////                        fontFamily = FontFamily.SansSerif,
////                        fontSize = 16.sp,
////                        fontWeight = FontWeight.Bold
////                    )
//                    BasicTextField(
//                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).fillMaxWidth().background(
//                            Color.Black),
//                        value = betVal,
//                        onValueChange = { betVal = it },
//                        singleLine = true,
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        textStyle = TextStyle(
//                            color = Color.White,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold,
//                            fontFamily = FontFamily.SansSerif,
//                            textAlign = TextAlign.Center
//                        )
//                    )
//                }
//            }
//
//            Card(
//                modifier = Modifier
//                    .padding(start = 5.dp, end = 5.dp, top = 10.dp, bottom = 10.dp)
//                    .fillMaxHeight()
//                    .weight(0.25f)
//                    .clip(RoundedCornerShape(10.dp))
//                    .clickable {
//                        resetGame()
//                    },
//                colors = CardDefaults.cardColors(yelloowww)
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Game",
//                        color = Color.Black,
//                        fontSize = 18.sp,
//                        fontFamily = FontFamily.SansSerif,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//
//            }
//
//            Card(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .weight(0.3f)
//                    .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
//                colors = CardDefaults.cardColors(Color.Transparent),
//
//                ) {
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Winning",
//                        fontFamily = FontFamily.SansSerif,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                    Text(
//                        text = "$ 100",
//                        fontFamily = FontFamily.SansSerif,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//
//    }

}



@Composable
fun BoxesUI(imageRes: Int, resetTrigger: Int, onReveal: (Int) -> Unit, enableClick: Boolean) {
    var isClicked by remember(resetTrigger) { mutableStateOf(false) }
    var showNewImage by remember(resetTrigger) { mutableStateOf(false) }


    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.8f else 1f,
        animationSpec = tween(durationMillis = 300),
        finishedListener = {
            if (isClicked) {
                showNewImage = true
                isClicked = false
                onReveal(imageRes)
            }
        }, label = "Pop Animation"
    )

    val currentImage = remember(showNewImage) {
        if (showNewImage) imageRes else R.drawable.coin_2
    }

    // hmm
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .background(pageBackground)
            .aspectRatio(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                enabled = if (enableClick) true else false
            ) {
                if (!isClicked) {
                    isClicked = true
//                    mMediaPlayer.start()
                }
            }
            .scale(scale),
    ) {
        AnimatedContent(targetState = currentImage) { image ->
            Image(
                modifier = Modifier.fillMaxSize().padding(10.dp),
                painter = painterResource(id = image),
                contentDescription = "Image"
            )
        }
    }
}




