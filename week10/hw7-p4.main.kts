// -----------------------------------------------------------------
// Homework 7, Problem 4
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

// goes through each of the list, puts them in a string and checks if it is the last one or not
fun <T> myJoinToString(
    l: List<T>,
    sep: String,
): String {
    if (l.isEmpty()) {
        return ""
    }
    if (l.size == 1) {
        return l[0].toString()
    }

    return l[0].toString() + sep + myJoinToString(l.drop(1), sep)
}

@EnabledTest
fun testMyJoinToString() {
    fun <T> helpTest(
        l: List<T>,
        sep: String,
        desc: String,
    ) {
        testSame(
            myJoinToString(l, sep),
            l.joinToString(sep),
            desc,
        )
    }

    helpTest(emptyList<Int>(), ", ", "empty")
    helpTest(listOf(1), ", ", "single")
    helpTest(listOf(1, 2, 3), ", ", "nums")
    helpTest(listOf("alice", "bob", "chris", "dan"), "-", "txts")
}

// uses the .fold abstraction which needs an initial, an int for the index a string and a S
fun <T> myJoinToStringFold(
    l: List<T>,
    sep: String,
): String {
    return l.foldIndexed("") { i, s, e ->
        if (i == l.size - 1) {
            s + e
        } else {
            s + e + sep
        }
    }
}

@EnabledTest
fun testMyJoinToStringFold() {
    fun <T> helpTest(
        l: List<T>,
        sep: String,
        desc: String,
    ) {
        testSame(
            myJoinToString(l, sep),
            myJoinToStringFold(l, sep),
            desc,
        )
    }

    helpTest(emptyList<Int>(), ", ", "empty")
    helpTest(listOf(1), ", ", "single")
    helpTest(listOf(1, 2, 3), ", ", "nums")
    helpTest(listOf("alice", "bob", "chris", "dan"), "-", "txts")
}

runEnabledTests(this)
