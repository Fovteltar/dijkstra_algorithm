package application.ui.objects

import logic.Graph
import logic.Vertex


class GraphUI(private val graph: Graph) {
    val verticesUI: MutableMap<VertexUI, VertexInfoUI> = mutableMapOf()
    val vertexToUI = mutableMapOf<Vertex, VertexUI>()

    fun addVertex(vertexUI: VertexUI) {
        graph.addVertex(vertexUI.vertex)
        verticesUI[vertexUI] = VertexInfoUI()  ////
        vertexToUI[vertexUI.vertex] = vertexUI
    }
    fun addEdge(edgeUI: EdgeUI) {
        graph.addEdge(edgeUI.edge)

        verticesUI[edgeUI.verticesUI.first]!!.addOutGoingEdge(edgeUI.verticesUI.second, edgeUI)
        verticesUI[edgeUI.verticesUI.second]!!.addInputEdge(edgeUI.verticesUI.first, edgeUI)
    }

    private fun removeUIEdgesTo(vertexUI: VertexUI):UInt{
        var removedEdgesAmount = 0u
        val toDeleteEdgesInfo = verticesUI[vertexUI]
        toDeleteEdgesInfo?.outGoingEdges?.values?.forEach {
            verticesUI[it.verticesUI.second]?.inputGoingEdges?.remove(it.verticesUI.first)
            ++removedEdgesAmount
        }
        toDeleteEdgesInfo?.outGoingEdges?.clear()

        toDeleteEdgesInfo?.inputGoingEdges?.values?.forEach {
            verticesUI[it.verticesUI.first]?.outGoingEdges?.remove(it.verticesUI.second)
            ++removedEdgesAmount
        }
        toDeleteEdgesInfo?.inputGoingEdges?.clear()
        return removedEdgesAmount
    }

    fun removeVertex(vertexUI: VertexUI):UInt {
        if (verticesUI.containsKey(vertexUI)) {
            graph.removeVertex(vertexUI.vertex)
            val removedEdgesAmount = removeUIEdgesTo(vertexUI)
            verticesUI.remove(vertexUI)
            vertexToUI.remove(vertexUI.vertex)
            return removedEdgesAmount
        }
        return 0u
    }

    fun removeEdge(edgeUI: EdgeUI) {
        if (verticesUI[edgeUI.verticesUI.first]?.outGoingEdges?.values?.contains(edgeUI) == true) {
            graph.removeEdge(edgeUI.edge)
            verticesUI[edgeUI.verticesUI.first]?.outGoingEdges?.remove(edgeUI.verticesUI.second)
            verticesUI[edgeUI.verticesUI.second]?.inputGoingEdges?.remove(edgeUI.verticesUI.first)
        }
    }

    fun getEdges():MutableList<EdgeUI>{
        val edges:MutableList<EdgeUI> = mutableListOf()
        verticesUI.keys.forEach {
            val currentEdges = verticesUI[it]!!.getOutGoingEdges()
            edges.addAll(currentEdges)
        }
        return edges
    }

}