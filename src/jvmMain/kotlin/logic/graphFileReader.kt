package logic

import  java.io.File
import java.io.IOException


class GraphFileReader(fileName: String) {
    var fileName = String()

    init {
        this.fileName = fileName

    }

    private fun getFileStrings(): MutableList<String> {
        val lineList = mutableListOf<String>()
        File(fileName).useLines {
            for (i in it) {
                lineList.add(i)
            }
        }
        return lineList
    }

    @Throws(IOException::class)
    private fun fileValidCheck(lineList: MutableList<String>) {
        var startIsSource = false

        val start = lineList[0]
        var step = -1
        var firstLine = 2
        //TO DO: Откомментировать все проверки, иначе .....
        if (lineList[1].all { char -> char.isDigit() } &&
            lineList[1].toInt() >= 0) step = lineList[1].toInt() //Проверка корректно ли задан шаг алгоритма
        else if (lineList[1].split(' ').size == 3) firstLine = 1 // Шаг может и не задаваться,
                                                                           // поэтому начала чтения строк меняем
        else if (!lineList[1].all { char -> char.isDigit() } ||
            lineList[1].toInt() < 0)                             // Проверка на то задан ли Шаг некорректно
            throw IOException("Algorithm step specified incorrectly")
        else throw IOException("Something wrong happend\n1: \"${lineList[1]}\"") // Неожидаемое поведение строки
                                                                                  // Указываем строку и её содержимое
        for (i in firstLine until lineList.size) {
            // Для описания ребра требуется 3 элемента <Вершина1><пробел><Вершина2><пробел><вес>
            if (lineList[i].split(' ').size != 3) { //если элементов не 3, то это точно не то, что нам надо
                throw IOException(
                    "Something wrong happend\n" +
                            "${i + 1}: \"${lineList[i]}\""
                )
            }
            val (source, destination, cost) = lineList[i].split(' ') // элементов точно три
            if (start.equals(source)) startIsSource = true // Проверка можно ли куда-нибудь попасть из старта
            if (!cost.all { char -> char.isDigit() } ||  // Проверка является ли стоимость положительным числом
                cost.toInt() < 0) throw IOException("Invalid edge weight\n${i + 1}: $source $destination $cost")
                //Если не является кидаем исключениие с указанием строки
        }
        if (!startIsSource) throw IOException("Start vertex is isolated or have no outgoing edges")
    }

    @Throws(IOException::class)
    fun graphFromFile(): Triple<Graph, Vertex, Int> {
        val lineList = getFileStrings()
        fileValidCheck(lineList)

        val vertexConformity: MutableMap<String, Vertex> = mutableMapOf()
        vertexConformity[lineList[0]] = Vertex()
        val start = vertexConformity[lineList[0]]
        val algorithmStep = lineList[1].toInt()

        val graph = Graph()
        graph.addVertex(start!!)
        for (i in 1 until lineList.size) {
            if (lineList[i].length >= 5) {
                val (source, destination, cost) = lineList[i].split(' ')
                if (source !in vertexConformity) {
                    val v = Vertex()
                    v.vertexName = source
                    vertexConformity[source] = v
                    graph.addVertex(vertexConformity[source]!!)
                }
                if (destination !in vertexConformity) {
                    val v = Vertex()
                    v.vertexName = destination
                    vertexConformity[destination] = v
                    graph.addVertex(vertexConformity[destination]!!)
                }
                graph.addEdge(Edge(Pair(vertexConformity[source]!!, vertexConformity[destination]!!), cost.toInt()))
            }
        }
        return Triple(graph, start, algorithmStep)
    }
}