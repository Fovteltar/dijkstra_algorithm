package logic

import application.logic.VertexInfo

class Graph(
    val vertices: MutableMap<Vertex, VertexInfo> = mutableMapOf(),
) {
    fun addVertex(vertex: Vertex) {
        vertices[vertex] = VertexInfo()  //init
    }
    fun addEdge(edge: Edge) {
        if(vertices.containsKey(edge.vertices.first) && vertices.containsKey(edge.vertices.second)){
            vertices[edge.vertices.first]!!.addOutGoingEdge(edge.vertices.second, edge)
            vertices[edge.vertices.second]!!.addInputEdge(edge.vertices.first, edge)
        }
    }

    private fun removeEdgesTo(vertex:Vertex){
        val toDeleteEdgesInfo = vertices[vertex]

        toDeleteEdgesInfo?.outGoingEdges?.values?.forEach {
            vertices[it.vertices.second]?.inputGoingEdges?.remove(it.vertices.first)
        }
        toDeleteEdgesInfo?.outGoingEdges?.clear()

        toDeleteEdgesInfo?.inputGoingEdges?.values?.forEach {
            vertices[it.vertices.first]?.outGoingEdges?.remove(it.vertices.second)
        }
        toDeleteEdgesInfo?.inputGoingEdges?.clear()
    }

    fun removeVertex(vertex: Vertex) {
        if (vertices.containsKey(vertex)) {
            removeEdgesTo(vertex)
            vertices.remove(vertex)
        }
    }

    fun removeEdge(edge: Edge) {
        if (vertices[edge.vertices.first]?.outGoingEdges?.values?.contains(edge) == true){
            vertices[edge.vertices.first]?.outGoingEdges?.remove(edge.vertices.second)
            vertices[edge.vertices.second]?.inputGoingEdges?.remove(edge.vertices.first)
        }
    }

    fun getDestinations(vertex:Vertex):MutableSet<Edge>?{
        if(vertex in vertices.keys){
            val dests:MutableSet<Edge> = mutableSetOf()
            vertices[vertex]?.outGoingEdges?.values?.forEach {
                dests.add(it)
            }
          return dests
        }
        return null
    }

    fun getVertices():MutableSet<Vertex>{
        return vertices.keys
    }
}