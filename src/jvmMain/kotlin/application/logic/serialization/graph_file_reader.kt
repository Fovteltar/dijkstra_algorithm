package logic

import application.logic.serialization.FileInfo
import application.logic.serialization.Parser
import  java.io.File
import java.io.IOException

class GraphFileReader(fileName: String) {
    var fileName = String()
    private val parser = Parser()

    init {
        this.fileName = fileName

    }

    private fun getFileStrings(): MutableList<String> {
        val lineList = mutableListOf<String>()
        File(fileName).useLines {
            for (i in it) {
                if (i.isNotBlank()) lineList.add(i.trim())
            }
        }
        return lineList
    }

    @Throws(IOException::class)
    fun getFileInformation(): FileInfo {
        parser.parse(getFileStrings())
        val graph = Graph()
        val vertexNameConformity: MutableMap<String, Vertex> = mutableMapOf()
        val coords: MutableMap<Vertex, Pair<Float, Float>> = mutableMapOf()
        val coordinatesInformation = parser.getCoordsInformation()
        coordinatesInformation.forEach { nameVertex, coordInf ->
            val currentVertex = Vertex(nameVertex)
            graph.addVertex(currentVertex)
            vertexNameConformity[nameVertex] = currentVertex
            coords[currentVertex] = Pair(coordInf.first, coordInf.second)
        }

        val startName = parser.getStartVertexName()
        val start = if(startName == null) null else vertexNameConformity[startName]

        val edges: HashSet<Pair<String, String>> = hashSetOf()
        for ((source, destination, weight) in parser.getGraphInformation()) {
            if (edges.contains(
                    Pair(
                        source,
                        destination
                    )
                )
            ) continue //если такое ребро уже есть, то остальные не учитываем
            //Проверка есть ли новая вершина в наборе соответствий вершин
            if (source !in vertexNameConformity) {
                vertexNameConformity[source] = Vertex(source)
                graph.addVertex(vertexNameConformity[source]!!)
            }
            if (destination !in vertexNameConformity) {
                vertexNameConformity[destination] = Vertex(destination)
                graph.addVertex(vertexNameConformity[destination]!!)
            }
            graph.addEdge(Edge(Pair(vertexNameConformity[source]!!, vertexNameConformity[destination]!!), weight))
            edges.add(Pair(source, destination))
            // Проверка добавления координат

            if (!coords.containsKey(vertexNameConformity[source])) {
                throw IOException("Vertex with \"$source\" name don't have coordinates")
            }
            if (!coords.containsKey(vertexNameConformity[destination])) {
                throw IOException("Vertex with \"$destination\" name don't have coordinates")
            }

        }

        return FileInfo(graph, start, coords, parser.getStateNumber())
    }
}