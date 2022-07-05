package logic

class Graph(
    val vertices: MutableMap<Vertex, MutableMap<Edge, Unit>> = mutableMapOf(),
    val edges: MutableMap<Edge, Unit> = mutableMapOf()
) {
    fun addVertex(vertex: Vertex) {
        vertices[vertex] = mutableMapOf()  //init
    }
    fun addEdge(edge: Edge) {
        edges[edge] = Unit
        vertices[edge.vertices.first]?.set(edge, Unit)
    }

    fun removeVertex(vertex: Vertex) {
        if (vertices.containsKey(vertex)) {
            val toDeleteEdges = vertices[vertex]
            toDeleteEdges?.forEach {
                vertices[vertex]?.remove(it.key)
                edges.remove(it.key)
            }


        }
    }

    fun removeEdge(edge: Edge) {
        if (edges.containsKey(edge)) {
            edges.remove(edge)
            vertices[edge.vertices.first]!!.remove(edge)
        }
    }
}