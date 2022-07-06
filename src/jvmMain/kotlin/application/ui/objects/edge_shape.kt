package application.ui.objects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

const val ARROW_SIZE = 10f

class EdgeShape(
    val startQuarter: Int,
    val start: Offset,
    val end: Offset,
    val arrowPosition: Offset
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            reset()
//            val gradient = (size.height - VERTEX_SIZE) / (size.width - VERTEX_SIZE)
//            val b = start.y + 1 / gradient * start.x
//            val coordX = (- 2 * gradient * b + sqrt(4 * gradient.pow(2) * b.pow(2) - 4 * (gradient.pow(2) + 1) * (b.pow(2) - ARROW_SIZE.pow(2)))) /
//                (2 * (gradient.pow(2) + 1))
//            val coordY = gradient * (coordX) + b
//            println("$coordX, $coordY")
//            addRect(Rect(start, end))

//            addRect(Rect())
            moveTo(x = start.x, y = start.y)
//            lineTo()

//            addRect(Rect(VERTEX_SIZE / 2f, VERTEX_SIZE / 2f, size.width - VERTEX_SIZE / 2f, size.height - VERTEX_SIZE / 2f))
            close()
        }
        return Outline.Generic(path)
    }
}