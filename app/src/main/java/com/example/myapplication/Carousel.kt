package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.ch1EndColor
import com.example.myapplication.ui.theme.ch1StartColor
import com.example.myapplication.ui.theme.ch2EndColor
import com.example.myapplication.ui.theme.ch2StartColor
import com.example.myapplication.ui.theme.ch3EndColor
import com.example.myapplication.ui.theme.ch3StartColor
import com.example.myapplication.ui.theme.ch4EndColor
import com.example.myapplication.ui.theme.ch4StartColor
import com.example.myapplication.data.Carousel
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay


val carousel = listOf(
    Carousel(
        carouselType = "discount",
        title = "Get 10 Free Spin!",
        description = "Earn Free 10 Spin for every Php 1000 spend at Wizzard Land",
        color =  getGradient(ch1StartColor, ch1EndColor),
        image = R.drawable.ch1
    ),

    Carousel(
        carouselType = "free",
        title = "Free Php 100!",
        description = "Get Php 100 for new user!",
        color =  getGradient(ch2StartColor, ch2EndColor),
        image = R.drawable.ch2
    ),

    Carousel(
        carouselType = "discount",
        title = "50% Cashback!",
        description = "Up to 50% cashback you can earn from top up!",
        color =  getGradient(ch3StartColor, ch3EndColor),
        image = R.drawable.ch1
    ),

    Carousel(
        carouselType = "free",
        title = "Earn Php 100!",
        description = "Play the new Game and get Php 100!",
        color =  getGradient(ch4StartColor, ch4EndColor),
        image = R.drawable.ch2
    )
)

fun getGradient(
    startColor: Color,
    endColor: Color,
): Brush {
    return Brush.linearGradient(
        colors = listOf(startColor, endColor)
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Carousel() {

    val pagerState = rememberPagerState(pageCount = carousel.size)

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.scrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(170.dp)
                .fillMaxWidth()
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp)),

                ) { currentPage ->

                Card(
                    modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize().background(carousel[currentPage].color)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize().padding(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(0.6f).fillMaxHeight().padding(end = 5.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    text = carousel[currentPage].title,
                                    fontSize = 17.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start
                                )

                                Text(
                                    text = carousel[currentPage].description,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start
                                )
                            }

                            Box(
                                modifier = Modifier.weight(0.4f)
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = painterResource(id = carousel[currentPage].image),
                                    contentDescription = carousel[currentPage].title,
                                    contentScale = ContentScale.Inside
                                )
                            }
                        }
                    }


                }
            }
        }
    }
}
