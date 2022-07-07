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

const val ARROW_WIDTH = 10f
const val ARROW_HEIGHT = 10f

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

            // SE - start and end
            val lengthBetweenSE = sqrt((start.x - end.x).pow(2) + (start.y - end.y).pow(2))

            val dots = mutableMapOf(
                "rectLeftTop" to Offset(x = start.x, y = start.y - lineWidth / 2),
                "rectRightTop" to Offset(x = start.x + lengthBetweenSE - ARROW_WIDTH, y = start.y - lineWidth / 2),
                "arrowLeftTop" to Offset(x = start.x + lengthBetweenSE - ARROW_WIDTH, y = start.y - lineWidth / 2 - ARROW_HEIGHT),
                "arrowRightCenter" to Offset(x = start.x + lengthBetweenSE, y = start.y),
                "arrowLeftBottom" to Offset(x = start.x + lengthBetweenSE - ARROW_WIDTH, y = start.y + lineWidth / 2 + ARROW_HEIGHT),
                "rectRightBottom" to Offset(x = start.x + lengthBetweenSE - ARROW_WIDTH, y = start.y + lineWidth / 2),
                "rectLeftBottom" to Offset(x = start.x, y = start.y + lineWidth / 2)
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

            val dotsAfterRotation = mutableMapOf(
                "rectLeftTop" to rotate(dots["rectLeftTop"]!!, angle),
                "rectRightTop" to rotate(dots["rectRightTop"]!!, angle),
                "arrowLeftTop" to rotate(dots["arrowLeftTop"]!!, angle),
                "arrowRightCenter" to rotate(dots["arrowRightCenter"]!!, angle),
                "arrowLeftBottom" to rotate(dots["arrowLeftBottom"]!!, angle),
                "rectRightBottom" to rotate(dots["rectRightBottom"]!!, angle),
                "rectLeftBottom" to rotate(dots["rectLeftBottom"]!!, angle),
            )

            logger.info("\n[EdgeShape]:" +
                    "\nWithoutRotation:" +
                    "\nrectLeftTop: ${dots["rectLeftTop"]}" +
                    "\nrectRightTop: ${dots["rectRightTop"]}" +
                    "\narrowLeftTop: ${dots["arrowLeftTop"]}" +
                    "\narrowRightCenter: ${dots["arrowRightCenter"]}" +
                    "\narrowLeftBottom: ${dots["arrowLeftBottom"]}" +
                    "\nrectRightBottom: ${dots["rectRightBottom"]}" +
                    "\nrectLeftBottom: ${dots["rectLeftBottom"]}" +
                    "\nWithRotation:" +
                    "\nrectLeftTop: ${dotsAfterRotation["rectLeftTop"]}" +
                    "\nrectRightTop: ${dotsAfterRotation["rectRightTop"]}" +
                    "\narrowLeftTop: ${dotsAfterRotation["arrowLeftTop"]}" +
                    "\narrowRightCenter: ${dotsAfterRotation["arrowRightCenter"]}" +
                    "\narrowLeftBottom: ${dotsAfterRotation["arrowLeftBottom"]}" +
                    "\nrectRightBottom: ${dotsAfterRotation["rectRightBottom"]}" +
                    "\nrectLeftBottom: ${dotsAfterRotation["rectLeftBottom"]}"
            )

            moveTo(dotsAfterRotation["rectLeftTop"]!!.x, dotsAfterRotation["rectLeftTop"]!!.y)
            lineTo(dotsAfterRotation["rectRightTop"]!!.x, dotsAfterRotation["rectRightTop"]!!.y)
            lineTo(dotsAfterRotation["arrowLeftTop"]!!.x, dotsAfterRotation["arrowLeftTop"]!!.y)
            lineTo(dotsAfterRotation["arrowRightCenter"]!!.x, dotsAfterRotation["arrowRightCenter"]!!.y)
            lineTo(dotsAfterRotation["arrowLeftBottom"]!!.x, dotsAfterRotation["arrowLeftBottom"]!!.y)
            lineTo(dotsAfterRotation["rectRightBottom"]!!.x, dotsAfterRotation["rectRightBottom"]!!.y)
            lineTo(dotsAfterRotation["rectLeftBottom"]!!.x, dotsAfterRotation["rectLeftBottom"]!!.y)
            lineTo(dotsAfterRotation["rectLeftTop"]!!.x, dotsAfterRotation["rectLeftTop"]!!.y)

            close()
        }
        return Outline.Generic(path)
    }
}