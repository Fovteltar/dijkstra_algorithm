package application.logic

import logic.Graph
import logic.Vertex
import java.io.IOException
import java.io.PrintWriter

class GraphFileWriter(fileName: String){
    var fileName = String()
    init{
        this.fileName = fileName
    }

    @Throws(IOException::class)
    fun graphToFile(graph: Graph, start: Vertex, algorithmStep:Int): Boolean{
        val fileWriter = PrintWriter(fileName)
        fileWriter.println(start.vertexName)
        fileWriter.println(algorithmStep)
        graph.edges.forEach {
            fileWriter.println("${it.key.vertices.first.vertexName} ${it.key.vertices.second.vertexName} ${it.key.weight}")
        }
        fileWriter.close()
        return true
    }

}