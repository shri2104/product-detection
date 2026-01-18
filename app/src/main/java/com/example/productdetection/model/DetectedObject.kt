package com.example.productdetection.model




import android.graphics.Rect

data class DetectedProduct(
    val id: Int,
    val boundingBox: Rect
)
