package application.logic.serialization

import logic.Graph
import logic.Vertex

data class FileInfo(val graph:Graph, val start:Vertex, val coords:MutableMap<Vertex, Pair<Float, Float>?>?, val stateNumber:Int?) {
}