// -----------------------------------------------------------------
// Homework 7, Problem 2
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

// sorts the string and maps it out according to the wikipedia math that was given to us
// once it is done mapping it out, it gets the last number in the bottom right and returns it
// https://en.wikipedia.org/wiki/Levenshtein_distance
fun levenshteinDistance(
    str1: String,
    str2: String,
): Int {
    return levenshteinHelper(str1, str2, str1.length, str2.length)
}

fun levenshteinHelper(
    str1: String,
    str2: String,
    i: Int,
    j: Int,
): Int {
    if (i == 0) {
        return j
    }

    if (j == 0) {
        return i
    }

    return if (str1[i - 1] == str2[j - 1]) {
        levenshteinHelper(str1, str2, i - 1, j - 1)
    } else {
        1 +
            minOf(
                levenshteinHelper(str1, str2, i - 1, j),
                levenshteinHelper(str1, str2, i, j - 1),
                levenshteinHelper(str1, str2, i - 1, j - 1),
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

runEnabledTests(this)
