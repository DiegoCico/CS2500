// -----------------------------------------------------------------
// Homework 7, Problem 3
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

fun <A, B> myZip(
    aL: List<A>,
    bL: List<B>,
): List<Pair<A, B>> {
    val listm = mutableListOf<Pair<A, B>>()

    for (i in 0 until minOf(aL.size, bL.size)) {
        val pair = Pair(aL[i], bL[i])
        listm.add(pair)
    }

    return listm
}

@EnabledTest
fun testMyZip() {
    fun <A, B> helpTest(
        listA: List<A>,
        listB: List<B>,
        desc: String,
    ) {
        testSame(
            myZip(listA, listB),
            listA.zip(listB),
            "$desc: a zip b",
        )

        testSame(
            myZip(listB, listA),
            listB.zip(listA),
            "$desc: b zip a",
        )
    }

    helpTest(
        emptyList<String>(),
        emptyList<Int>(),
        "empty/empty",
    )

    helpTest(
        emptyList<String>(),
        listOf(1, 2),
        "empty/non-empty",
    )

    helpTest(
        listOf(1, 2, 3),
        listOf("a", "b"),
        "mixed",
    )
}
runEnabledTests(this)
