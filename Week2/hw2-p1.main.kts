// -----------------------------------------------------------------
// Homework 2, Problem 1
// -----------------------------------------------------------------

// determines if it starts with Y or not
fun startsWithY(word: String): String {
    if (word.lowercase().startsWith("y")) {
        return "starts with y"
    }
    return "does not start with y"
}

fun main() {
    println(startsWithY("kotlin"))
    println(startsWithY("ymca"))
    println(startsWithY("pinnaple"))
}

main()
