package application.controller

import GraphFileWriter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import application.logic.Logic
import application.logic.serialization.FileInfo
import application.logic.serialization.State
import application.ui.UI
import application.ui.objects.EdgeUI
import application.ui.objects.VERTEX_SIZE
import application.ui.objects.VertexColor
import application.ui.objects.VertexUI
import application.ui.window.Canvas
import application.ui.window.Toolbar
import logger
import logic.*

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

    private var stateMachine: StateMachine? = null
    var stateIndex: UInt? = null
    val maxStateIndex: UInt?
        get() {
            return stateMachine?.size?.toUInt()?.minus(1u)
        }
    val stateNow: State?
        get() {
            return stateMachine?.getState(stateIndex!!.toInt())
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
                            onPreviousState()
                        }
                        "nextState" -> {
                            onNextState()
                        }
                        "skip" -> {
                            onSkip()
                        }
                        "save" -> {
                            onSave()
                        }
                        "load" -> {
                            onLoad()
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
                        startAlgorithm(sender)
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

    private fun addVertex(offset: Offset, vertex: Vertex = Vertex()): Boolean {
        // center offset -> top-left offset
        val topLeftOffset = Offset(x = offset.x - VERTEX_SIZE / 2, y = offset.y - VERTEX_SIZE / 2)
        val vertexUI = VertexUI(vertex = vertex, tools = this)
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

    private fun addEdge(verticesUI: Pair<VertexUI, VertexUI>, weight: UInt = 1u): Boolean {
        return try {
            val edgeUI = EdgeUI(
                verticesUI = verticesUI,
                tools = this,
                edge = Edge(
                    Pair(
                        first = verticesUI.first.vertex,
                        second = verticesUI.second.vertex
                    ),
                    weight = weight.toInt()
                )
            )
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

    private fun onPreviousState() {
        if (stateIndex != null) {
            if (stateIndex!! >= 1u) {
                stateIndex = stateIndex!! - 1u
                val currentVertex = stateNow?.currentVertex
                val vertexToCost = stateNow?.vertexToCost

                ui?.graphUI?.verticesUI?.keys?.forEach {
                    if (it.colorState.value == VertexColor.WAS_LOOKED) {
                        it.colorState.value = VertexColor.LOOKING
                    }

                    if (it.weightInAlgorithmState.value != (vertexToCost?.get(it.vertex) ?: -1)) {
                        it.weightInAlgorithmState.value = stateNow?.vertexToCost?.get(it.vertex).toString()

                        when (it.colorState.value) {
                            VertexColor.FIXED, VertexColor.LOOKING -> {
                                it.colorState.value = VertexColor.DEFAULT
                            }
                        }
                    }
                }
            }
        }
    }

    private fun onNextState() {
        if (stateIndex != null) {
            if (stateIndex!! + 1u <= if (maxStateIndex != null) maxStateIndex!! else 0u) {
                stateIndex = stateIndex!! + 1u
                val currentVertex = stateNow?.currentVertex
                val vertexToCost = stateNow?.vertexToCost

                ui?.graphUI?.verticesUI?.keys?.forEach {
                    if (it.vertex == currentVertex) {
                        it.colorState.value = VertexColor.FIXED
                    }

                    if (it.weightInAlgorithmState.value != (vertexToCost?.get(it.vertex) ?: -1)) {
                        it.weightInAlgorithmState.value = vertexToCost?.get(it.vertex).toString()
                        it.colorState.value = VertexColor.LOOKING
                    }
                    else {
                        if (it.colorState.value != VertexColor.FIXED) {
                            if (it.colorState.value == VertexColor.LOOKING) {
                                it.colorState.value = VertexColor.WAS_LOOKED
                            }
                            else {
                                it.colorState.value = VertexColor.DEFAULT
                            }
                        }
                    }
                }
                logger.info("$stateNow")
            }

        }
    }

    private fun startAlgorithm(sender: VertexUI) {
        stateMachine = algorithm.dijkstraAlgorithm(graph = logic.graph, start = sender.vertex)
        stateIndex = 0u

        ui?.graphUI?.verticesUI?.keys?.forEach { it.isAlgoStartedState.value = true}
        val currentVertex = stateNow?.currentVertex
        val vertexToCost = stateNow?.vertexToCost

        ui?.graphUI?.verticesUI?.keys?.forEach {
            if (it.vertex == currentVertex) {
                it.colorState.value = VertexColor.FIXED
            }

            if (it.weightInAlgorithmState.value != (vertexToCost?.get(it.vertex) ?: -1)) {
                it.weightInAlgorithmState.value = vertexToCost?.get(it.vertex).toString()
            }
        }
    }

    private fun onSkip() {
        if (stateMachine != null) {
            while (stateIndex != maxStateIndex) {
                onNextState()
            }
        }
    }

    private fun onSave() {
        val start = stateMachine?.getState(0)?.currentVertex
        val gfw = GraphFileWriter("test.txt")

        val coords =
            ui?.graphUI?.verticesUI?.keys?.map { it.vertex to if (true) Pair(it.topLeftOffset.x, it.topLeftOffset.y) else null}?.toMap()?.toMutableMap()

        gfw.toFile(
            fileInfo = FileInfo(
                graph = logic.graph,
                start = start!!,
                coords = coords,
                stateNumber = stateIndex?.toInt()
            )
        )
    }

    private fun onLoad() {
        val gfr = GraphFileReader("test.txt")
        val fileInfo = gfr.graphFromFile()
        if (fileInfo.coords != null && fileInfo.stateNumber != null) {
            ui?.graphUI?.verticesUI?.keys?.forEach {
                removeVertex(it)
            }
            lateinit var startUI: VertexUI
            fileInfo.coords.forEach {
                addVertex(vertex = it.key, offset = Offset(x = it.value!!.first, y = it.value!!.second))
            }

            fileInfo.graph.getEdges().forEach {
                addEdge(
                    Pair(
                        first = ui!!.graphUI.vertexToUI[it.vertices.first]!!,
                        second = ui!!.graphUI.vertexToUI[it.vertices.first]!!
                    ),
                    weight = it.weight.toUInt()
                )
            }

            stateIndex = fileInfo.stateNumber.toUInt()
            startAlgorithm(ui?.graphUI?.vertexToUI?.get(fileInfo.start)!!)
        }
    }
}