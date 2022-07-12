package application.logic.serialization

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ParserTest {

    @Test
    fun parse() {
        val string_ingoing = mutableListOf<String>(
            "graph{",
            "a b 1",
            "b c 2",
            "a c 3",
            "b e 2",
            "}",
            "start = a",
            "state = 0",
            "coords{",
            "a(0,0)",
            "b(0,1)",
            "c(1,1)",
            "e(1,2)",
            "}",
        )
        val v = Parser()
        assertDoesNotThrow { v.parse(string_ingoing) }
    }
    @Test
    fun parse1() {
        val string_ingoing = mutableListOf<String>(
            "graph{",
            "a b 7",
            "b c 10",
            "c e 1",
            "a e 5",
            "i e 5",
            "a e 15",
            "j i 4",
            "c j 6",
            "}",
            "start = a",
            "state = 3",
            "coords{",
            "a(0,0)",
            "b(0,1)",
            "c(1,1)",
            "e(1,2)",
            "i(2,2)",
            "j(3,1)",
            "}",
        )
        val v = Parser()
        assertDoesNotThrow { v.parse(string_ingoing) }
    }
    @Test
    fun parse2() {
        val string_ingoing = mutableListOf<String>(
            "state = 0",
            "graph{",
            "a b 1",
            "b c 2",
            "c e 1",
            "a e 5",
            "i e 5",
            "a e 15",
            "}",
            "start = a",
            "coords{",
            "a(0,0)",
            "b(0,1)",
            "c(1,1)",
            "e(1,2)",
            "i(2,2)",
            "}",
        )
        val v = Parser()
        assertDoesNotThrow { v.parse(string_ingoing) }
    }
    @Test
    fun parse3() {
        val string_ingoing = mutableListOf<String>(
            "graph{",
            "a b 1",
            "b c 2",
            "c e 1",
            "a e 5",
            "i e 5",
            "a e 15",
            "}",
            "state = 0",
            "start = a",
            "coords{",
            "a(1,1)",
            "b(2,2)",
            "c(2,3)",
            "}",
        )


        val v = Parser()
        assertDoesNotThrow { v.parse(string_ingoing) }
    }
}