// -----------------------------------------------------------------
// Homework 2, Problem 2
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.testSame

val WAKEUP_PROMPT = "What hour in the morning (1-11) do you like to wakeup?"
val WAKEUP_NOT_A_NUMBER = "You did not enter a number :("
val WAKEUP_NOT_IN_RANGE = "You did not enter an hour from 1-11am"
val WAKEUP_EARLY = "Before 8am? Early bird catches the worm!"
val WAKEUP_OTHER = "8am or later? Coffee time!"

// it checks the time we receive and within that time it checks the conditions in the when statemt
fun wakeupTime() {
    println(WAKEUP_PROMPT)
    val inp = input()
    when {
        (inp.toIntOrNull() == null) -> println(WAKEUP_NOT_A_NUMBER)
        (inp.toInt() < 1 || inp.toInt() > 11) -> println(WAKEUP_NOT_IN_RANGE)
        (inp.toInt() < 8) -> println(WAKEUP_EARLY)
        else -> println(WAKEUP_OTHER)
    }
}

@EnabledTest
fun testWakeupTime() {
    // helps to test, given what is typed at the console
    // and what the expected output should be
    fun testHelp(
        consoleIn: String,
        expectedOut: String,
    ) {
        testSame(
            captureResults(::wakeupTime, consoleIn),
            CapturedResult(
                Unit,
                WAKEUP_PROMPT,
                expectedOut,
            ),
            consoleIn,
        )
    }

    testHelp("howdy", WAKEUP_NOT_A_NUMBER)
    testHelp("0", WAKEUP_NOT_IN_RANGE)
    testHelp("12", WAKEUP_NOT_IN_RANGE)
    testHelp("5", WAKEUP_EARLY)
    testHelp("8", WAKEUP_OTHER)
    testHelp("11", WAKEUP_OTHER)
}

testWakeupTime()
