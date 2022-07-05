package logic

import ui.objects.VertexUI

class Vertex() {
    val vertexUI = VertexUI(this)
    var vertexName: String = this.hashCode().toString()
}