// -----------------------------------------------------------------
// Homework 1, Problem 1
// -----------------------------------------------------------------

fun kthPerfectSquare(a: Int): Int {
    return (a + 1) * (a + 1)
}

fun perfectDescription(a: Int) {
    println("perfect square $a is ${kthPerfectSquare(a)}")
}

fun main() {
    // outputs inputs/outputs/expectations for kthPerfectSquare
    fun testSquare(
        k: Int,
        expectedResult: Int,
    ) {
        println("tried $k, got ${ kthPerfectSquare(k) }, expected $expectedResult")
    }

    println("== kthPerfectSquare ==")
    testSquare(0, 1)
    testSquare(1, 4)
    testSquare(2, 9)
    testSquare(3, 16)
    println()
}

main()
