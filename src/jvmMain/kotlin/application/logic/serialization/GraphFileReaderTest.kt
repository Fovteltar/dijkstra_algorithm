package application.logic.serialization

import logic.Edge
import logic.Graph
import logic.GraphFileReader
import logic.Vertex
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class GraphFileReaderTest {

    @Test
    fun getFileInformation() {
        val graph_info = GraphFileReader("test.txt")
        val grrrr = graph_info.getFileInformation().graph
        val graph1 = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        val v3 = Vertex("c")
        graph1.addVertex(v1)
        graph1.addVertex(v2)
        graph1.addVertex(v3)
        graph1.addEdge(Edge(Pair(v1, v2), 1))
        graph1.addEdge(Edge(Pair(v1, v3), 4))
        graph1.addEdge(Edge(Pair(v2, v3), 2))
        assertEquals(graph1.vertices.keys.toString(),grrrr.vertices.keys.toString())

    }
    @Test
    fun getFileInformation_not_correct() {
        val graph_info = GraphFileReader("test.txt")
        val grrrr = graph_info.getFileInformation().graph
        val graph1 = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("d")
        val v3 = Vertex("c")
        graph1.addVertex(v1)
        graph1.addVertex(v2)
        graph1.addVertex(v3)
        graph1.addEdge(Edge(Pair(v1, v2), 1))
        graph1.addEdge(Edge(Pair(v1, v3), 4))
        graph1.addEdge(Edge(Pair(v2, v3), 2))
        assertNotEquals(graph1.vertices.keys.toString(),grrrr.vertices.keys.toString())

        //   assertEquals(graph1.)
        //   assertEquals(0,graph_info.getFileInformation().graph)
    }
}