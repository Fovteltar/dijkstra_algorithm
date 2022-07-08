package application.logic

import logic.Graph
import logic.Vertex

class FileInfo(val graph:Graph, val start:Vertex, val coords:MutableMap<Vertex, Pair<Float, Float>?>?, val stateNumber:Int?) {
}