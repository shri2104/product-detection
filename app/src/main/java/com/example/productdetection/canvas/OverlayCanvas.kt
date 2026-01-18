package com.example.productdetection.ui

import android.util.Size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size as UiSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.productdetection.model.DetectedProduct

@Composable
fun AROverlay(
    products: List<DetectedProduct>,
    imageSize: Size
) {
    Canvas(modifier = Modifier.fillMaxSize()) {

        val scaleX = size.width / imageSize.width
        val scaleY = size.height / imageSize.height

        products.forEach { product ->
            val r = product.boundingBox

            val left = r.left * scaleX
            val top = r.top * scaleY
            val width = r.width() * scaleX
            val height = r.height() * scaleY

            // Bounding box
            drawRect(
                color = Color.Green,
                topLeft = Offset(left, top),
                size = UiSize(width, height),
                style = Stroke(3f)
            )

            // Tick mark
            drawCircle(
                color = Color.Green,
                radius = 16f,
                center = Offset(left + width - 20, top + 20)
            )
        }
    }
}