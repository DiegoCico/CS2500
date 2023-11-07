// -----------------------------------------------------------------
// Homework 7, Problem 2
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

// creates a 2D list and maps it out according to the wikipedia math that was given to us
// once it is done mapping it out, it gets the last number in the bottom right and returns it
// https://en.wikipedia.org/wiki/Levenshtein_distance
fun levenshteinDistance(
    str1: String,
    str2: String,
): Int {
    val matrix = MutableList(str1.length + 1) { MutableList(str2.length + 1) { 0 } }
    for (i in 0..str1.length) {
        for (j in 0..str2.length) {
            if (i == 0) {
                matrix[i][j] = j
            } else if (j == 0) {
                matrix[i][j] = i
            } else if (str1[i - 1] == str2[j - 1]) {
                matrix[i][j] = matrix[i - 1][j - 1]
            } else {
                matrix[i][j] = 1 + minOf(matrix[i - 1][j], matrix[i][j - 1], matrix[i - 1][j - 1])
            }
        }
    }

    return matrix[str1.length][str2.length]
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
