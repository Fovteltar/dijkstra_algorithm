package application.ui.objects


class VertexInfoUI {
    val outGoingEdges:MutableMap<VertexUI, EdgeUI> = mutableMapOf()
    val inputGoingEdges:MutableMap<VertexUI, EdgeUI> = mutableMapOf()

    fun addOutGoingEdge(vertex:VertexUI, edge:EdgeUI){
        outGoingEdges[vertex] = edge
    }
    fun addInputEdge(vertex:VertexUI, edge:EdgeUI){
        inputGoingEdges[vertex] = edge
    }

    fun getOutGoingEdges():MutableList<EdgeUI>{
        return outGoingEdges.values.toMutableList()
    }
    fun getInputEdge():MutableList<EdgeUI>{
        return inputGoingEdges.values.toMutableList()
    }
}