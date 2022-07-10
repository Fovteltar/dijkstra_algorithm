package logic

import application.logic.serialization.State


class StateMachine(graph: Graph, start: Vertex) {
    private val states: MutableMap<Vertex, MutableList<String>> = mutableMapOf()
    private val currentStateVertexes: MutableList<Vertex> = mutableListOf()
    var size = 1
        private set

    init {
        graph.vertices.keys.forEach {
            states[it] = mutableListOf()
            states[it]!!.add("∞")
        }
        states[start]!!.remove("∞")
        states[start]!!.add("0")

        currentStateVertexes.add(start)
    }

    fun addNextState(currentState: MutableMap<Vertex, Int>, currentStateVertex: Vertex) {
        ++size
        currentStateVertexes.add(currentStateVertex)
        states.keys.forEach {
            if (it in currentState) states[it]!!.add(currentState[it].toString())
            else states[it]!!.add(states[it]!![states[it]!!.lastIndex])
        }
    }

    fun getState(stateNumber:Int): State?{
        if(stateNumber in 1 until size){
            val state: MutableMap<Vertex, String> = mutableMapOf()
            states.keys.forEach {
                state[it] = states[it]!![stateNumber]
            }
            return State(state, currentStateVertexes[stateNumber])
        }
        return null
    }
}