package application.ui.objects

import logic.Graph


class GraphUI(private val graph: Graph) {
    val verticesUI: MutableMap<VertexUI, VertexInfoUI> = mutableMapOf()

    fun addVertex(vertexUI: VertexUI) {
        graph.addVertex(vertexUI.vertex)
        verticesUI[vertexUI] = VertexInfoUI()  ////
    }
    fun addEdge(edgeUI: EdgeUI) {
        graph.addEdge(edgeUI.edge)

        verticesUI[edgeUI.verticesUI.first]!!.addOutGoingEdge(edgeUI.verticesUI.second, edgeUI)
        verticesUI[edgeUI.verticesUI.second]!!.addInputEdge(edgeUI.verticesUI.first, edgeUI)
    }

    private fun removeUIEdgesTo(vertexUI: VertexUI){
        val toDeleteEdgesInfo = verticesUI[vertexUI]
        toDeleteEdgesInfo?.outGoingEdges?.values?.forEach {
            verticesUI[it.verticesUI.second]?.inputGoingEdges?.remove(it.verticesUI.first)
        }
        toDeleteEdgesInfo?.outGoingEdges?.clear()

        toDeleteEdgesInfo?.inputGoingEdges?.values?.forEach {
            verticesUI[it.verticesUI.first]?.outGoingEdges?.remove(it.verticesUI.second)
        }
        toDeleteEdgesInfo?.inputGoingEdges?.clear()
    }

    fun removeVertex(vertexUI: VertexUI) {
        if (verticesUI.containsKey(vertexUI)) {
            val edgesUIToRemove = removeUIEdgesTo(vertexUI)
            verticesUI.remove(vertexUI)
        }
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