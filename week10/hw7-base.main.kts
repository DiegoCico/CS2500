// <file "p1">
// <solution>
import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame
// </solution>
// -----------------------------------------------------------------
// Homework 7, Problem 1
// -----------------------------------------------------------------

// In this problem, you'll practice applying list abstractions in
// order to design a useful algorithm, topK!

// When working with data, it is often helpful to be able to get
// the "best" set of data points, by some measure. For example...
//
// a) The single longest string in a list of strings
// b) The 2 smallest numbers in a list of integers
// c) The 5 points that are closest to the y-axis
//    (i.e., the absolute value of their x-coordinate
//    is smallest).
//
// To start, consider the following definition of an "evaluation"
// function: one that takes an input of some type and associates
// an output "score" (where bigger scores are understood to be
// better for the task at hand):

// a way to "score" a particular type of data
typealias EvaluationFunction<T> = (T) -> Int

// TODO 1/1: Design the function topK that takes a list of
//           items, a corresponding evaluation function, and k
//           (assumed to be a postive integer), and then returns
//           the k items in the list that get the highest score
//           (if there are ties, you are free to return any of the
//           winners; if there aren't enough items in the list,
//           return as many as you can).
//
//           To help...
//           -  Your tests must cover the three examples above,
//              each time covering the empty list, a list whose
//              size is at larger than the indicated k, and a
//              non-empty list whose size is smaller than k (this
//              last test isn't necessary for (a)). Here's a data
//              type for example (c):

// A two-dimensional point
data class Point2D(val x: Int, val y: Int) {
    // distance to the y-axis
    fun distToYAxis(): Int = if (x > 0) x else -x
}

val p2dOrigin = Point2D(0, 0)
val p2DRight = Point2D(3, -4)
val p2DLeft = Point2D(-10, 7)

// @EnabledTest
// fun testPoint2D() {
//     testSame(
//         p2dOrigin.distToYAxis(),
//         0,
//         "origin/distance"
//     )

//     testSame(
//         p2DRight.distToYAxis(),
//         3,
//         "right/distance"
//     )

//     testSame(
//         p2DLeft.distToYAxis(),
//         10,
//         "left/distance"
//     )
// }
// <solution>
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
// </solution>
//           - Here is a set of steps you are encouraged to follow
//             (using appropriate abstractions) in order to code up
//             your function:
//
//             1. Given your list of items, produce a
//                list of each item with its score (as given
//                by the evaluation function); here's a useful
//                type to capture that pairing:

// an association between an item and a score
data class ItemScore<T>(val item: T, val score: Int)

//             2. Sort the list of these pairs, biggest-first
//                (the sortedByDescending member function might
//                be useful here).
//
//             3. Now that you have the list in order, you no
//                longer need the scores; produce a list that
//                maintains this order, but just contains the
//                items.
//
//             4. Finally, just return the first k items of this
//                list (cough, that you "take").
//
// <solution>
// produces (up to) the top-k items in the supplied
// list according to the supplied evaluation function
fun <T> topK(
    possibilities: List<T>,
    evalFunc: EvaluationFunction<T>,
    k: Int,
): List<T> {
    // associate each item with its score
    val itemsWithScores =
        possibilities.map {
            ItemScore(
                it,
                evalFunc(it),
            )
        }

    // sort by score
    val sortedByEval =
        itemsWithScores.sortedByDescending {
            it.score
        }

    // strip away score
    val sortedWithoutScores =
        sortedByEval.map {
            it.item
        }

    // get the first-k (i.e., top-k via score)
    return sortedWithoutScores.take(k)
}

@EnabledTest
fun testTopK() {
    // a) The single longest string in a list of strings
    val singleLongestString = {
            strings: List<String> ->
        topK(
            strings,
            { s: String -> s.length },
            1,
        )
    }

    testSame(
        singleLongestString(emptyList<String>()),
        emptyList<String>(),
        "a/empty",
    )

    testSame(
        singleLongestString(
            listOf(
                "a",
                "do",
                "tri",
                "pneumonoultramicroscopicsilicovolcanoconiosis",
                "",
            ),
        ),
        listOf(
            "pneumonoultramicroscopicsilicovolcanoconiosis",
        ),
        "a/longer",
    )

    // b) The 2 smallest numbers in a list of integers
    val twoSmallestNums = {
            nums: List<Int> ->
        topK(
            nums,
            { -it },
            2,
        )
    }

    testSame(
        twoSmallestNums(emptyList<Int>()),
        emptyList<Int>(),
        "b/empty",
    )

    testSame(
        twoSmallestNums(listOf(42)),
        listOf(42),
        "b/shorter",
    )

    testSame(
        twoSmallestNums(
            listOf(
                8,
                6,
                7,
                5,
                3,
                0,
                9,
            ),
        ),
        listOf(0, 3),
        "b/longer",
    )

    // c) The 5 points that are closest to the y-axis
    //    (i.e., the absolute value of their x-coordinate
    //    is smallest).
    val fiveSmallestPoints = {
            points: List<Point2D> ->
        topK(
            points,
            { -it.distToYAxis() },
            5,
        )
    }

    testSame(
        fiveSmallestPoints(emptyList<Point2D>()),
        emptyList<Point2D>(),
        "c/empty",
    )

    testSame(
        fiveSmallestPoints(
            listOf(
                p2dOrigin,
                p2DLeft,
                p2DRight,
            ),
        ),
        listOf(
            p2dOrigin,
            p2DRight,
            p2DLeft,
        ),
        "c/shorter",
    )

    testSame(
        fiveSmallestPoints(
            listOf(
                p2dOrigin,
            ) +
                List<Point2D>(10) {
                    // (1, 0), (3, 1), (5, 2), ...
                    Point2D(2 * it + 1, it)
                } +
                List<Point2D>(10) {
                    // (-2, 0), (-4, -2), (-6, -4)
                    Point2D(-2 * (it + 1), -2 * it)
                },
        ),
        listOf(
            p2dOrigin,
            Point2D(1, 0),
            Point2D(-2, 0),
            Point2D(3, 1),
            Point2D(-4, -2),
        ),
        "c/longer",
    )
}
// </solution>
// <file "p2">
// -----------------------------------------------------------------
// Homework 7, Problem 2
// -----------------------------------------------------------------

