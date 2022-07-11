package logic

import application.logic.serialization.AlgorithmUpdate


class StateMachine(graph: Graph, start: Vertex) {
    private val states: MutableMap<Vertex, MutableList<Float>> = mutableMapOf()
    private val currentStateVertexes: MutableList<Vertex?> = mutableListOf()
    private val updates:MutableList<Pair<Vertex, Int>?>  = mutableListOf()
    var size = 0
        private set

    init {
        graph.vertices.keys.forEach {
            states[it] = mutableListOf()
            states[it]!!.add(Float.POSITIVE_INFINITY)
        }
        currentStateVertexes.add(null)
        updates.add(null)
    }
    fun getUpdate(index:Int):AlgorithmUpdate {
        return when(index) {
            0 -> AlgorithmUpdate(null, null)
            1 -> AlgorithmUpdate(Pair(null, currentStateVertexes[1]), null)
            in 2 until size -> {
                val prevState = getState(index)
                val currentVertexChange =
                    if(currentStateVertexes[index] == currentStateVertexes[index+1])
                        null
                    else
                        Pair(currentStateVertexes[index], currentStateVertexes[index+1])
                val markVertexChange =
                    if(prevState != null && updates[index+1]?.first != null && updates[index+1]?.second != null)
                        Pair(updates[index+1]!!.first, Pair(prevState[updates[index+1]?.first]!!.toInt(), updates[index+1]!!.second))
                    else null
                AlgorithmUpdate(currentVertexChange, markVertexChange)
            }
            else -> {AlgorithmUpdate(null, null)}
        }
    }
    fun addNextState(currentUpdate: Pair<Vertex, Int>?, currentStateVertex: Vertex) {
        currentStateVertexes.add(currentStateVertex)
        updates.add(currentUpdate)
        states.keys.forEach {
            states[it]!!.add(states[it]!![size])
        }
        if(currentUpdate != null) {
            states[currentUpdate.first]!!.removeAt(size)
            states[currentUpdate.first]!!.add(currentUpdate.second.toFloat())
        }
        ++size
    }

    private fun getState(stateNumber:Int):MutableMap<Vertex, Float>?{
        if(stateNumber in 0 until size){
            val state: MutableMap<Vertex, Float> = mutableMapOf()
            states.keys.forEach {
                state[it] = states[it]!![stateNumber]
            }
            return state
        }
        return null
    }
}