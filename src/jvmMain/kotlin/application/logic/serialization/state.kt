package application.logic.serialization

import logic.Vertex

data class State(val vertexToCost:MutableMap<Vertex, String>, val currentVertex:Vertex) {
}