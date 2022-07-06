package application.logic

class Graph(
    val vertices: MutableMap<Vertex, MutableList<Edge>> = mutableMapOf(),////
    val edges: MutableMap<Edge, Unit> = mutableMapOf()
) {
    fun addVertex(vertex: Vertex) {
        vertices[vertex] = mutableListOf()  ////
    }
    fun addEdge(edge: Edge) {
        edges[edge] = Unit
        vertices[edge.vertices?.first]!!.add(edge)////
    }

    fun removeVertex(vertex: Vertex) {
        if (vertices.containsKey(vertex)) {
            vertices.remove(vertex)
//            vertices[vertex]?.removeAll()   создать удаление всех рёбер
        }
    }

    fun removeEdge(edge: Edge) {
        if (edges.containsKey(edge)) {
            edges.remove(edge)
            vertices[edge.vertices?.first]!!.remove(edge)        ////
        }
    }
}