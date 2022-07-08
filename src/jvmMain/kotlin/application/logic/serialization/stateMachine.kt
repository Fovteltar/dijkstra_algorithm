package logic




class StateMachine(graph: Graph, start: Vertex) {
    private val states: MutableMap<Vertex, MutableList<String>> = mutableMapOf()
    private val currentStateVertexes: MutableList<Vertex> = mutableListOf()
    var size = 1
        private set

    init {
        graph.vertices.keys.forEach {
            states[it] = mutableListOf()
            if (it == start) states[it]!!.add("0")
            else states[it]!!.add("∞")
        }
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

    fun getState(stateNumber:Int):Pair<MutableMap<Vertex, String>, Vertex>?{
        if(stateNumber in 0 until size){
            val state: MutableMap<Vertex, String> = mutableMapOf()
            states.keys.forEach {
                state[it] = states[it]!![stateNumber]
            }
            return Pair(state, currentStateVertexes[stateNumber])
        }
        return null
    }
}