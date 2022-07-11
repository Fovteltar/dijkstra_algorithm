import application.logic.serialization.FileInfo
import application.logic.serialization.KeyWords
import application.logic.serialization.TextKeyWords
import logic.Edge
import logic.Vertex
import java.io.File
import java.io.IOException
import java.io.PrintWriter

class GraphFileWriter(private val fileName: String){
    private val textKeyWords:TextKeyWords = TextKeyWords()


    private fun edgesToFile(edges: MutableSet<Edge>, filePrinter:PrintWriter){
        filePrinter.println(textKeyWords.getKeyWordString(KeyWords.GRAPH) + textKeyWords.blockStart)
        edges.forEach {
            filePrinter.println("\t${it.vertices.first.vertexName} ${it.vertices.second.vertexName} ${it.weight}")
        }
        filePrinter.println(textKeyWords.blockEnd)
    }

    private fun startToFile(vertex:Vertex, filePrinter:PrintWriter){
        filePrinter.println(textKeyWords.getKeyWordString(KeyWords.START) +"=" + vertex.vertexName)
    }

    private fun coordsToFile(coords:MutableMap<Vertex, Pair<Float, Float>>?, filePrinter:PrintWriter){
        filePrinter.println(textKeyWords.getKeyWordString(KeyWords.COORDS) + textKeyWords.blockStart)
        coords!!.forEach { vertex, offsetPair ->
            filePrinter.println("\t${vertex.vertexName} ${offsetPair?.first} ${offsetPair?.second}")
        }
        filePrinter.println(textKeyWords.blockEnd)
    }

    private fun stateNumberToFile(number: Int, filePrinter: PrintWriter){
        filePrinter.println(textKeyWords.getKeyWordString(KeyWords.STATE_NUMBER) +"="+ number.toString())
    }

    @Throws(IOException::class)
    fun toFile(fileInfo: FileInfo){
        val printer = File(fileName).printWriter()
        if(printer.checkError()) throw IOException("File didn't open")
        edgesToFile(fileInfo.graph.getEdges(), printer)
        if(fileInfo.start != null)
            startToFile(fileInfo.start, printer)
        if(fileInfo.stateNumber != null)
            stateNumberToFile(fileInfo.stateNumber, printer)
        if (fileInfo.coords != null)
            coordsToFile(fileInfo.coords, printer)
        printer.close()
    }

}