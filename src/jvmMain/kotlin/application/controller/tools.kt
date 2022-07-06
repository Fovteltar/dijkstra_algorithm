package application.controller

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import application.logic.Logic
import application.logic.Vertex
import application.ui.UI
import application.ui.objects.EdgeUI
import application.ui.objects.VERTEX_SIZE
import application.ui.objects.VertexUI
import application.ui.window.Canvas
import logger

enum class SelectedTool {
    NOTHING, ADD_VERTEX, REMOVE_VERTEX, ADD_EDGE, REMOVE_EDGE
}

class Tools(
    val logic: Logic,
    var ui: UI? = null
) {
    var selectedTool: SelectedTool = SelectedTool.NOTHING
    val verticesAmount = mutableStateOf(0)
    val edgesAmount = mutableStateOf(0)

    var vertexSelected = Pair<Boolean, VertexUI?>(false, null)

    @OptIn(ExperimentalComposeUiApi::class)
    fun notifyMe(sender: Any) {
        logger.info("[Tools] I was notified")
        when(sender) {
            is Pair<*, *> -> {
                if (sender.first is UI && sender.second is KeyEvent) {
                    val it = sender.second as KeyEvent
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        ui?.toolbar?.selectedButtonState?.value = SelectedTool.NOTHING
                    }
                }
                else if (sender.first is Canvas && sender.second is Offset) {
                    when(selectedTool) {
                        SelectedTool.ADD_VERTEX -> {
                            logger.info("[Tools] Canvas event, selectedTool = $selectedTool, so addVertex")
                            addVertex(sender.second as Offset)
                        }
                    }
                }
                else {
                    logger.info("[Tools] Can't response :(")
                }
            }
            is VertexUI -> {
                when (selectedTool) {
                    SelectedTool.REMOVE_VERTEX -> {
                        removeVertex(vertexUI = sender)
                    }
                    SelectedTool.ADD_EDGE -> {
                        if (!vertexSelected.first) {
                            vertexSelected = Pair(true, sender)
                        } else {
                            if (vertexSelected.second != sender) {
                                addEdge(Pair(vertexSelected.second!!, sender))
                                vertexSelected = Pair(false, null)
                            }
                        }
                        logger.info {
                            "[Tools] Vertex selected: $vertexSelected"
                        }
                    }
                    else -> {}
                }
            }
            is EdgeUI -> {
                when (selectedTool) {
                    SelectedTool.REMOVE_EDGE -> {
                        removeEdge(sender)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun addVertex(
        offset: Offset,
    ): Boolean {
        // center offset -> top-left offset
        val topLeftOffset = Offset(x = offset.x - VERTEX_SIZE / 2, y = offset.y - VERTEX_SIZE / 2)
        val vertexUI = VertexUI(vertex = Vertex(), tools = this)
        vertexUI.topLeftOffset = topLeftOffset
        ui!!.graphUI.addVertex(vertexUI)
        verticesAmount.value += 1
        logger.info("[Tools] Vertex was added with offset: $offset")
        return true
    }

    private fun removeVertex(
        vertexUI: VertexUI,
    ): Boolean {
        return try {
            ui!!.graphUI.removeVertex(vertexUI)
            verticesAmount.value -= 1
            true
        } catch (exception: Exception) {
            logger.info(exception.message)
            false
        }
    }

    private fun addEdge(verticesUI: Pair<VertexUI, VertexUI>): Boolean {
        return try {
            val edgeUI = EdgeUI(verticesUI = verticesUI, tools = this)
            ui!!.graphUI.addEdge(edgeUI)
            edgesAmount.value += 1
            true
        } catch (exception: Exception) {
            logger.info(exception.message)
            false
        }
    }

    private fun removeEdge(edgeUI: EdgeUI): Boolean {
        return try {
            ui!!.graphUI.removeEdge(edgeUI)
            edgesAmount.value -= 1
            true
        } catch (exception: Exception) {
            logger.info(exception.message)
            false
        }
    }
}