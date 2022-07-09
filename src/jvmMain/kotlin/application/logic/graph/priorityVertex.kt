package application.logic.graph

import logic.Vertex

data class PriorityVertex(val vertex: Vertex, val priority: Int) : Comparable<PriorityVertex> {
    override fun compareTo(other: PriorityVertex): Int {
        if (priority > other.priority) return 1
        else if (priority < other.priority) return -1
        return 0
    }
}