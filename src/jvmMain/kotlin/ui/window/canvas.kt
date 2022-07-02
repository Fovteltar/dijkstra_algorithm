package ui.window

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import logic.Graph
import logic.SelectedTool
import logic.Tools
import logger

const val BOX_SIZE = 4000

class Canvas(val tools: Tools, val graph: Graph) {
    @Composable
    fun draw(modifier: Modifier) {
        // For scrollbars
        val verticalState = rememberScrollState(0)
        val horizontalState = rememberScrollState(0)
        // For vertices in box
        val verticesAmount = remember { mutableStateOf(0) }
        val edgesAmount = remember { mutableStateOf(0) }
        tools.subscribeVerticesAmount(verticesAmount)
        tools.subscribeEdgesAmount(edgesAmount)
        Box(
            modifier = Modifier
                .verticalScroll(verticalState)
                .horizontalScroll(horizontalState)
                .size(BOX_SIZE.dp, BOX_SIZE.dp)
                .background(color = Color.White)
                .pointerInput(Unit) {
                    detectTapGestures {
                            offset ->
                    logger.info("$offset")
                        when(tools.selectedTool) {
                            SelectedTool.ADD_VERTEX -> {
                                tools.addVertex(offset)
                            }
                            else -> {}
                        }
                    }
                }
                .composed { modifier }
        ) {
        logger.info("[CANVAS]: BOX WAS UPDATED!")

            if (verticesAmount.value > 0) {
                for (vertex in graph.vertices.keys) {
                    vertex.vertexUI.drawVertex()
                }
            }

            if (edgesAmount.value > 0) {
                for (edge in graph.edges.keys) {
                    edge.edgeUI.drawEdge()
                }
            }

            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(verticalState),
//            modifier = Modifier.align(alignment = Alignment.TopEnd)
            )
            HorizontalScrollbar(
                adapter = rememberScrollbarAdapter(horizontalState),
//            modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}
