package com.example.productdetection.camera

import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.productdetection.ml.ObjectDetectorHelper
import com.example.productdetection.ui.AROverlay

@Composable
fun CameraScreen() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val detector = remember { ObjectDetectorHelper.create() }
    val imageSize = remember { Size(1280, 720) }

    val trackedProducts = remember {
        mutableStateListOf<com.example.productdetection.model.DetectedProduct>()
    }

    Box(modifier = Modifier.fillMaxSize()) {

        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val providerFuture = ProcessCameraProvider.getInstance(ctx)
                providerFuture.addListener({

                    val cameraProvider = providerFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val analysis = ImageAnalysis.Builder()
                        .setTargetResolution(imageSize)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    analysis.setAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        CameraAnalyzer(detector) { rects ->
                            val updated = ProductTracker.update(rects)
                            trackedProducts.clear()
                            trackedProducts.addAll(updated)
                        }
                    )

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        analysis
                    )

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        AROverlay(
            products = trackedProducts,
            imageSize = imageSize
        )
    }
}
