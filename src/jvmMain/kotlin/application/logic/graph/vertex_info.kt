package application.logic

import logic.Edge
import logic.Vertex

class VertexInfo(val outGoingEdges:MutableMap<Vertex, Edge> = mutableMapOf(),
                 val inputGoingEdges:MutableMap<Vertex, Edge> = mutableMapOf()) {

    fun addOutGoingEdge(vertex:Vertex, edge:Edge){
        outGoingEdges[vertex] = edge
    }
    fun addInputEdge(vertex:Vertex, edge:Edge){
        inputGoingEdges[vertex] = edge
    }

}