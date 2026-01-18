import android.graphics.Rect
import com.example.productdetection.model.DetectedProduct

object ProductTracker {

    private val trackedProducts = mutableListOf<TrackedItem>()
    private var idCounter = 0

    private const val IOU_THRESHOLD = 0.5f
    private const val DISTANCE_THRESHOLD = 80f
    private const val STABLE_FRAMES = 5

    fun update(detections: List<Rect>): List<DetectedProduct> {

        detections.forEach { rect ->

            val match = trackedProducts.firstOrNull {
                iou(it.boundingBox, rect) > IOU_THRESHOLD ||
                        distance(it.boundingBox, rect) < DISTANCE_THRESHOLD
            }

            if (match != null) {
                match.framesSeen++
                match.boundingBox = rect
            } else {
                trackedProducts.add(
                    TrackedItem(
                        id = idCounter++,
                        boundingBox = rect,
                        framesSeen = 1
                    )
                )
            }
        }

        return trackedProducts
            .filter { it.framesSeen >= STABLE_FRAMES }
            .map {
                DetectedProduct(
                    id = it.id,
                    boundingBox = it.boundingBox
                )
            }
    }


    private fun distance(a: Rect, b: Rect): Float {
        val ax = a.centerX().toFloat()
        val ay = a.centerY().toFloat()
        val bx = b.centerX().toFloat()
        val by = b.centerY().toFloat()

        return kotlin.math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by))
    }

    private fun iou(a: Rect, b: Rect): Float {
        val xA = maxOf(a.left, b.left)
        val yA = maxOf(a.top, b.top)
        val xB = minOf(a.right, b.right)
        val yB = minOf(a.bottom, b.bottom)

        val interArea = maxOf(0, xB - xA) * maxOf(0, yB - yA)
        val boxAArea = a.width() * a.height()
        val boxBArea = b.width() * b.height()

        return interArea.toFloat() / (boxAArea + boxBArea - interArea + 1e-6f)
    }
    private data class TrackedItem(
        val id: Int,
        var boundingBox: Rect,
        var framesSeen: Int
    )
}

