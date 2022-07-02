package logic

import ui.objects.EdgeUI

class Edge(val vertices: Pair<Vertex, Vertex>, val weight: Int = 0) {
    val edgeUI = EdgeUI(this)
}