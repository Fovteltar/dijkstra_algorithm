import logic.Edge
import logic.Graph
import logic.Vertex
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GraphTest {
    val graph = Graph()

    @BeforeEach
    fun before() {
        val graph = Graph()
    }

    @Test
    fun addVertex_non_trivial() {

        graph.addVertex(Vertex(""))
        graph.addVertex(Vertex("cde"))
        graph.addVertex(Vertex("dd"))
        val added_vertices = graph.vertices.keys.toString()
        assertEquals("[, cde, dd]", added_vertices)
    }

    @Test
    fun addVertex() {
        graph.addVertex(Vertex("a"))
        graph.addVertex(Vertex("b"))
        graph.addVertex(Vertex("d"))
        val added_vertices = graph.vertices
        assertEquals(3, added_vertices.size)
    }

    @Test
    fun addVertex_not_correct() {
        val graph = Graph()
        graph.addVertex(Vertex("b"))
        val added_vertices = graph.vertices.keys.toString()
        assertNotEquals("[a]", added_vertices)
    }
    @Test
    fun addEdge_non_trivial() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        graph.addVertex(v1)
        graph.addVertex(v2)
        val v3 = Vertex("c")
        val edge = Edge(Pair(v3, v2), 10)
        val edges = graph.getEdges()
        val exception = Assertions.assertThrows(IndexOutOfBoundsException::class.java) {
            edges.elementAt(0).toString()
        }
    }

    @Test
    fun addEdge() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        graph.addVertex(v1)
        graph.addVertex(v2)
        graph.addEdge(Edge(Pair(v1, v2), 10))
        val edge1 = graph.getEdges()
        assertEquals("a b 10", edge1.elementAt(0).toString())
    }

    @Test
    fun addEdge_not_correct() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        graph.addVertex(v1)
        graph.addVertex(v2)
        graph.addEdge(Edge(Pair(v1, v2), 5))
        val edge1 = graph.getEdges()
        assertNotEquals("a b 10", edge1.elementAt(0).toString())
    }

    @Test
    fun removeVertex() {
        val graph = Graph()
        val v1 = Vertex("a")
        graph.addVertex(v1)
        graph.removeVertex(v1)
        val got_one = graph.vertices.keys.toString()
        assertEquals("[]", got_one)

    }

    @Test
    fun removeVertex_not_correct() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        graph.addVertex(v1)
        graph.addVertex(v2)
        graph.removeVertex(v1)
        val got_one = graph.vertices.keys.toString()
        assertNotEquals("[]", got_one)

    }
    @Test
    fun removeVertex_non_trivial() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        graph.addVertex(v1)
        graph.removeVertex(v2)
        val got_one = graph.vertices.keys.toString()
        assertNotEquals("[]", got_one)

    }

    @Test
    fun removeEdge() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        graph.addVertex(v1)
        graph.addVertex(v2)
        //graph.addEdge(Edge(Pair(v1,v2),10))
        val edge2 = Edge(Pair(v1, v2), 10)
        graph.removeEdge(edge2)
        assertEquals("[]", graph.getEdges().toString())

    }
    @Test
    fun removeEdge_not_correct() {
        val graph = Graph()
        val v1 = Vertex("a")
        val v2 = Vertex("b")
        val v3 = Vertex("c")
        val v4 = Vertex("d")
        graph.addVertex(v1)
        graph.addVertex(v2)
        graph.addVertex(v3)
        graph.addVertex(v4)
        //graph.addEdge(Edge(Pair(v1,v2),10))
        val edge1 = Edge(Pair(v1, v2), 10)
        val edge2 = Edge(Pair(v3, v4), 5)
        graph.addEdge(edge1)
        graph.removeEdge(edge2)
        assertNotEquals("[]", graph.getEdges().toString())

    }


    @Test
    fun getDestinations() {
        val vertexA = Vertex("a")
        val vertexB = Vertex("b")
        val vertexC = Vertex("c")
        val vertexD = Vertex("d")
        graph.addVertex(vertexA)
        graph.addVertex(vertexB)
        graph.addVertex(vertexC)
        graph.addVertex(vertexD)
        graph.addEdge(Edge(Pair(vertexA, vertexB), 5))
        graph.addEdge(Edge(Pair(vertexA, vertexC), 4))
        graph.addEdge(Edge(Pair(vertexA, vertexD), 6))
        assertEquals("[a b 5, a c 4, a d 6]",graph.getDestinations(vertexA).toString())
    }
    @Test
    fun getDestinations_not_correct() {
        val vertexA = Vertex("a")
        val vertexB = Vertex("b")
        val vertexC = Vertex("c")
        val vertexD = Vertex("d")
        graph.addVertex(vertexA)
        graph.addVertex(vertexB)
        graph.addVertex(vertexC)
        graph.addVertex(vertexD)
        graph.addEdge(Edge(Pair(vertexA, vertexB), 5))
        graph.addEdge(Edge(Pair(vertexA, vertexC), 4))
        graph.addEdge(Edge(Pair(vertexA, vertexD), 6))
        assertNotEquals("[a b 5, a c 4]",graph.getDestinations(vertexA).toString())
    }

    @Test
    fun getVertices() {
        val graph = Graph()
        val v1 = Vertex("a")
        graph.addVertex(v1)
        assertEquals("[a]", graph.getVertices().toString())

    }

    @Test
    fun getVertices_not_correct() {
        val graph = Graph()
        val v1 = Vertex("b")
        graph.addVertex(v1)
        assertNotEquals("[a]", graph.getVertices().toString())

    }

    @Test
    fun getEdges() {
        val vertexA = Vertex("a")
        val vertexB = Vertex("b")
        val vertexC = Vertex("c")
        val vertexD = Vertex("d")
        graph.addVertex(vertexA)
        graph.addVertex(vertexB)
        graph.addVertex(vertexC)
        graph.addVertex(vertexD)
        graph.addEdge(Edge(Pair(vertexA, vertexB), 5))
        graph.addEdge(Edge(Pair(vertexA, vertexC), 4))
        graph.addEdge(Edge(Pair(vertexA, vertexD), 6))
        assertEquals(3, graph.getEdges().size)
        val edges = graph.getEdges()
        val expected = mutableListOf<String>(
            "a b 5",
            "a c 4",
            "a d 6"
        )
        for (i in 0..edges.size-1) {
            assertEquals(expected.elementAt(i), edges.elementAt(i).toString())
        }
    }
    @Test
    fun getEdges_non_correct() {
        val vertexA = Vertex("a")
        val vertexB = Vertex("b")
        val vertexC = Vertex("c")
        val vertexD = Vertex("d")
        graph.addVertex(vertexA)
        graph.addVertex(vertexB)
        graph.addVertex(vertexC)
        graph.addVertex(vertexD)
        graph.addEdge(Edge(Pair(vertexA, vertexB), 5))
        graph.addEdge(Edge(Pair(vertexA, vertexD), 6))
        assertNotEquals(3, graph.getEdges().size)
        val edges = graph.getEdges()
        val expected = mutableListOf<String>(
            "a b 6",
            "a c 4",
        )
        for (i in 0..edges.size-1) {
            assertNotEquals(expected.elementAt(i), edges.elementAt(i).toString())
        }
    }
}