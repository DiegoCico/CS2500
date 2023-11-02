// -----------------------------------------------------------------
// Homework 6, Problem 1
// -----------------------------------------------------------------
import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

// implements the list and the function into this function and it combines them together
fun <T> myInplaceMap(
    l: MutableList<T>,
    func: (MutableList<T>) -> List<T>,
): List<T> {
    return func(l)
}

@EnabledTest
fun testMyInplaceMap() {
    testSame(
        myInplaceMap(mutableListOf(), { t: MutableList<Int> -> t.map { it * 2 } }),
        listOf(),
        "EMPTY",
    )

    testSame(
        myInplaceMap(mutableListOf(1, 2, 3), { t: MutableList<Int> -> t.map { it * 2 } }),
        listOf(2, 4, 6),
        "123",
    )

    testSame(
        myInplaceMap(mutableListOf("Hi", "Bye", "Nice"), { t: MutableList<String> -> t.map { it + "!" } }),
        listOf("Hi!", "Bye!", "Nice!"),
        "Strings",
    )
}

runEnabledTests(this)
