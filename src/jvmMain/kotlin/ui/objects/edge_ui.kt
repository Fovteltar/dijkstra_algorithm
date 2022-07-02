package ui.objects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import logger
import logic.Edge
import logic.SelectedTool
import ui.window.Canvas
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

const val LINE_WIDTH = 10f

class EdgeUI(val edge: Edge) {
    // subscriber
    var canvas: Canvas? = null
    @Composable
    fun drawEdge() {
        val size = Offset(
            x = abs(
                edge.vertices.first.vertexUI.topLeftOffset.x
                        - edge.vertices.second.vertexUI.topLeftOffset.x
            ) + VERTEX_SIZE,
            y = abs(
                edge.vertices.first.vertexUI.topLeftOffset.y
                        - edge.vertices.second.vertexUI.topLeftOffset.y
            ) + VERTEX_SIZE
        )
        val startTopLeft = Pair(
            first = edge.vertices.first.vertexUI.topLeftOffset.x < edge.vertices.second.vertexUI.topLeftOffset.x,
            second = edge.vertices.first.vertexUI.topLeftOffset.y < edge.vertices.second.vertexUI.topLeftOffset.y
        )
        val startQuarter = if (startTopLeft.first) {
            if (startTopLeft.second) 2
            else 3
        } else {
            if (startTopLeft.second) 1
            else 4
        }
        // for start position of the vertex with shape circle
        // additional else for case where vertex1.x == vertex2.x
        val dislocation = if (size.x - VERTEX_SIZE != 0f) {
            // gradient = a, there y = ax + b
            val gradient = (size.y - VERTEX_SIZE) / (size.x - VERTEX_SIZE)
            val dislocation = let {
                val radius = VERTEX_SIZE / 2
                val x = radius / sqrt((gradient.pow(2) + 1))
                Offset(
                    x = x,
                    y = gradient * x
                )
            }

            logger.info("[EdgeUI] gradient: $gradient")

            dislocation
        } else Offset(0f, VERTEX_SIZE / 2f)

        logger.info(
            """
                [EdgeUI]:
                size: ${size.x} ${size.y}
                dislocation: ${dislocation.x} ${dislocation.y}
            """.trimIndent()
        )
        val start =
            when (startQuarter) {
                1 -> Offset(size.x - dislocation.x - VERTEX_SIZE / 2, 0f + dislocation.y + VERTEX_SIZE / 2)
                2 -> Offset(0f + dislocation.x + VERTEX_SIZE / 2, 0f + dislocation.y + VERTEX_SIZE / 2)
                3 -> Offset(0f + dislocation.x + VERTEX_SIZE / 2, size.y - dislocation.y - VERTEX_SIZE / 2)
                4 -> Offset(size.x - dislocation.x - VERTEX_SIZE / 2, size.y - dislocation.y - VERTEX_SIZE / 2)
                else -> throw Error("Incorrect startQuarter value")
            }
        val end =
            when (startQuarter) {
                1 -> Offset(0f + dislocation.x + VERTEX_SIZE / 2, size.y - dislocation.y - VERTEX_SIZE / 2)
                2 -> Offset(size.x - dislocation.x - VERTEX_SIZE / 2, size.y - dislocation.y - VERTEX_SIZE / 2)
                3 -> Offset(size.x - dislocation.x - VERTEX_SIZE / 2, 0f + dislocation.y + VERTEX_SIZE / 2)
                4 -> Offset(0f + dislocation.x + VERTEX_SIZE / 2, 0f + dislocation.y + VERTEX_SIZE / 2)
                else -> throw Error("Incorrect startQuarter value")
            }
        /*
        Surface(
            modifier = Modifier
                .offset(
                    x = min(
                        edge.vertices.first.vertexUI.topLeftOffset.x,
                        edge.vertices.second.vertexUI.topLeftOffset.x,
                    ).dp, // + VERTEX_SIZE.dp / 2,
                    y = min(
                        edge.vertices.first.vertexUI.topLeftOffset.y,
                        edge.vertices.second.vertexUI.topLeftOffset.y,
                    ).dp, // + VERTEX_SIZE.dp / 2,
                )
                .size(
                    width = size.x.dp,
                    height = size.y.dp
                )
                .clip(shape = ArrowShape(
                    Pair(
                        first = edge.vertices.first.vertexUI.topLeftOffset.x < edge.vertices.second.vertexUI.topLeftOffset.x,
                        second = edge.vertices.first.vertexUI.topLeftOffset.y < edge.vertices.second.vertexUI.topLeftOffset.y
                    )
                ))
                .clickable {
                    when(canvas?.tools?.selectedTool) {
                        SelectedTool.REMOVE_EDGE -> {
                            canvas?.tools?.removeEdge(edge)
                        }
                    }
                },
            color = Color.Red
        ) {}
        */
        Canvas(
            modifier = Modifier
                .offset(
                    x = min(
                        edge.vertices.first.vertexUI.topLeftOffset.x,
                        edge.vertices.second.vertexUI.topLeftOffset.x,
                    ).dp,
                    y = min(
                        edge.vertices.first.vertexUI.topLeftOffset.y,
                        edge.vertices.second.vertexUI.topLeftOffset.y,
                    ).dp
                )
                .size(
                    width = size.x.dp,
                    height = size.y.dp
                )
                .clip(RectangleShape /*ArrowShape(startQuarter, start, end, end)*/)
                .clickable {
                    val tools = canvas?.tools
                    when (tools?.selectedTool) {
                        SelectedTool.REMOVE_EDGE -> {
                            tools.removeEdge(edge)
                        }
                        else -> {}
                    }
                },
        ) {
            drawLine(
                color = Color.Red,
                start = start,
                end = end,
                strokeWidth = LINE_WIDTH
            )
            drawCircle(
                color = Color.Magenta,
                radius = ARROW_SIZE,
                center = end,
            )
        }
    }

    fun subscribeCanvas(canvas: Canvas) {
        this.canvas = canvas
    }
}