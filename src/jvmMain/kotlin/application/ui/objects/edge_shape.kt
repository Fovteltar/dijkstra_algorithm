package application.ui.objects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import logger
import kotlin.math.*

class EdgeShape(
    val startQuarter: Int,
    val start: Offset,
    val end: Offset,
    val arrowPosition: Offset,
    val lineWidth: Float
): Shape {
    @Throws(IllegalStateException::class)
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            reset()
            logger.info("\n[EdgeShape]" +
                    "\nstart = $start" +
                    "\nend = $end" +
                    "\nsize = $size")

            val length = sqrt((start.x - end.x).pow(2) + (start.y - end.y).pow(2))
            val rectDots = mutableMapOf(
                "leftTop" to Offset(x = start.x, y = start.y - lineWidth / 2),
                "rightTop" to Offset(x = start.x + length, y = start.y - lineWidth / 2),
                "rightBottom" to Offset(x = start.x + length, y = start.y + lineWidth / 2),
                "leftBottom" to Offset(x = start.x, y = start.y + lineWidth / 2)
            )

            val tan = abs(end.y - start.y) / abs(end.x - start.x)

            val degToRad = { angle: Float -> (angle * Math.PI / 180f).toFloat()}

            val angle = when(startQuarter) {
                1 -> { degToRad(180f) + atan(-tan) }
                2 -> { atan(tan) }
                3 -> { atan(-tan) }
                4 -> { degToRad(180f) + atan(tan) }
                else -> { throw Exception("[EdgeShape] Wrong startQuarter") }
            }

            logger.info("\n[EdgeShape]" +
                    "\ntan = $tan" +
                    "\nangle = $angle" +
                    "\nstartQuarter = $startQuarter"
                    )

            // rotate near dot start
            val rotate = { dot: Offset, angle: Float ->
                Offset(
                    x = (dot.x - start.x) * cos(angle) - (dot.y - start.y) * sin(angle) + start.x,
                    y = (dot.x - start.x) * sin(angle) + (dot.y - start.y) * cos(angle) + start.y
                )
            }

            val rectDotsWithRotation = mutableMapOf(
                "leftTop" to rotate(rectDots["leftTop"]!!, angle),
                "rightTop" to rotate(rectDots["rightTop"]!!, angle),
                "rightBottom" to rotate(rectDots["rightBottom"]!!, angle),
                "leftBottom" to rotate(rectDots["leftBottom"]!!, angle),
            )

            logger.info("\n[EdgeShape]:" +
                    "\nWithoutRotation:" +
                    "\nleftTop: ${rectDots["leftTop"]}" +
                    "\nrightTop: ${rectDots["rightTop"]}" +
                    "\nrightBottom: ${rectDots["rightBottom"]}" +
                    "\nleftBottom: ${rectDots["leftBottom"]}" +
                    "\nWithRotation:" +
                    "\nleftTop: ${rectDotsWithRotation["leftTop"]}" +
                    "\nrightTop: ${rectDotsWithRotation["rightTop"]}" +
                    "\nrightBottom: ${rectDotsWithRotation["rightBottom"]}" +
                    "\nleftBottom: ${rectDotsWithRotation["leftBottom"]}"
            )

            moveTo(rectDotsWithRotation["leftTop"]!!.x, rectDotsWithRotation["leftTop"]!!.y)
            lineTo(rectDotsWithRotation["rightTop"]!!.x, rectDotsWithRotation["rightTop"]!!.y)
            lineTo(rectDotsWithRotation["rightBottom"]!!.x, rectDotsWithRotation["rightBottom"]!!.y)
            lineTo(rectDotsWithRotation["leftBottom"]!!.x, rectDotsWithRotation["leftBottom"]!!.y)
            lineTo(rectDotsWithRotation["leftTop"]!!.x, rectDotsWithRotation["leftTop"]!!.y)

            close()
        }
        return Outline.Generic(path)
    }
}