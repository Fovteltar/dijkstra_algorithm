package logic

class Vertex() {
    var vertexName = this.hashCode().toString()
    constructor(name:String) : this() {
        vertexName = name
    }

    override fun toString(): String {
        return vertexName
    }
}