package com.example.myapplication.data

import androidx.compose.ui.graphics.Brush

data class Carousel(
    val id: Int,
    val carouselType: String,
    val gameName: String,
    val head: String,
    val description: String,
    val color: Brush,
    val image: Int
)