// -----------------------------------------------------------------
// Homework 6, Problem 3
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.runEnabledTests
import khoury.testSame

// goes through every variable according to reactConsole
fun <S> funcReactConsole(
    initialState: S,
    stateToText: (S) -> String,
    nextState: (S, String) -> S,
    isTerminalState: (S) -> Boolean,
    terminalStateToText: (S) -> String,
): S {
    var nS = initialState
    while (true) {
        println(stateToText(nS))
        nS = nextState(nS, input())
        if (isTerminalState(nS)) {
            break
        }
    }
    println(terminalStateToText(nS))
    return nS
}

@EnabledTest
fun testFunReactConsole() {
    testSame(
        captureResults(
            {
                funcReactConsole(
                    initialState = 1,
                    stateToText = { s -> "$s" },
                    nextState = { s, _ -> 2 * s },
                    isTerminalState = { s -> s >= 100 },
                    terminalStateToText = { _ -> "fin." },
                )
            },
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ),
        CapturedResult(
            128,
            "1", "2", "4", "8", "16", "32", "64", "fin.",
        ),
        "doubling",
    )

    testSame(
        captureResults(
            {
                funcReactConsole(
                    initialState = "start",
                    stateToText = { s -> "$s!" },
                    nextState = { _, kbInput -> kbInput },
                    isTerminalState = { s -> s == "done" },
                    terminalStateToText = { s -> "$s!!!" },
                )
            },
            "howdy",
            "cool",
            "getting old",
            "done",
        ),
        CapturedResult(
            "done",
            "start!",
            "howdy!",
            "cool!",
            "getting old!",
            "done!!!",
        ),
        "done yet?",
    )
}

// member functions necessary for a
// class to utilize the imperative
// reactConsole below
interface IReactStateImp {
    // render the state
    fun toText(): String

    // transition to the next state
    // based upon supplied kb input
    fun transition(kbInput: String)

    // terminal state predicate
    fun isTerminal(): Boolean

    // render terminal states
    fun terminalToText(): String
}

// Follows along the IReactStateImp with its own implements to make it work as intended
data class DoublingState(private var n: Int) : IReactStateImp {
    override fun toText(): String = n.toString()

    override fun transition(i: String) {
        n *= 2
    }

    override fun isTerminal(): Boolean = if (n >= 128) true else false

    override fun terminalToText(): String = "fin."

    fun getNum(): Int = n
}

// Follows along the IReactStateImp with its own implements to make it work as intended
data class DoneYetState(var word: String, val end: String) : IReactStateImp {
    override fun toText(): String = "$word!"

    override fun transition(i: String) {
        word = i
    }

    override fun isTerminal(): Boolean = if (word == end) true else false

    override fun terminalToText(): String = "$end!!!"
}

// implements reactConsole imperatively
// (using an object-oriented state)
fun impReactConsole(state: IReactStateImp) {
    while (!state.isTerminal()) {
        println(state.toText())

        state.transition(input())
    }
    println(state.terminalToText())
}

@EnabledTest
fun testImpReactConsole() {
    // creates the initial state
    // for the doubling program
    val doubleState = DoublingState(1)

    // tests (primarily) the printed program output
    testSame(
        captureResults(
            { impReactConsole(doubleState) },
            "",
            "",
            "",
            "",
            "",
            "",
            "",
        ),
        CapturedResult(
            Unit,
            "1", "2", "4", "8", "16", "32", "64", "fin.",
        ),
        "doubling: I/O",
    )

    // tests (primarily) the final program state
    testSame(
        doubleState.getNum(),
        128,
        "doubling: final state",
    )

    // creates the initial state
    // for the done-yet program
    // (with both starting phrase
    // and phrase that signals
    // the end)
    val startPhrase = "start"
    val exitPhrase = "done"
    val doneYetState = DoneYetState(startPhrase, exitPhrase)

    // tests the program printed output (as dictated
    // by keyboard input)
    testSame(
        captureResults(
            { impReactConsole(doneYetState) },
            "howdy",
            "cool",
            "getting old",
            exitPhrase,
        ),
        CapturedResult(
            Unit,
            "$startPhrase!",
            "howdy!",
            "cool!",
            "getting old!",
            "$exitPhrase!!!",
        ),
        "done yet?: I/O",
    )
}

runEnabledTests(this)
