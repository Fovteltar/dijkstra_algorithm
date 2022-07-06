package application.ui.objects

import application.logic.Graph

class GraphUI(private val graph: Graph) {
    val verticesUI: MutableMap<VertexUI, MutableList<EdgeUI>> = mutableMapOf()
    val edgesUI: MutableMap<EdgeUI, Unit> = mutableMapOf()

    fun addVertex(vertexUI: VertexUI) {
        graph.addVertex(vertexUI.vertex)
        verticesUI[vertexUI] = mutableListOf()  ////
    }
    fun addEdge(edgeUI: EdgeUI) {
        graph.addEdge(edgeUI.edge)
        edgesUI[edgeUI] = Unit
        verticesUI[edgeUI.verticesUI.first]!!.add(edgeUI)////
    }

    fun removeVertex(vertexUI: VertexUI) {
        if (verticesUI.containsKey(vertexUI)) {
            graph.removeVertex(vertexUI.vertex)
            verticesUI.remove(vertexUI)
//            verticesUI[vertexUI]?.removeAll()   создать удаление всех рёбер
        }
    }

    fun removeEdge(edgeUI: EdgeUI) {
        if (edgesUI.containsKey(edgeUI)) {
            graph.removeEdge(edgeUI.edge)
            edgesUI.remove(edgeUI)
            verticesUI[edgeUI.verticesUI.first]!!.remove(edgeUI)        ////
        }
    }
}