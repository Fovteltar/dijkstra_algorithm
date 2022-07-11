package application.logic.serialization

import logic.Vertex

data class AlgorithmUpdate(
    val currentVertexChange:Pair<Vertex?, Vertex?>?,
    val markVertexChange:Pair<Vertex, Pair<Int, Int>>?
) {}