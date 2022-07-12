package application.logic.serialization
import java.io.IOException



class Parser{
    private val textKeyWords = TextKeyWords()
    private val keyWords:MutableMap<String, KeyWords> = textKeyWords.keyWords
    private val keyWordsStartIndexes: MutableMap<KeyWords, Int> = mutableMapOf()
    private val keyWordsBlocksEnds: MutableMap<KeyWords, Int> = mutableMapOf()
    private var strings:MutableList<String> = mutableListOf()


    fun parse(strings:MutableList<String>){
        for (i in strings.indices)
            keyWords.keys.forEach {
                if(strings[i].contains(it, ignoreCase = true)){
                    if(!keyWordsStartIndexes.containsKey(keyWords[it]!!)) {
                        keyWordsStartIndexes[keyWords[it]!!] = i
                    }
                    else{
                        throw IOException("Input error \n${i+2}: ${strings[i]}")
                    }
                }
        }
        if(!keyWordsStartIndexes.containsKey(KeyWords.GRAPH)){
            throw IOException("Graph is not specified")
        }
        this.strings = strings
        checkValidGraph()
        checkValidCoords()
        checkValidBlocks()
    }


    fun getStartVertexName():String?{
        if(keyWordsStartIndexes.containsKey(KeyWords.START)) {
            val startValue =
                strings[keyWordsStartIndexes[KeyWords.START]!!].filter { !it.isWhitespace() }.substringAfter("=")
            if (startValue != "=") {
                return startValue
            }
        }
        return null
    }

    fun getStateNumber():Int?{
        if (keyWordsStartIndexes.containsKey(KeyWords.STATE_NUMBER)){
            val stepValue= strings[keyWordsStartIndexes[KeyWords.STATE_NUMBER]!!].
            filter { !it.isWhitespace() }.substringAfter("=")
            if (stepValue != "=" && stepValue.toIntOrNull() != null && stepValue.toInt() >= 0) return stepValue.toInt()
        }
        return null
    }


    private fun checkValidGraph(){
        var currentIndexString:Int = keyWordsStartIndexes[KeyWords.GRAPH] !!+ 1
        var startExistingFlag = false

        while (true){
            val splitString = strings[currentIndexString].split(" ").filter { s: String -> s.isNotBlank() }
            if (splitString.size == 3) {
                var (source, destination, weight) = splitString
                if (weight.toIntOrNull() == null || weight.toInt() < 0)
                    throw IOException("Invalid edge weight\n${currentIndexString+2}: ${strings[currentIndexString]}")
                ++currentIndexString
            }
            else break
        }

        if(strings[currentIndexString] != textKeyWords.blockEnd) throw IOException("Wrong graph block")
        keyWordsBlocksEnds[KeyWords.GRAPH] = currentIndexString - 1
    }

    private fun checkValidCoords(){
        if (!keyWordsStartIndexes.containsKey(KeyWords.COORDS)) return
        var currentIndexString:Int = keyWordsStartIndexes[KeyWords.COORDS] !!+ 1
        val start = getStartVertexName()

        while (true){
            val splitString = strings[currentIndexString].split('(', ')', ',', ' ').
            filter { s: String -> s.isNotBlank()}

            if (splitString.size == 3) {
                var (vertex, offsetX, offsetY) = splitString
                if (offsetX.toFloatOrNull() == null)
                    throw IOException("Invalid Xoffset\n${currentIndexString + 1}: ${strings[currentIndexString]}")
                if (offsetY.toFloatOrNull() == null)
                    throw IOException("Invalid Yoffset\n${currentIndexString + 1}: ${strings[currentIndexString]}")
                ++currentIndexString
            }
            else break
        }

        if(strings[currentIndexString] != textKeyWords.blockEnd) throw IOException("Wrong graph block")
        keyWordsBlocksEnds[KeyWords.COORDS] = currentIndexString - 1
    }

    private fun checkValidBlocks(){
        val graphRange = keyWordsStartIndexes[KeyWords.GRAPH]!!.rangeTo(keyWordsBlocksEnds[KeyWords.GRAPH]!!)
        //Start key check
        if (keyWordsStartIndexes.containsKey(KeyWords.START) && keyWordsStartIndexes[KeyWords.START]!! in graphRange)throw IOException("Input error")

        val coordsRange = keyWordsBlocksEnds[KeyWords.COORDS]?.let { keyWordsStartIndexes[KeyWords.COORDS]?.rangeTo(it) }
        if (coordsRange != null){
            if (keyWordsStartIndexes.containsKey(KeyWords.START) && keyWordsStartIndexes[KeyWords.START] in coordsRange)throw IOException("Input error")
        }
        if(keyWordsStartIndexes.containsKey(KeyWords.STATE_NUMBER)){
            if (coordsRange != null){
                if (keyWordsStartIndexes[KeyWords.STATE_NUMBER]!! in coordsRange)throw IOException("Input error")
            }
            if (keyWordsStartIndexes[KeyWords.STATE_NUMBER]!! in graphRange)throw IOException("Input error")
        }
    }

    fun getGraphInformation():MutableList<Triple<String, String, Int>>{
        val graphInformation:MutableList<Triple<String, String, Int>> = mutableListOf()
        for (i in (keyWordsStartIndexes[KeyWords.GRAPH]!! + 1).rangeTo(keyWordsBlocksEnds[KeyWords.GRAPH]!!)){
            val (source, destination, weight) = strings[i].split(' ').filter { s: String ->  s.isNotBlank()}
            graphInformation.add(Triple(source, destination, weight.toInt()))
        }
        return graphInformation
    }

    fun getCoordsInformation():MutableMap<String, Pair<Float, Float>>{
        if(!keyWordsStartIndexes.containsKey(KeyWords.COORDS)) return throw IOException("Coords not set")
        val coordsInformation:MutableMap<String, Pair<Float, Float>> = mutableMapOf()
        for (i in (keyWordsStartIndexes[KeyWords.COORDS]!!+1).rangeTo(keyWordsBlocksEnds[KeyWords.COORDS]!!)){
            val (vertex, xOffset, yOffset) = strings[i].split(' ', '(', ')', ',').filter { s: String ->  s.isNotBlank()}
            coordsInformation[vertex] = Pair(xOffset.toFloat(), yOffset.toFloat())
        }
        return coordsInformation
    }
}
