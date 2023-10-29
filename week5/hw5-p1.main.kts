// -----------------------------------------------------------------
// Homework 5, Problem 1
// -----------------------------------------------------------------

import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.runEnabledTests
import khoury.testSame

// uses do-while to go through every single one till the exit word is said
fun affirmTillDone(q: String) {
    do {
        println(q)
        println()
        println(prompt)
        val a = input()
    } while (a != exitResponse)

    println(finished)
}

val exitResponse = "exit"
val prompt = "Press '$exitResponse' to indicate you are done :)"
val finished = "Have a good day!"

@EnabledTest
fun testAffirmTillDone() {
    // Brené Brown
    val affirmation1 = "Courage starts with showing up and letting ourselves be seen."
    val helpTest1 = { affirmTillDone(affirmation1) }

    // Michelle Obama
    val affirmation2 = "Am I good enough? Yes I am."
    val helpTest2 = { affirmTillDone(affirmation2) }

    testSame(
        captureResults(
            helpTest1,
            "again",
            "AGAIN",
            exitResponse,
        ),
        CapturedResult(
            Unit,
            affirmation1, "", prompt,
            affirmation1, "", prompt,
            affirmation1, "", prompt,
            finished,
        ),
        "one",
    )

    testSame(
        captureResults(
            helpTest2,
            "MORE",
            exitResponse,
        ),
        CapturedResult(
            Unit,
            affirmation2,
            "",
            prompt,
            affirmation2,
            "",
            prompt,
            finished,
        ),
        "two",
    )
}

runEnabledTests(this)
// affirmTillDone("test")
