package application.ui.objects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import application.controller.Tools
import logger
import logic.Edge
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

const val LINE_WIDTH = 10f

class EdgeUI(val verticesUI: Pair<VertexUI, VertexUI>, private val tools: Tools) {
    val edge = Edge(Pair(verticesUI.first.vertex, verticesUI.second.vertex))
    @Composable
    fun draw() {
        val size = Offset(
            x = abs(
                verticesUI.first.topLeftOffset.x
                        - verticesUI.second.topLeftOffset.x
            ) + VERTEX_SIZE,
            y = abs(
                verticesUI.first.topLeftOffset.y
                        - verticesUI.second.topLeftOffset.y
            ) + VERTEX_SIZE
        )
        val startTopLeft = Pair(
            first = verticesUI.first.topLeftOffset.x < verticesUI.second.topLeftOffset.x,
            second = verticesUI.first.topLeftOffset.y < verticesUI.second.topLeftOffset.y
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

        Surface(
            modifier = Modifier
                .offset(
                    x = min(
                        verticesUI.first.topLeftOffset.x,
                        verticesUI.second.topLeftOffset.x,
                    ).dp,
                    y = min(
                        verticesUI.first.topLeftOffset.y,
                        verticesUI.second.topLeftOffset.y,
                    ).dp
                )
                .size(
                    width = size.x.dp,
                    height = size.y.dp
                )
                .clip(EdgeShape(startQuarter, start, end, end, LINE_WIDTH))
                .clickable {
                    tools.notifyMe(this)
                },
            shape = EdgeShape(startQuarter, start, end, end, LINE_WIDTH),
            color = Color.Red
        ) {}
    }
}