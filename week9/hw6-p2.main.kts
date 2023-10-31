// -----------------------------------------------------------------
// Homework 6, Problem 2
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.runEnabledTests
import khoury.testSame

data class SneakyNode(
    val phraseIndex: Int,
    val characterIndex: Int,
    val nextNodeIndex: Int,
)

fun secretMessage(
    sn: List<SneakyNode>,
    p: List<String>,
) {
    var nodeI = sn[0].nextNodeIndex - 1
    var index = sn[0].characterIndex
    var phraseIndex = sn[0].phraseIndex
    var secret: String = "" + p[sn[0].phraseIndex].subSequence(index, index + 1)

    do {
        nodeI = sn[nodeI].nextNodeIndex - 1
        index = sn[nodeI].characterIndex
        phraseIndex = sn[nodeI].phraseIndex
        secret += p[phraseIndex].get(index)
    } while (nodeI != -2)

    println(secret)
}

@EnabledTest
fun testSecretMessage() {
    val phrases =
        listOf(
            "how are you doing?",
            "i hope you have a lovely day!",
            "programming is fun :)",
        )

    val startNode1 = SneakyNode(2, 19, 8)
    val startNode2 = SneakyNode(1, 2, 2)

    val otherNodes =
        listOf(
            SneakyNode(0, 2, 5),
            SneakyNode(2, 2, 1),
            SneakyNode(1, 28, -1),
            SneakyNode(2, 0, -1),
            SneakyNode(0, 12, 6),
            SneakyNode(1, 27, 3),
            SneakyNode(1, 1, 1),
            SneakyNode(2, 20, -1),
        )

    testSame(
        captureResults(
            {
                secretMessage(
                    listOf(startNode1) + otherNodes,
                    phrases,
                )
            },
        ),
        CapturedResult(
            Unit,
            ":)",
        ),
        "example 1",
    )

    testSame(
        captureResults(
            {
                secretMessage(
                    listOf(startNode2) + otherNodes,
                    phrases,
                )
            },
        ),
        CapturedResult(
            Unit,
            "howdy!",
        ),
        "example 2",
    )
}

runEnabledTests(this)
