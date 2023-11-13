// -----------------------------------------------------------------
// Homework 7, Problem 1
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

typealias EvaluationFunction<T> = (T) -> Int

// A two-dimensional point
data class Point2D(val x: Int, val y: Int) {
    // distance to the y-axis
    fun distToYAxis(): Int = if (x > 0) x else -x
}

val p2dOrigin = Point2D(0, 0)
val p2DRight = Point2D(3, -4)
val p2DLeft = Point2D(-10, 7)

@EnabledTest
fun testPoint2D() {
    testSame(
        p2dOrigin.distToYAxis(),
        0,
        "origin/distance",
    )

    testSame(
        p2DRight.distToYAxis(),
        3,
        "right/distance",
    )

    testSame(
        p2DLeft.distToYAxis(),
        10,
        "left/distance",
    )
}

// an association between an item and a score
data class ItemScore<T>(val item: T, val score: Int)

// gets the list, function, and k
// it maps the list into the itemscore and then sorts it in decending
// maps it again and grabs all the k's
fun <T> topK(
    items: List<T>,
    eF: EvaluationFunction<T>,
    k: Int,
): List<T> {
    if (items.isEmpty()) {
        return emptyList()
    }

    fun initFunc(s: T): ItemScore<T> {
        return ItemScore(s, eF(s))
    }

    val scoreList = items.map(::initFunc)
    val sortList = scoreList.sortedByDescending { it.score }

    return sortList.take(k).map { it.item }
}

@EnabledTest
fun testTopK() {
    testSame(
        topK(listOf("Hi", "Bye", "See Yea", "Night"), { s -> s.length }, 3),
        listOf("See Yea", "Night", "Bye"),
        "longest String",
    )

    testSame(
        topK(listOf(1, 6, 3, 9, 7, 3, 5), { n -> n }, 3),
        listOf(9, 7, 6),
        "Biggest Number",
    )

    testSame(
        topK(listOf("Hi", "Bye", "See Yea", "Night"), { s -> s.length * -1 }, 2),
        listOf("Hi", "Bye"),
        "Shortest String",
    )

    testSame(
        topK(listOf(1, 6, 3, 9, 7, 3, 5), { n -> n * -1 }, 2),
        listOf(1, 3),
        "Smallest Number",
    )

    testSame(
        topK(emptyList<Int>(), { s -> s }, 5),
        listOf(),
        "empty/INT",
    )

    testSame(
        topK(emptyList<String>(), { s -> s.length }, 5),
        listOf(),
        "empty/STRING",
    )
}

runEnabledTests(this)
