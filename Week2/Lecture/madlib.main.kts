import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame

// TODO: design the program madlib that asks for a name,
//       then a verb (ending in 'ing), then says that the
//       name did the verb all through class

// ******************************************************************
// Data
// ******************************************************************

// represents the phase of the program
enum class MadlibStage {
    NAME,
    VERB,
    FUNNY,
}

// represents the full program state
data class MadlibState(val stage: MadlibStage, val name: String, val verb: String)

val stateInit = MadlibState(MadlibStage.NAME, "", "")
val stateVerb = MadlibState(MadlibStage.VERB, "chris", "")
val stateFunny = MadlibState(MadlibStage.FUNNY, "chris", "swiping")

val promptName = "Please enter a name"
val promptVerb = "Please enter a verb ending in 'ing'"

val funny = "chris spent class swiping"

// ******************************************************************
// Functions
// ******************************************************************

// "funny" statement with a name and verb
fun renderMadlibFunny(
    name: String,
    verb: String,
): String {
    return "$name spent class $verb"
}

@EnabledTest
fun testRenderMadlibFunny() {
    testSame(
        renderMadlibFunny("chris", "swiping"),
        funny,
        "chris/swiping",
    )

    testSame(
        renderMadlibFunny("harry", "daydreaming"),
        "harry spent class daydreaming",
        "harry/daydreaming",
    )
}

// produces the text associated with each stage of madlibing
fun renderMadlib(state: MadlibState): String {
    return when (state.stage) {
        MadlibStage.NAME -> promptName
        MadlibStage.VERB -> promptVerb
        MadlibStage.FUNNY -> renderMadlibFunny(state.name, state.verb)
    }
}

@EnabledTest
fun testRenderMadlib() {
    testSame(
        renderMadlib(stateInit),
        promptName,
        "init",
    )

    testSame(
        renderMadlib(stateVerb),
        promptVerb,
        "verb",
    )

    testSame(
        renderMadlib(stateFunny),
        funny,
        "funny",
    )
}

// moves through the program flow: name -> verb -> funny
// (keeping the typed input as it goes)
fun transitionMadlib(
    state: MadlibState,
    typedInput: String,
): MadlibState {
    return when (state.stage) {
        MadlibStage.NAME -> MadlibState(MadlibStage.VERB, typedInput, "")
        MadlibStage.VERB -> MadlibState(MadlibStage.FUNNY, state.name, typedInput)
        MadlibStage.FUNNY -> state
    }
}

@EnabledTest
fun testTransitionMadlib() {
    testSame(
        transitionMadlib(stateInit, "chris"),
        stateVerb,
        "init",
    )

    testSame(
        transitionMadlib(stateVerb, "swiping"),
        stateFunny,
        "verb",
    )
}

// determines if it's funny time!
fun isFunny(state: MadlibState): Boolean {
    return state.stage == MadlibStage.FUNNY
}

@EnabledTest
fun testIsFunny() {
    testSame(
        isFunny(stateInit),
        false,
        "init",
    )

    testSame(
        isFunny(stateVerb),
        false,
        "verb",
    )

    testSame(
        isFunny(stateFunny),
        true,
        "funny",
    )
}

// plays through a simple madlib
fun madlib() {
    reactConsole(
        initialState = stateInit,
        stateToText = ::renderMadlib,
        nextState = ::transitionMadlib,
        isTerminalState = ::isFunny,
    )
}

@EnabledTest
fun testMadlib() {
    testSame(
        captureResults(::madlib, "chris", "swiping"),
        CapturedResult(
            Unit,
            promptName,
            promptVerb,
            funny,
        ),
        "chris/swiping",
    )

    testSame(
        captureResults(::madlib, "harry", "daydreaming"),
        CapturedResult(
            Unit,
            promptName,
            promptVerb,
            "harry spent class daydreaming",
        ),
        "harry/daydreaming",
    )
}

fun main() {
    // madlib()
}

runEnabledTests(this)
main()
