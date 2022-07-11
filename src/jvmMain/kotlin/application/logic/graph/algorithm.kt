package logic

import application.logic.graph.PriorityVertex
import java.util.*
import kotlin.math.min

class Algorithm {
    fun dijkstraAlgorithm(graph: Graph, start: Vertex): StateMachine {
        val stateMachine = StateMachine(graph, start)
        val closed: MutableSet<Vertex> = mutableSetOf()
        val costs = mutableMapOf<Vertex, Int>()
        val queue = PriorityQueue<PriorityVertex>()
        queue.add(PriorityVertex(start, Int.MAX_VALUE)) //
        costs[start] = 0
        PriorityVertex(start, 1)
        stateMachine.addNextState(Pair(start, costs[start]!!), start)
        while (!queue.isEmpty()) {
            val current = queue.poll()
            if (current.vertex in closed) continue
            stateMachine.addNextState(null, current.vertex)
            val dests = graph.getDestinations(current.vertex)
            dests?.forEach {
                val new_cost = costs[current.vertex]!! + it.weight
                if (it.vertices.second in costs) {
                    costs[it.vertices.second] = min(costs[it.vertices.second]!!, new_cost)
                } else {
                    costs[it.vertices.second] = new_cost
                }
                stateMachine.addNextState(Pair(it.vertices.second, costs[it.vertices.second]!!), current.vertex)
                queue.add(PriorityVertex(it.vertices.second, it.weight + costs[it.vertices.second]!!))
            }
            closed.add(current.vertex)
        }
        return stateMachine
    }
}