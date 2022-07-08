package logic


class Edge(val vertices: Pair<Vertex, Vertex>, var weight: Int = 0) {
    override fun toString(): String {
        return "${vertices.first} ${vertices.second} $weight"
    }
}