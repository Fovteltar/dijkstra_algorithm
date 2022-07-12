package application.ui.objects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.controller.Tools
import application.ui.dialog.EdgeDialog
import logger
import logic.Edge
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

const val LINE_WIDTH = 10f

class EdgeUI(
    val verticesUI: Pair<VertexUI, VertexUI>,
    private val tools: Tools,
    val edge: Edge = Edge(Pair(verticesUI.first.vertex, verticesUI.second.vertex)),
    val isDialogOpenOnCreate: Boolean = true
) {
    val edgeDialog = EdgeDialog()
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

            logger.info("gradient: $gradient")

            dislocation
        } else Offset(0f, VERTEX_SIZE / 2f)

        logger.info(
            """
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

        val offset = Offset(
            x = min(
                verticesUI.first.topLeftOffset.x,
                verticesUI.second.topLeftOffset.x,
            ),
            y = min(
                verticesUI.first.topLeftOffset.y,
                verticesUI.second.topLeftOffset.y,
            )
        )

        Surface(
            modifier = Modifier
                .offset(
                    x = offset.x.dp,
                    y = offset.y.dp
                )
                .requiredSize(
                    width = size.x.dp,
                    height = size.y.dp
                )
                .clip(EdgeShape(startQuarter, start, end, LINE_WIDTH))
                .clickable {
                    tools.notifyMe(this)
                },
//            shape = EdgeShape(startQuarter, start, end, LINE_WIDTH),
            color = Color.Red
        ) {}

        logger.debug("Offset: $offset")

        // Imagine edge ------->. weightPos - near witch part of edge weight will be drawn
        // Must be in range: (0; 1]
        val weightPos = 2 / 3f

        val centerEdgeOffset = when(startQuarter) {
            1 -> {
                Offset(
                    x = offset.x + start.x - weightPos * abs(start.x - end.x),
                    y = offset.y + start.y + weightPos * abs(start.y - end.y)
                )
            }
            2 -> {
                Offset(
                    x = offset.x + start.x + weightPos * abs(start.x - end.x),
                    y = offset.y + start.y + weightPos * abs(start.y - end.y)
                )
            }
            3 -> {
                Offset(
                    x = offset.x + start.x + weightPos * abs(start.x - end.x),
                    y = offset.y + start.y - weightPos * abs(start.y - end.y)
                )
            }
            4 -> {
                Offset(
                    x = offset.x + start.x - weightPos * abs(start.x - end.x),
                    y = offset.y + start.y - weightPos * abs(start.y - end.y)
                )
            }
            else -> {throw Error("Incorrect startQuarter value")}
        }

        val switcher = remember { mutableStateOf(false) }
        val isDialogOpen = remember { mutableStateOf(isDialogOpenOnCreate) }
        logger.debug("START: $start, END: $end, CENTER: $centerEdgeOffset")

        logger.debug("edgeDialog.text: ${edgeDialog.textState.value}")
        Text(
            text = edgeDialog.textState.value,
            color = Color.Black,
            fontSize = 18.sp,
            modifier = Modifier
                .offset(x = centerEdgeOffset.x.dp, y = centerEdgeOffset.y.dp)
                .clickable {
                    if (!tools.isAlgoStarted.value) {
                        isDialogOpen.value = true
                    }
                }
                .size(36.dp, 24.dp)
        )
        if (isDialogOpen.value == true) {
            edgeDialog.draw(isDialogOpen, switcher)
        }
        edge.weight = edgeDialog.textState.value.toInt()
        logger.debug("startTL: ${verticesUI.first.topLeftOffset}, endTL: ${verticesUI.second.topLeftOffset}")
    }
}