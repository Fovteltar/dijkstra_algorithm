package application.logic.serialization

class TextKeyWords{
    val keyWords:MutableMap<String, KeyWords> = mutableMapOf(
        "coords" to KeyWords.COORDS,
        "graph" to KeyWords.GRAPH,
        "state" to KeyWords.STATE_NUMBER,
        "start" to KeyWords.START)
    val blockStart = "{"

    val blockEnd = "}"

    fun getKeyWordString(key:KeyWords):String{
        for ((stringKey, enumKey) in keyWords){
            if (key == enumKey) return stringKey
        }
        return "NOT FOUND KEY-WORD STRING"
    }

}