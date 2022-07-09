package application.controller

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import application.logic.Logic
import application.ui.UI
import application.ui.objects.EdgeUI
import application.ui.objects.VERTEX_SIZE
import application.ui.objects.VertexUI
import application.ui.window.Canvas
import application.ui.window.Toolbar
import logger
import logic.Algorithm
import logic.StateMachine
import logic.Vertex

enum class SelectedTool {
    NOTHING, ADD_VERTEX, REMOVE_VERTEX, ADD_EDGE, REMOVE_EDGE, START_ALGORITHM
}

class Tools(
    val logic: Logic,
    var ui: UI? = null
) {
    var selectedTool: SelectedTool = SelectedTool.NOTHING
    val verticesAmount = mutableStateOf(0)
    val edgesAmount = mutableStateOf(0)

    private var vertexSelected = Pair<Boolean, VertexUI?>(false, null)

    private val algorithm = Algorithm()

    private lateinit var stateMachine: StateMachine
    var stateIndex = 0u
    val maxStateIndex: UInt
        get() {
            return stateMachine.size.toUInt() - 1u
        }
    val stateNow: Pair<MutableMap<Vertex, String>, Vertex>?
        get() {
            return stateMachine.getState(stateIndex.toInt())
        }

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
                else if (sender.first is Toolbar && sender.second is String) {
                    when (sender.second) {
                        "previousState" -> {
                            if (stateIndex >= 1u) {
                                stateIndex--
                                ui?.graphUI?.verticesUI?.keys?.forEach {
                                    it.weightInAlgorithmState.value = stateNow?.first?.get(it.vertex).toString()
                                }
                            }
                        }
                        "nextState" -> {
                            if (stateIndex + 1u <= maxStateIndex) {
                                stateIndex++
                                ui?.graphUI?.verticesUI?.keys?.forEach {
                                    it.weightInAlgorithmState.value = stateNow?.first?.get(it.vertex).toString()
                                }
                            }
                        }
                        else -> {
                            logger.info("[Tools] Can't response :(" +
                                    "\nsender = $sender")
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
                    SelectedTool.START_ALGORITHM -> {
                        stateMachine = algorithm.dijkstraAlgorithm(graph = logic.graph, start = sender.vertex)
                        ui?.graphUI?.verticesUI?.keys?.forEach { it.isAlgoStartedState.value = true}
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

    private fun addVertex(offset: Offset): Boolean {
        // center offset -> top-left offset
        val topLeftOffset = Offset(x = offset.x - VERTEX_SIZE / 2, y = offset.y - VERTEX_SIZE / 2)
        val vertexUI = VertexUI(vertex = Vertex(), tools = this)
        vertexUI.topLeftOffset = topLeftOffset
        ui!!.graphUI.addVertex(vertexUI)
        verticesAmount.value += 1
//        logger.info("[Tools] Vertex was added with offset: $offset")
        return true
    }

    private fun removeVertex(vertexUI: VertexUI): Boolean {
        return try {
            val removedEdgesAmount = ui!!.graphUI.removeVertex(vertexUI)
            verticesAmount.value -= 1
            edgesAmount.value -= removedEdgesAmount.toInt()
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