package logic

import kotlin.math.min

class Algorithm {
    fun dijkstraAlgorithm(graph: Graph, start: Vertex): MutableList<Pair<Vertex, MutableMap<Vertex, Int>>> {
        val solution_list: MutableList<Pair<Vertex, MutableMap<Vertex, Int>>> = mutableListOf()
        val closed: MutableSet<Vertex> = mutableSetOf()
        val costs = mutableMapOf<Vertex, Int>()
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        costs.put(start, 0)

        while (!queue.isEmpty()) {
            val current = queue.removeFirst()
            if (current in closed) continue
            val dests = graph.vertices[current]?.keys
            val part_solution: MutableMap<Vertex, Int> = mutableMapOf()
            dests?.forEach {
                val new_cost = costs[current]!! + it.weight
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
                queue.add(it.vertices.second)
            }
            solution_list.add(Pair(current, part_solution))
            closed.add(current)
        }
        return solution_list
    }
}