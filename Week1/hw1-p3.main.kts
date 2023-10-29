// -----------------------------------------------------------------
// Homework 1, Problem 3
// -----------------------------------------------------------------

fun makeGreeter(w: String): (String) -> String {
    fun add(x: String): String {
        return w + " " + x
    }

    return ::add
}

fun main() {
    val myGreeter = makeGreeter("Howdy")
    println(myGreeter("World"))

    val myGreeterTwo = makeGreeter("Hello")
    println(myGreeterTwo("World"))
}

main()
