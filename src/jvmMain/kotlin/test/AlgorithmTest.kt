import application.logic.VertexInfo
import application.logic.serialization.AlgorithmUpdate
import logic.Algorithm
import logic.Edge
import logic.Graph
import logic.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

internal class AlgorithmTest {
    fun buildGraph(verticesStringFormat: MutableList<String>, edgesStringFormat: MutableMap<String, MutableMap<String, Int>>): Graph {
        val vertices = verticesStringFormat.map { it to Vertex(it) }.toMap()

        val outgoingEdges = mutableMapOf<Vertex, MutableList<Pair<Vertex, Edge>>>()
        val inputgoingEdges = mutableMapOf<Vertex, MutableList<Pair<Vertex, Edge>>>()

        vertices.values.forEach {
            outgoingEdges[it] = mutableListOf()
            inputgoingEdges[it] = mutableListOf()
        }

        edgesStringFormat.forEach {
            val it1 = it
            it.value.forEach {
                val pair = Pair(vertices[it1.key]!!, vertices[it.key]!!) to
                        Edge(
                            vertices = Pair(vertices[it1.key]!!, vertices[it.key]!!),
                            weight = it.value
                        )
                outgoingEdges[pair.first.first]!!.add(Pair(pair.first.second, pair.second))
                inputgoingEdges[pair.first.second]!!.add(Pair(pair.first.first, pair.second))
            }
        }

        val verticesInfo = mutableMapOf<Vertex, VertexInfo>()
        verticesStringFormat.forEach {
            verticesInfo[vertices[it]!!] = VertexInfo()
        }

        outgoingEdges.forEach {
            val vertex = it.key
            it.value.forEach {
                verticesInfo[vertex]!!.outGoingEdges[it.first] = it.second
            }
        }

        inputgoingEdges.forEach {
            val vertex = it.key
            it.value.forEach {
                verticesInfo[vertex]!!.inputGoingEdges[it.first] = it.second
            }
        }

        val graph = Graph(verticesInfo)

        return graph

    }

    @Test
    fun dijkstraAlgorithm_trivial() {
        val verticesStringFormat = mutableListOf(
            "a", "b", "c","d"
        )

        val edgesStringFormat = mutableMapOf(
            "a" to mutableMapOf(
                "b" to 1,
                "c" to 4,
                "d" to 10
            ),
            "b" to mutableMapOf(
                "c" to 2,
            ),
            "c" to mutableMapOf(
                "d" to 3
            )
        )
        val graph = buildGraph(verticesStringFormat, edgesStringFormat)
        val waitingValues = mutableMapOf<String, String>(
            "a" to "0.0",
            "b" to "1.0",
            "c" to "3.0",
            "d" to "6.0"
        )
        val alg = Algorithm()
        val stateMachine = alg.dijkstraAlgorithm(graph,graph.getVertices().first())
        val finalst = stateMachine.getUpdate(0)
        val update = AlgorithmUpdate(null,null)

        assertEquals(finalst,update)
    }
    @Test
    fun dijkstraAlgorithm1_non_trivial() {
        val verticesStringFormat = mutableListOf(
            "A", "B1", "B2","C1","C2","E","I","J"
        )

        val edgesStringFormat = mutableMapOf(
            "A" to mutableMapOf(
                "B1" to 7,
                "E" to 5,
            ),
            "B1" to mutableMapOf(
                "B2" to 8,
                "C1" to 3,
                "C2" to 10
            ),
            "B2" to mutableMapOf(
                "C1" to 3
            ),
            "C1" to mutableMapOf(
                "J" to 20
            ),
            "C2" to mutableMapOf(
                "E" to 1,
                "J" to 6
            ),
            "E" to mutableMapOf(
                "J" to 3
            ),
            "J" to mutableMapOf(
                "I" to 4,
                "B2" to 1
            ),
            "I" to mutableMapOf(
                "E" to 5
            ),

        )
        val graph = buildGraph(verticesStringFormat, edgesStringFormat)
        val waitingValues = mutableMapOf<String, String>(
            "A" to "0.0",
            "B1" to "7.0",
            "B2" to "9.0",
            "C1" to "10.0",
            "C2" to "17.0",
            "E" to "5.0",
            "J" to "8.0",
            "I" to "12.0",
        )
        val alg = Algorithm()
        val stateMachine = alg.dijkstraAlgorithm(graph,graph.getVertices().first())
        val finalst = stateMachine.getUpdate(3)

        val v = graph.getVertices().filter { vertex: Vertex -> vertex.vertexName == "E" }
        val update = AlgorithmUpdate(null,Pair(v[0],Pair(Float.POSITIVE_INFINITY.toInt(),5)))

        assertEquals(finalst,update)

    }
    @Test
    fun dijkstraAlgorithm_update_doesntexist() {
        val verticesStringFormat = mutableListOf(
            "a", "b", "c","d","e"
        )

        val edgesStringFormat = mutableMapOf(
            "a" to mutableMapOf(
                "b" to 1,
                "c" to 3,
            ),
            "b" to mutableMapOf(
                "c" to 2,
            ),
            "c" to mutableMapOf(
                "d" to 3
            )
        )
        val graph = buildGraph(verticesStringFormat, edgesStringFormat)
        val waitingValues = mutableMapOf<String, String>(
            "a" to "0.0",
            "b" to "1.0",
            "c" to "3.0",
            "d" to "6.0",
            "e" to "Infinity"
        )
        val alg = Algorithm()
        val stateMachine = alg.dijkstraAlgorithm(graph,graph.getVertices().first())
        val finalst = stateMachine.getUpdate(99)
        assertNull(finalst.currentVertexChange)
        assertNull(finalst.markVertexChange)

    }
    @Test
    fun dijkstraAlgorithm_no_path() {
        val verticesStringFormat = mutableListOf(
            "a", "b", "c","d","e"
        )

        val edgesStringFormat = mutableMapOf(
            "a" to mutableMapOf(
                "b" to 1,
                "c" to 3,
            ),
            "b" to mutableMapOf(
                "c" to 2,
            ),
            "c" to mutableMapOf(
                "d" to 3
            )
        )
        val graph = buildGraph(verticesStringFormat, edgesStringFormat)
        val waitingValues = mutableMapOf<String, String>(
            "a" to "0.0",
            "b" to "1.0",
            "c" to "3.0",
            "d" to "6.0",
            "e" to "Infinity"
        )
        val alg = Algorithm()
        val stateMachine = alg.dijkstraAlgorithm(graph,graph.getVertices().first())
        val finalst = stateMachine.getUpdate(stateMachine.size-1)
        val vC = graph.getVertices().filter { vertex: Vertex -> vertex.vertexName == "c" }[0]
        val vD = graph.getVertices().filter { vertex: Vertex -> vertex.vertexName == "d" }[0]
        assertEquals(AlgorithmUpdate(Pair(vC,vD),null),finalst)


    }
    }
