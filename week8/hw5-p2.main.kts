// -----------------------------------------------------------------
// Homework 5, Problem 2
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

// I and O takes in any data type
// I is the input O is the output
// afterwards it maps them out according to the function used
fun <I, O> myMap(
    l: List<I>,
    functionC: (I) -> O,
): List<O> {
    return l.map(functionC)
}

@EnabledTest
fun testMyMap() {
    val emptyNums = emptyList<Int>()
    val nums = listOf(8, 6, 7, 5, 3, 0, 9)
    val names = listOf("alice", "bob", "chris")

    val add1: (Int) -> Int = { it + 1 }

    fun <T> exclaim(arg: T): String = "$arg!"
    val stringLen: (String) -> Int = { it.length }

    testSame(
        myMap(emptyNums, add1),
        emptyNums.map(add1),
        "empty, +1",
    )

    testSame(
        myMap(nums, add1),
        nums.map(add1),
        "non-empty, +1",
    )

    testSame(
        myMap(names, ::exclaim),
        names.map(::exclaim),
        "non-empty, exclaim",
    )

    testSame(
        myMap(names, stringLen),
        names.map(stringLen),
        "non-empty, length",
    )

    testSame(
        myMap(myMap(names, stringLen), add1),
        names.map(stringLen).map(add1),
        "non-empty, length->add1",
    )
}

runEnabledTests(this)
