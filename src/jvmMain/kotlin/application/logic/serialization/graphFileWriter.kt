import application.logic.serialization.FileInfo
import application.logic.serialization.KeyWords
import application.logic.serialization.TextKeyWords
import logic.Edge
import logic.Vertex
import java.io.File
import java.io.PrintWriter

class GraphFileWriter(fileName: String){
    var fileName = String()
    val textKeyWords:TextKeyWords = TextKeyWords()
    private val keyWords:MutableMap<String, KeyWords> = textKeyWords.keyWords
    init{
        this.fileName = fileName
    }

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

    private fun coordsToFile(coords:MutableMap<Vertex, Pair<Float, Float>?>?, filePrinter:PrintWriter){
        filePrinter.println(textKeyWords.getKeyWordString(KeyWords.COORDS) + textKeyWords.blockStart)
        coords!!.forEach { vertex, offsetPair ->
            filePrinter.println("\t${vertex.vertexName} ${offsetPair?.first} ${offsetPair?.first}")
        }
        filePrinter.println(textKeyWords.blockEnd)
    }

    private fun stateNumberToFile(number: Int, filePrinter: PrintWriter){
        filePrinter.println(textKeyWords.getKeyWordString(KeyWords.STATE_NUMBER) +"="+ number.toString())
    }
    fun toFile(fileInfo: FileInfo){
        val printer = File(fileName).printWriter()
        edgesToFile(fileInfo.graph.getEdges(), printer)
        startToFile(fileInfo.start, printer)
        if(fileInfo.stateNumber != null)
            stateNumberToFile(fileInfo.stateNumber, printer)
        if (fileInfo.coords != null)
            coordsToFile(fileInfo.coords, printer)
        printer.close()
    }

}