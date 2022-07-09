package logic

import application.logic.serialization.FileInfo
import application.logic.serialization.Parser
import  java.io.File
import java.io.IOException

class GraphFileReader(fileName: String) {
    var fileName = String()
    val parser = Parser()

    init {
        this.fileName = fileName

    }

    private fun getFileStrings(): MutableList<String> {
        val lineList = mutableListOf<String>()
        File(fileName).useLines {
            for (i in it) {
                if (i.isNotBlank())lineList.add(i.trim())
            }
        }
        return lineList
    }

    @Throws(IOException::class)
    fun graphFromFile(): FileInfo {
        parser.parse(getFileStrings())
        val start = Vertex(parser.getStartVertexName())
        val graph = Graph()
        graph.addVertex(start)
        val coordinatesInformation = parser.getCoordsInformation()
        val coords:MutableMap<Vertex, Pair<Float, Float>?> = mutableMapOf()
        val vertexNameConformity: MutableMap<String, Vertex> = mutableMapOf()
        vertexNameConformity[start.vertexName] = start

        val edges:HashSet<Pair<String, String>> = hashSetOf()
        for ((source, destination, weight) in parser.getGraphInformation()){
            if (edges.contains(Pair(source, destination))) continue //если такое ребро уже есть, то остальные не учитываем
            //Проверка есть ли новая вершина в наборе соответствий вершин
            if (source !in vertexNameConformity){
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
            if(coordinatesInformation != null){ // если координаты заданы
                if(!coords.containsKey(vertexNameConformity[source])){
                    if(coordinatesInformation.containsKey(source)) {
                        coords[vertexNameConformity[source]!!] = coordinatesInformation[source]!!
                    }
                    else coords[vertexNameConformity[source]!!] = null
                }
                if(!coords.containsKey(vertexNameConformity[destination]) ){
                    if(coordinatesInformation.containsKey(destination)) {
                        coords[vertexNameConformity[destination]!!] = coordinatesInformation[destination]!!
                    }
                    else coords[vertexNameConformity[destination]!!] = null
                }
            }
        }
        return FileInfo(graph, start, coords, parser.getStateNumber())
    }
}