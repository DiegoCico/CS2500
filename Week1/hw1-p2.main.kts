// -----------------------------------------------------------------
// Homework 1, Problem 2
// -----------------------------------------------------------------

fun bingoWord(w: String): String {
    val c: String = w.first().uppercase()
    val l: String = w.length.toString()
    return c + " " + l
}

fun main() {
    println("--------------------------")
    println(bingoWord("bingo"))
    println(bingoWord("word"))
    println("--------------------------")
}

main()
