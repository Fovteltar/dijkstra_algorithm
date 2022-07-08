package logic


import kotlin.math.min

class Algorithm {
    fun dijkstraAlgorithm(graph: Graph, start: Vertex): StateMachine {
        val stateMachine = StateMachine(graph, start)
        val closed: MutableSet<Vertex> = mutableSetOf()
        val costs = mutableMapOf<Vertex, Int>()
        val queue = ArrayDeque<Vertex>()
        queue.add(start)
        costs[start] = 0

        while (!queue.isEmpty()) {
            val current = queue.removeFirst()
            if (current in closed) continue
            val dests = graph.getDestinations(current)
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
                stateMachine.addNextState(part_solution, current)
                queue.add(it.vertices.second)
            }
            closed.add(current)
        }
        return stateMachine
    }
}