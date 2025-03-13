package com.example.myapplication.Games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
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
import com.example.myapplication.Carousel
import com.example.myapplication.ui.theme.pageBackground
import com.example.myapplication.ui.theme.yelloowww

@Composable
fun GameUI() {
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.13f)
                .layoutId("headerContent")
                .padding(10.dp)
                .border(1.dp, Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = yelloowww,
                            fontSize = 32.sp,
                            textDecoration = TextDecoration.None
                        )
                    ) {
                        append("S")
                    }
                    append("cratch")
                    withStyle(
                        style = SpanStyle(
                            color = yelloowww,
                            fontSize = 32.sp,
                            textDecoration = TextDecoration.None
                        )
                    ) {
                        append("C")
                    }
                    append("ard")
                },
                modifier = Modifier.padding(start = 20.dp),
                color =  MaterialTheme.colorScheme.background,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
            )


        }

        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .background(pageBackground)
                .layoutId("mainContent")
                .border(1.dp, Color.Yellow)
        ) {

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.13f)
                .layoutId("footerContent")
                .padding(10.dp)
                .border(1.dp, Color.Blue),
        ) {
            Text(
                "Footer"
            )


        }
    }
}