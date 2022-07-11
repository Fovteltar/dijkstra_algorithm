package application.controller

import GraphFileWriter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.*
import application.logic.Logic
import application.logic.serialization.AlgorithmUpdate
import application.logic.serialization.FileInfo
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
    NOTHING, ADD_VERTEX, REMOVE_VERTEX, ADD_EDGE, REMOVE_EDGE
}

class Tools(
    val logic: Logic,
    var ui: UI? = null
) {
    var selectedTool: SelectedTool = SelectedTool.NOTHING
    val verticesAmount = mutableStateOf(0)
    val edgesAmount = mutableStateOf(0)
    val isAlgoStarted = mutableStateOf(false)

    private var vertexSelected = Pair<Boolean, VertexUI?>(false, null)

    private val algorithm = Algorithm()

    private var stateMachine: StateMachine? = null
    private var stateIndex: UInt? = null
    private val maxStateIndex: UInt?
        get() {
            return stateMachine?.size?.toUInt()?.minus(1u)
        }
    private val updatesNow: AlgorithmUpdate?
        get() {
            return stateIndex?.toInt()?.let { stateMachine?.getUpdate(it) }
        }

    private var outdatedLookingVertex: VertexUI? = null

    @OptIn(ExperimentalComposeUiApi::class)
    fun notifyMe(sender: Any) {
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
                        "changedAlgoState" -> {
                            if (!isAlgoStarted.value) {
                                algoReset()
                            }
                        }
                        else -> {
                            logger.info("Can't response :(" +
                                    "\nsender = $sender")
                        }
                    }
                }
                else {
                    logger.info("Can't response :(")
                }
            }
            is VertexUI -> {
                if (isAlgoStarted.value) {
                    logger.info("Start algorithm")
                    startAlgorithm(sender)
                }
                else {
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
                                "Vertex selected: $vertexSelected"
                            }
                        }
                        else -> {}
                    }
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

    private fun addVertex(offset: Offset, vertex: Vertex = Vertex(), needToMove: Boolean = true): Boolean {
        // center offset -> top-left offset
        val topLeftOffset = Offset(x = offset.x - VERTEX_SIZE / 2, y = offset.y - VERTEX_SIZE / 2)
        val vertexUI = VertexUI(vertex = vertex, tools = this)
        if (needToMove) {
            vertexUI.topLeftOffset = topLeftOffset
        }
        else {
            vertexUI.topLeftOffset = offset
        }
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
        outdatedLookingVertex = null
        if (stateIndex != null) {
            if (stateIndex!! >= 1u) {
//                wasLookedVertices.keys.forEach {
//                    if (it.colorState.value != VertexColor.WAS_LOOKED) {
//                        it.colorState.value = VertexColor.WAS_LOOKED
//                    }
//                }

                val (newColor, newVertexUI) = this.let {
                    val currentVertex = updatesNow?.currentVertexChange
                    val vertexToCost = updatesNow?.markVertexChange

                    if (currentVertex != null) {
                        val vertexUI = ui?.graphUI?.vertexToUI?.get(currentVertex.second)
                        Pair(VertexColor.WAS_LOOKED, vertexUI)
                    }
                    else if(vertexToCost != null) {
                        val vertexUI = ui?.graphUI?.vertexToUI?.get(vertexToCost.first)
                        if (vertexToCost.second.first == Int.MAX_VALUE) {
                            vertexUI?.weightInAlgorithmState?.value = "∞"
                            Pair(VertexColor.DEFAULT, vertexUI)
                        }
                        else {
                            vertexUI?.weightInAlgorithmState?.value = vertexToCost.second.first.toString()
                            Pair(VertexColor.WAS_LOOKED, vertexUI)
                        }
                    }
                    else {
                        Pair(VertexColor.DEFAULT, null)
                    }
                }
                if (newVertexUI != null) {
                    newVertexUI.colorState.value = newColor
                }

                stateIndex = stateIndex!! - 1u
                val (prevColor, prevVertexUI) = this.let {
                    val currentVertex = updatesNow?.currentVertexChange
                    val vertexToCost = updatesNow?.markVertexChange

                    if (currentVertex != null) {
                        val vertexUI = ui?.graphUI?.vertexToUI?.get(currentVertex.second)
                        Pair(VertexColor.FIXED, vertexUI)
                    }
                    else if(vertexToCost != null) {
                        val vertexUI = ui?.graphUI?.vertexToUI?.get(vertexToCost.first)
                        if (vertexUI != null) {
                            outdatedLookingVertex = vertexUI
                        }
                        Pair(VertexColor.LOOKING, vertexUI)
                    }
                    else {
                        newVertexUI?.weightInAlgorithmState?.value = "∞"
                        Pair(VertexColor.DEFAULT, newVertexUI)
                    }
                }

                if (prevVertexUI != null) {
                    prevVertexUI.colorState.value = prevColor
                }

                if (logger.isDebugEnabled) {
                    logger.debug("$updatesNow")
                }
            }
        }
    }

    private fun onNextState() {
        if (stateIndex != null) {
            if (stateIndex!! + 1u <= if (maxStateIndex != null) maxStateIndex!! else 0u) {
                stateIndex = stateIndex!! + 1u
                val currentVertex = updatesNow?.currentVertexChange
                val vertexToCost = updatesNow?.markVertexChange

                if (logger.isDebugEnabled) {
                    logger.debug("$updatesNow")
                }

                if(outdatedLookingVertex != null) {
                    outdatedLookingVertex?.colorState?.value = VertexColor.WAS_LOOKED
                    outdatedLookingVertex = null
                }

                if (currentVertex != null) {
                    val vertexUI = ui?.graphUI?.vertexToUI?.get(currentVertex.second)
                    if (stateIndex == 1u) {
                        vertexUI?.weightInAlgorithmState?.value = "0"
                    }
                    vertexUI?.colorState?.value = VertexColor.FIXED
                }
                else if(vertexToCost != null) {
                    val vertexUI = ui?.graphUI?.vertexToUI?.get(vertexToCost.first)
                    if (vertexUI != null) {
                        outdatedLookingVertex = vertexUI
                        vertexUI.weightInAlgorithmState.value = vertexToCost.second.second.toString()
                        vertexUI.colorState.value = VertexColor.LOOKING
                        outdatedLookingVertex = vertexUI
                        if (stateIndex == maxStateIndex) {
                            vertexUI.colorState.value = VertexColor.FIXED
                        }
                    }
                }
            }
        }
    }

    private fun startAlgorithm(sender: VertexUI) {
        stateMachine = algorithm.dijkstraAlgorithm(graph = logic.graph, start = sender.vertex)
        stateIndex = 0u

        ui?.graphUI?.verticesUI?.keys?.forEach {
            it.weightInAlgorithmState.value = "∞"
            it.colorState.value = VertexColor.DEFAULT
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
        val start = stateMachine?.getUpdate(1)?.currentVertexChange?.second
        val gfw = GraphFileWriter("test.txt")

        val coords =
            ui?.graphUI?.verticesUI?.keys?.map { it.vertex to Pair(it.topLeftOffset.x, it.topLeftOffset.y)}?.toMap()?.toMutableMap()

        if (coords == null) {
            logger.info("Trying to save no coords")
        }
        else {
            gfw.toFile(
                fileInfo = FileInfo(
                    graph = logic.graph,
                    start = start,
                    coords = coords,
                    stateNumber = stateIndex?.toInt()
                )
            )
        }
    }

    private fun onLoad() {
        logger.info("Trying to read the file \"test.txt\"")
        try {
            val gfr = GraphFileReader("test.txt")
            val fileInfo = gfr.getFileInformation()

            algoReset()

            ui?.graphUI?.verticesUI?.keys?.forEach {
                removeVertex(it)
            }

            println("fileInfo: $fileInfo")

            fileInfo.coords.forEach {
                println("${it.key}")
                addVertex(vertex = it.key, offset = Offset(x = it.value.first, y = it.value.second), needToMove = false)
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

            stateIndex = fileInfo.stateNumber?.toUInt()
            if (stateIndex != null) {
                startAlgorithm(ui?.graphUI?.vertexToUI?.get(fileInfo.start)!!)
            }
            logger.info("Successful read file \"test.txt\"")
        }
        catch(exc: Exception) {
            logger.error(exc) {
                "Broken file: ${exc.message}"
            }
        }

    }

    private fun algoReset() {
        ui?.graphUI?.verticesUI?.keys?.forEach {
            it.weightInAlgorithmState.value = ""
            it.colorState.value = VertexColor.DEFAULT
        }
        stateMachine = null
        stateIndex = null
    }
}