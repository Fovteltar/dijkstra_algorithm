package logic

class Graph(
    val vertices: MutableMap<Vertex, Unit> = mutableMapOf(),
    val edges: MutableMap<Edge, Unit> = mutableMapOf()
) {
    fun addVertex(vertex: Vertex) {
        vertices[vertex] = Unit
    }
    fun addEdge(edge: Edge) {
        edges[edge] = Unit
    }

    fun removeVertex(vertex: Vertex) {
        if (vertices.containsKey(vertex)) {
            vertices.remove(vertex)
        }
    }

    fun removeEdge(edge: Edge) {
        if (edges.containsKey(edge)) {
            edges.remove(edge)
        }
    }
}