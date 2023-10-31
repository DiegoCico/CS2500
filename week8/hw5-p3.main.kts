// -----------------------------------------------------------------
// Homework 5, Problem 3
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

val sepCard = "|"
val sepTag = ","

// flash card with a back and front and tags
data class TaggedFlashCard(val front: String, val back: String, val tag: List<String>) {
    // checks if it has the tag
    fun isTagged(s: String): Boolean {
        return s in tag
    }

    // prints out it in the right format
    fun fileFormat(): String {
        return this.front + sepCard + this.back + sepCard + this.tag.joinToString()
    }
}

@EnabledTest
fun testTaggedFlashCard() {
    val t1 = TaggedFlashCard("Hi", "Bye", listOf("greetings", "leaving", "human-like"))
    val t2 = TaggedFlashCard("1+1", "2", listOf("Math", "Addition", "easy"))
    val t3 = TaggedFlashCard("What class is this", "Coding", listOf("coding", "FUNDIES", "easy-hard"))

    testSame(
        t1.isTagged("leaving"),
        true,
        "Tag for t1",
    )

    testSame(
        t1.fileFormat(),
        "Hi|Bye|greetings, leaving, human-like",
        "fomrat for t1",
    )

    testSame(
        t2.isTagged("leaving"),
        false,
        "Tag for t2",
    )

    testSame(
        t2.fileFormat(),
        "1+1|2|Math, Addition, easy",
        "fomrat for t2",
    )

    testSame(
        t3.isTagged("FUNDIES"),
        true,
        "Tag for t3",
    )

    testSame(
        t3.fileFormat(),
        "What class is this|Coding|coding, FUNDIES, easy-hard",
        "fomrat for t3",
    )
}

runEnabledTests(this)
