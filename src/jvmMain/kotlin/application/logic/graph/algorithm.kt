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
        while (!queue.isEmpty()) {
            val current = queue.poll()
            if (current.vertex in closed) continue
            stateMachine.addNextState(mutableMapOf<Vertex, Int>(), current.vertex)
            val dests = graph.getDestinations(current.vertex)
            val part_solution: MutableMap<Vertex, Int> = mutableMapOf()
            dests?.forEach {
                val new_cost = costs[current.vertex]!! + it.weight
                if (it.vertices.second in costs) {
                    val old_cost = costs[it.vertices.second]
                    costs[it.vertices.second] = min(costs[it.vertices.second]!!, new_cost)
                    if (old_cost != costs[it.vertices.second]) {
                        part_solution[it.vertices.second] = costs[it.vertices.second]!!
                    }
                } else {
                    costs[it.vertices.second] = new_cost
                    part_solution[it.vertices.second] = costs[it.vertices.second]!!
                }
                stateMachine.addNextState(part_solution, current.vertex)
                queue.add(PriorityVertex(it.vertices.second, it.weight + costs[it.vertices.second]!!))
            }
            closed.add(current.vertex)
        }
        return stateMachine
    }
}