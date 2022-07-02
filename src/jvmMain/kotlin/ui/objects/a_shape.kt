//package ui.objects
//
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Outline
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.unit.Density
//import androidx.compose.ui.unit.LayoutDirection
//import kotlin.math.pow
//import kotlin.math.sqrt
//import logger
//
//class AShape(
//    val startTopLeft: Pair<Boolean, Boolean> = Pair(true, true),
//    val arrowSize: Int
//): Shape {
//    override fun createOutline(
//        size: Size,
//        layoutDirection: LayoutDirection,
//        density: Density
//    ): Outline {
//        val path = Path().apply {
//            reset()
//            val gradient = size.height / size.width
//            val dislocation = let {
//                val radius = VERTEX_SIZE / 2
//                val x = radius / sqrt((gradient.pow(2) + 1))
//                Offset(
//                    x = x,
//                    y = gradient * x
//                )
//            }
//            logger.info("size: ${size.width} ${size.height}" +
//                    " gradient: $gradient" +
//                    " dislocation: ${dislocation.x} ${dislocation.y}")
//            val startPos = Offset(
//                x = if (startTopLeft.first) 0f + dislocation.x else size.width - dislocation.x,
//                y = if (startTopLeft.second) 0f + dislocation.y else size.height - dislocation.y
//            )
//            val endPos = Offset(
//                x = if (startTopLeft.first) size.width - dislocation.x else 0f + dislocation.x,
//                y = if (startTopLeft.second) size.height - dislocation.y else 0f + dislocation.y
//            )
//
//            logger.info("startPos: $startPos, endPos: $endPos, size: $size")
//
//            val (startCoordinateQuarter: Int, endCoordinateQuarter: Int) = let {
//                if (endPos.y > startPos.y) {
//                    if (endPos.x > startPos.x) mutableListOf(1, 4)
//                    else mutableListOf(2, 3)
//                }
//                else {
//                    if (endPos.x > startPos.x) mutableListOf(3, 2)
//                    else mutableListOf(4, 1)
//                }
//            }
//
//            // Triangle
//            val arrowGradient = (endPos.y - startPos.y) / (endPos.x - startPos.x)
//            val arrowStartPoint = let {
//                val arrowX: Float
//                val arrowY: Float
//                when(startCoordinateQuarter) {
//                    1 -> {
//                        arrowX = endPos.x - sqrt(3f) * arrowSize / (2 * sqrt(arrowGradient.pow(2) + 1))
//                        arrowY = startPos.y + arrowGradient * arrowX - dislocation.y
//                    }
//                    2 -> {
//                        arrowX = endPos.x + sqrt(3f) * arrowSize / (2 * sqrt(arrowGradient.pow(2) + 1))
//                        arrowY = endPos.y + arrowGradient * arrowX + dislocation.y
//                    }
//                    3 -> {
//                        arrowX = endPos.x - sqrt(3f) * arrowSize / (2 * sqrt(arrowGradient.pow(2) + 1))
//                        arrowY = startPos.y + arrowGradient * arrowX + dislocation.y
//                    }
//                    else -> {
//                        arrowX = endPos.x + sqrt(3f) * arrowSize / (2 * sqrt(arrowGradient.pow(2) + 1))
//                        arrowY = endPos.y + arrowGradient * arrowX - dislocation.y
//                    }
//                }
//                Offset(
//                    x = arrowX,
//                    y = arrowY
//                )
//            }
//
//
//            logger.info("arrowStartPoint: ${arrowStartPoint.x} ${arrowStartPoint.y}" +
//                    " grad: ${arrowStartPoint.y / arrowStartPoint.x}" +
//                    " arrowGradient: $arrowGradient")
//
//            val orthogonalGradient = -1 / arrowGradient
//            val arrowRightPoint = let {
//                val arrowX = arrowStartPoint.x + sqrt(arrowSize / (2 * (orthogonalGradient.pow(2) + 1)))
//                val arrowY = orthogonalGradient * (arrowX - arrowStartPoint.x) + arrowStartPoint.y
//                Offset(
//                    x = arrowX,
//                    y = arrowY
//                )
//            }
//
//            logger.info("arrowRightPoint: ${arrowRightPoint.x} ${arrowRightPoint.y}")
//
//            val arrowLeftPoint = let {
//                val arrowX = 2 * arrowStartPoint.x - arrowRightPoint.x
//                val arrowY = orthogonalGradient * (arrowX - arrowStartPoint.x) + arrowStartPoint.y
//                Offset(
//                    x = arrowX,
//                    y = arrowY
//                )
//            }
//
//            logger.info("arrowLeftPoint: ${arrowLeftPoint.x} ${arrowLeftPoint.y}")
//
//            moveTo(
//                x = startPos.x,
//                y = startPos.y - DELTAY / 2
//            )
//            // Separate line on four parts
//            lineTo(
//                x = let {
//                    when (startCoordinateQuarter) {
//                        1, 3 -> {
//                            arrowStartPoint.x - (arrowRightPoint.x - arrowLeftPoint.x) / 4
//                        }
//                        else -> {
//                            arrowStartPoint.x + (arrowRightPoint.x - arrowLeftPoint.x) / 4
//                        }
//                    }
//                },
//                y = let {
//                    when (startCoordinateQuarter) {
//                        1, 4 -> {
//                            arrowStartPoint.y + (arrowRightPoint.y - arrowLeftPoint.y) / 4
//                        }
//                        else -> {
//                            arrowStartPoint.y - (arrowRightPoint.y - arrowLeftPoint.y) / 4
//                        }
//                    }
//                }
//            )
//            lineTo(
//                x = arrowLeftPoint.x,
//                y = arrowLeftPoint.y
//            )
//            lineTo(
//                x = endPos.x,
//                y = endPos.y
//            )
//            lineTo(
//                x = arrowRightPoint.x,
//                y = arrowRightPoint.y
//            )
//            // Separate line on four parts
//            lineTo(
//                x = let {
//                    when (startCoordinateQuarter) {
//                        1, 4 -> {
//                            arrowStartPoint.x - (arrowRightPoint.x - arrowLeftPoint.x) / 4
//                        }
//                        else -> {
//                            arrowStartPoint.x + (arrowRightPoint.x - arrowLeftPoint.x) / 4
//                        }
//                    }
//                },
//                y = let {
//                    when (startCoordinateQuarter) {
//                        1, 4 -> {
//                            arrowStartPoint.y - (arrowRightPoint.y - arrowLeftPoint.y) / 4
//                        }
//                        else -> {
//                            arrowStartPoint.y + (arrowRightPoint.y - arrowLeftPoint.y) / 4
//                        }
//                    }
//                }
//            )
//            lineTo(
//                x = startPos.x,
//                y = startPos.y + DELTAY / 2
//            )
//            lineTo(
//                x = startPos.x,
//                y = startPos.y - DELTAY / 2
//            )
//
//            close()
//        }
//        return Outline.Generic(path)
//    }
//}