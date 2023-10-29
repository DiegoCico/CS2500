// -----------------------------------------------------------------
// Homework 3, Problem 3
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.reactConsole
import khoury.testSame

enum class NumberState {
    START,
    DONE,
}

data class NumberStage(var state: NumberState, var lis: List<Int>, var r: String)

// gets the state and returns the number or if it is done or not
fun numberStateToText(c: NumberStage): String {
    if (c.state == NumberState.START && !c.lis.isEmpty()) {
        return c.lis[0].toString()
    } else {
        return "Done!"
    }
}

// switches between the states
fun numberNextState(
    c: NumberStage,
    s: String,
): NumberStage {
    if (c.lis.isEmpty() || c.lis.size == 1) {
        return NumberStage(NumberState.DONE, c.lis, "test")
    } else {
        return NumberStage(NumberState.START, c.lis.drop(1), "$c.list[0]")
    }
}

// checks if it is done or not
fun isDone(c: NumberStage): Boolean {
    return (c.state == NumberState.DONE)
}

// runs the program
fun showNumbers(numL: List<Int>): String {
    if (numL.size != 0) {
        return reactConsole(
            initialState = NumberStage(NumberState.START, numL, ""),
            stateToText = ::numberStateToText,
            nextState = ::numberNextState,
            isTerminalState = ::isDone,
        ).r
    } else {
        println("Done!")
        return ""
    }
}

@EnabledTest
fun testShowNumbers() {
    // makes a captureResults-friendly function :)
    fun helpTest(numList: List<Int>): () -> Unit {
        fun showMyNumbers() {
            showNumbers(numList)
        }

        return ::showMyNumbers
    }

    testSame(
        captureResults(
            helpTest(emptyList<Int>()),
            "",
        ),
        CapturedResult(
            Unit,
            "Done!",
        ),
        "empty",
    )

    testSame(
        captureResults(
            helpTest(listOf(5, 4, 3, 2, 1)),
            "",
            "",
            "",
            "",
            "",
            "",
        ),
        CapturedResult(
            Unit,
            "5",
            "4",
            "3",
            "2",
            "1",
            "Done!",
        ),
        "5/4/3/2/1",
    )
}

testShowNumbers()