// In this problem you'll implement an algorithm in natural-
// language processing (NLP), which helps you reason about how
// "close" two pieces of text are from one another, by computing
// their "distance" as the minimum number of single-character
// changes (e.g., adding a character, removing one, or
// substituting) required to change one into another.
//
// TODO 1/1: Finish designing the function levenshteinDistance
//           (https://en.wikipedia.org/wiki/Levenshtein_distance),
//           which computes the distance between two strings.
//
//           Notes:
//           - In the Wikipedia article, the "Definition" is really
//             what you want to translate to Kotlin; this is a
//             common task in software development - translating a
//             theoretical/mathematical description of an approach
//             into code for your system. You are encouraged to
//             make your code look as similar as possible to this
//             description!
//           - In class we learned some best practices about the
//             use of recursion (vs iteration); in this case, you
//             can assume we'll apply this to relatively short
//             texts, which makes a recursive approach reasonable.
//           - You have been supplied some tests to help make sure
//             you got it right. Be sure to make sure you
//             understand the metric and tests before you start
//             coding!
//

// @EnabledTest
// fun testLevenshteinDistance() {
//     testSame(
//         levenshteinDistance("", "howdy"),
//         5,
//         "'', 'howdy'",
//     )

//     testSame(
//         levenshteinDistance("howdy", ""),
//         5,
//         "'howdy', ''",
//     )

//     testSame(
//         levenshteinDistance("howdy", "howdy"),
//         0,
//         "'howdy', 'howdy'",
//     )

//     testSame(
//         levenshteinDistance("kitten", "sitting"),
//         3,
//         "'kitten', 'sitting'",
//     )

//     testSame(
//         levenshteinDistance("sitting", "kitten"),
//         3,
//         "'sitting', 'kitten'",
//     )
// }
// <solution>
// calculates the minimum number of single-character edits
// between the supplied strings
fun levenshteinDistance(
    a: String,
    b: String,
): Int {
    // shorthand for producing all the letters of
    // a string except the first
    fun tail(s: String): String = s.drop(1)

    // shorthand for recursive call, making this
    // look like the wikipedia definition
    val lev = ::levenshteinDistance

    return when {
        b.isEmpty() -> a.length
        a.isEmpty() -> b.length
        a[0] == b[0] -> lev(tail(a), tail(b))
        else ->
            1 +
                minOf(
                    lev(tail(a), b),
                    lev(a, tail(b)),
                    lev(tail(a), tail(b)),
                )
    }
}

@EnabledTest
fun testLevenshteinDistance() {
    testSame(
        levenshteinDistance("", "howdy"),
        5,
        "'', 'howdy'",
    )

    testSame(
        levenshteinDistance("howdy", ""),
        5,
        "'howdy', ''",
    )

    testSame(
        levenshteinDistance("howdy", "howdy"),
        0,
        "'howdy', 'howdy'",
    )

    testSame(
        levenshteinDistance("kitten", "sitting"),
        3,
        "'kitten', 'sitting'",
    )

    testSame(
        levenshteinDistance("sitting", "kitten"),
        3,
        "'sitting', 'kitten'",
    )
}
// </solution>
// <file "p3">
// -----------------------------------------------------------------
// Homework 7, Problem 3
// -----------------------------------------------------------------

// A useful list abstraction that we haven't yet covered is `zip`
// (via the Kotlin docs): returns a list of pairs built from the
// elements of this list and the other list with the same index;
// the returned list has length of the shortest list.

// In this problem you'll practice using the imperative form of the
// accumulator pattern to implement this useful abstraction!

