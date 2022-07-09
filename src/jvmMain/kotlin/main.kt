import logic.Algorithm
import logic.GraphFileReader
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

fun main() {
//    BasicConfigurator.configure()
//    val application = Application()
//    application.start()
    val gfr = GraphFileReader("info.txt")
    val gfi = gfr.graphFromFile()
    val alg = Algorithm()
    val stateMachine = alg.dijkstraAlgorithm(gfi.graph, gfi.start)
    val currentstep = if(gfi.stateNumber != null) gfi.stateNumber else 0
    for (i in currentstep until stateMachine.size){
        val state = stateMachine.getState(i)
        println("Step $i current vertex is ${state!!.second}")
        state.first.keys.forEach {
            println("\t${it} - ${state.first[it]}")
        }
    }
    val fileWriter = GraphFileWriter("name.txt")
    fileWriter.toFile(gfi)
}