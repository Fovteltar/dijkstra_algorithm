package logic

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import ui.objects.VERTEX_SIZE
import ui.window.BOX_SIZE
import ui.window.Canvas
import logger

enum class SelectedTool {
    NOTHING, ADD_VERTEX, REMOVE_VERTEX, ADD_EDGE, REMOVE_EDGE
}

class Tools(
    var selectedTool: SelectedTool = SelectedTool.NOTHING,
    val graph: Graph,
    var canvas: Canvas? = null
) {
    lateinit var verticesAmount: MutableState<Int>
    lateinit var edgesAmount: MutableState<Int>
    var vertexSelected = Pair<Boolean, Vertex?>(false, null)
    fun addVertex(
         offset: Offset,
    ): Boolean {
        // center offset -> top-left offset
         val topLeftOffset = Offset(x = offset.x - VERTEX_SIZE / 2, y = offset.y - VERTEX_SIZE / 2)
         if (topLeftOffset.x >= 0 &&
             topLeftOffset.y >= 0 &&
             topLeftOffset.x <= BOX_SIZE - VERTEX_SIZE &&
             topLeftOffset.y <= BOX_SIZE - VERTEX_SIZE
         ) {
             val vertex = Vertex()
             vertex.vertexUI.topLeftOffset = topLeftOffset
             vertex.vertexUI.subscribeCanvas(canvas!!)
             graph.addVertex(vertex)
             verticesAmount.value += 1
             logger.info("[Tools] Vertex was added with offset: $offset")
             return true
         }
         else {
             logger.info("[Tools] SELECTED OFFSET CAN'T BE USED FOR CREATING VERTEX")
             return false
         }
     }

    fun removeVertex(
        vertex: Vertex,
    ): Boolean {
        return try {
            graph.removeVertex(vertex)
            verticesAmount.value -= 1
            true
        } catch (exception: Exception) {
            logger.info(exception.message)
            false
        }
    }

    fun addEdge(vertices: Pair<Vertex, Vertex>): Boolean {
        return try {
            val edge = Edge(vertices)
            edge.edgeUI.subscribeCanvas(canvas!!)
            graph.addEdge(edge)
            edgesAmount.value += 1
            true
        } catch (exception: Exception) {
            logger.info(exception.message)
            false
        }
    }

    fun removeEdge(edge: Edge): Boolean {
        return try {
            graph.removeEdge(edge)
            edgesAmount.value -= 1
            true
        } catch (exception: Exception) {
            logger.info(exception.message)
            false
        }
    }

    fun subscribeVerticesAmount(verticesAmount: MutableState<Int>) {
        this.verticesAmount = verticesAmount
    }

    fun subscribeEdgesAmount(edgesAmount: MutableState<Int>) {
        this.edgesAmount = edgesAmount
    }
}