// TODO 1/1: apply the imperative form of the accumulator pattern
//           to finish designing the function, myZip.
//
//           Notes:
//           - While you'll need a mutable list inside the
//             function, you should return an immutable list.
//           - A Pair is a built-in type in Kotlin; look it up in
//             the docs to understand what it does!
//           - You have been supplied tests that should pass once
//             the function has been completed.
//

// @EnabledTest
// fun testMyZip() {
//     fun <A, B> helpTest(
//         listA: List<A>,
//         listB: List<B>,
//         desc: String,
//     ) {
//         testSame(
//             myZip(listA, listB),
//             listA.zip(listB),
//             "$desc: a zip b",
//         )

//         testSame(
//             myZip(listB, listA),
//             listB.zip(listA),
//             "$desc: b zip a",
//         )
//     }

//     helpTest(
//         emptyList<String>(),
//         emptyList<Int>(),
//         "empty/empty",
//     )

//     helpTest(
//         emptyList<String>(),
//         listOf(1, 2),
//         "empty/non-empty",
//     )

//     helpTest(
//         listOf(1, 2, 3),
//         listOf("a", "b"),
//         "mixed",
//     )
// }
// <solution>
// zips two supplied lists
fun <A, B> myZip(
    listA: List<A>,
    listB: List<B>,
): List<Pair<A, B>> {
    val result = mutableListOf<Pair<A, B>>()
    val lastIndex =
        minOf(
            listA.lastIndex,
            listB.lastIndex,
        )

    for (idx in 0..lastIndex) {
        result.add(
            Pair(
                listA[idx],
                listB[idx],
            ),
        )
    }

    return result
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
// </solution>
// <file "p4">
// -----------------------------------------------------------------
// Homework 7, Problem 4
// -----------------------------------------------------------------

// In this problem, you'll practice applying the functional
// version of the accumulator pattern to reimplement the useful
// `joinToString` function.

// TODO 1/2: Finish designing the myJoinToString function, which
//           creates a string from all the elements of the supplied
//           list separated using a supplied separator.
//
//           Notes:
//           - For purposes of this problem, you must apply the
//             recursive form of the accumulator pattern.
//           - Recall that we can recur on the structure of a
//             Kotlin list by checking if it's empty and if not,
//             combine the first element with the recursive result
//             of the remaining list elements.
//           - You have been supplied tests that should pass once
//             the function has been completed.
//

// @EnabledTest
// fun testMyJoinToString() {
//     fun <T> helpTest(
//         l: List<T>,
//         sep: String,
//         desc: String,
//     ) {
//         testSame(
//             myJoinToString(l, sep),
//             l.joinToString(sep),
//             desc,
//         )
//     }

//     helpTest(emptyList<Int>(), ", ", "empty")
//     helpTest(listOf(1), ", ", "single")
//     helpTest(listOf(1, 2, 3), ", ", "nums")
//     helpTest(listOf("alice", "bob", "chris", "dan"), "-", "txts")
// }

// <solution>
// joins the list elements with the seperator
fun <T> myJoinToString(
    elements: List<T>,
    sep: String,
): String {
    // iterates through the list
    // accumulator: the joined string so far
    fun myJoinToStringHelp(
        sublist: List<T>,
        acc: String,
    ): String {
        return when (sublist.isEmpty()) {
            true -> acc
            false -> {
                val firstStr: String = "${ sublist[0] }"
                val newAcc: String =
                    when (acc.isEmpty()) {
                        true -> firstStr
                        false -> "$acc$sep$firstStr"
                    }

                myJoinToStringHelp(sublist.drop(1), newAcc)
            }
        }
    }

    return myJoinToStringHelp(elements, "")
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
// </solution>
// TODO 2/2: Now given that your recursive implementation works,
//           finish designing the function myJoinToStringFold to
//           produce the same result, but effectively using the
//           fold abstraction (which iterates for itself).
//
//           You have been supplied tests that should pass once
//           the function has been completed; this time, instead
//           of comparing against the built-in Kotlin version, they
//           show that your two implementations solve the same
//           problem!
//

// @EnabledTest
// fun testMyJoinToStringFold() {
//     fun <T> helpTest(
//         l: List<T>,
//         sep: String,
//         desc: String,
//     ) {
//         testSame(
//             myJoinToString(l, sep),
//             myJoinToStringFold(l, sep),
//             desc,
//         )
//     }

//     helpTest(emptyList<Int>(), ", ", "empty")
//     helpTest(listOf(1), ", ", "single")
//     helpTest(listOf(1, 2, 3), ", ", "nums")
//     helpTest(listOf("alice", "bob", "chris", "dan"), "-", "txts")
// }
// <solution>
// joins the list elements with the seperator
fun <T> myJoinToStringFold(
    elements: List<T>,
    sep: String,
): String {
    return elements.fold("") { acc, element ->
        val elementString = "$element"

        when (acc.isEmpty()) {
            true -> elementString
            false -> "$acc$sep$elementString"
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

fun main() {
}

runEnabledTests(this)
main()
// </solution>
