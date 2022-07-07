package application.ui.objects

import logic.Graph


class GraphUI(private val graph: Graph) {
    val verticesUI: MutableMap<VertexUI, MutableMap<EdgeUI, Unit>> = mutableMapOf()
    val edgesUI: MutableMap<EdgeUI, Unit> = mutableMapOf()

    fun addVertex(vertexUI: VertexUI) {
        verticesUI[vertexUI] = mutableMapOf()  ////
        graph.addVertex(vertexUI.vertex)
    }
    fun addEdge(edgeUI: EdgeUI) {
        edgesUI[edgeUI] = Unit
        verticesUI[edgeUI.verticesUI.first]?.set(edgeUI, Unit)
        graph.addEdge(edgeUI.edge)
    }

    // Возвращает кол-во удаленных ребер
    fun removeVertex(vertexUI: VertexUI): UInt {
        var removedEdgesAmount = 0u
        if (verticesUI.containsKey(vertexUI)) {
            val toDeleteEdges = verticesUI[vertexUI]
            toDeleteEdges?.forEach {
                verticesUI[vertexUI]?.remove(it.key)
                edgesUI.remove(it.key)
                removedEdgesAmount++
            }
            verticesUI.remove(vertexUI)
            graph.removeVertex(vertexUI.vertex)
        }
        return removedEdgesAmount
    }

    fun removeEdge(edgeUI: EdgeUI) {
        if (edgesUI.containsKey(edgeUI)) {
            edgesUI.remove(edgeUI)
            verticesUI[edgeUI.verticesUI.first]!!.remove(edgeUI)        ////
            graph.removeEdge(edgeUI.edge)
        }
    }
}