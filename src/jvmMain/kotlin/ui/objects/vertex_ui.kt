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
import androidx.compose.ui.unit.dp
import logger
import logic.SelectedTool
import logic.Vertex
import ui.window.Canvas

const val VERTEX_SIZE = 100

class VertexUI(val vertex: Vertex) {
    var topLeftOffset: Offset = Offset(0f, 0f)

    // subscriber
    var canvas: Canvas? = null
    @Composable
    fun drawVertex() {
        Canvas(
            modifier = Modifier
                .offset(x = topLeftOffset.x.dp, y = topLeftOffset.y.dp)
                .size(VERTEX_SIZE.dp, VERTEX_SIZE.dp)
                .clip(shape = CircleShape)
                .clickable {
                    val tools = canvas?.tools
                    val graph = canvas?.graph
                    when (tools?.selectedTool) {
                        SelectedTool.REMOVE_VERTEX -> {
                            tools.removeVertex(vertex = vertex)
                        }
                        SelectedTool.ADD_EDGE -> {
                            if (!tools.vertexSelected.first) {
                                tools.vertexSelected = Pair(true, vertex)
                            } else {
                                if (tools.vertexSelected.second != vertex) {
                                    tools.addEdge(Pair(tools.vertexSelected.second!!, vertex))
                                    tools.vertexSelected = Pair(false, null)
                                }
                            }
                            logger.info {
                                "Vertex selected: ${tools.vertexSelected}" +
                                        " All edges: ${graph?.edges}"
                            }
                        }
                        else -> {}
                    }
                },
        ) {
            drawCircle(Color.Blue, VERTEX_SIZE / 2f)
        }
    }

    fun subscribeCanvas(canvas: Canvas) {
        this.canvas = canvas
    }
}