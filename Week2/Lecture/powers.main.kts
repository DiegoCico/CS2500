import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame

// TODO: design the program powers to output the sequence: 1, 2, 4, ...
//       doubling, until it's greater than 100, returning the
//       first power of 2 greater than 100

val thresh: Int = 100

// Displays the current number
fun renderPowers(num: Int): String {
    return "Current number: $num"
}

@EnabledTest
fun testRenderPowers() {
    testSame(
        renderPowers(1),
        "Current number: 1",
        "1",
    )

    testSame(
        renderPowers(64),
        "Current number: 64",
        "64",
    )
}

// returns the supplied value doubled,
// ignoring any input from the console
fun transitionPowers(
    num: Int,
    typedInput: String,
): Int {
    return 2 * num
}

@EnabledTest
fun testTransitionPowers() {
    testSame(
        transitionPowers(1, "howdy"),
        2,
        "1",
    )

    testSame(
        transitionPowers(64, ""),
        128,
        "64",
    )
}

// determines when the program stops
fun terminalPowers(num: Int): Boolean {
    return num > thresh
}

@EnabledTest
fun testTerminalPowers() {
    testSame(
        terminalPowers(1),
        false,
        "1",
    )

    testSame(
        terminalPowers(64),
        false,
        "64",
    )

    testSame(
        terminalPowers(128),
        true,
        "128",
    )
}

// Just outputs "Done!"
fun terminalRenderPowers(num: Int): String {
    return "Done!"
}

@EnabledTest
fun testTerminalRenderPowers() {
    testSame(
        terminalRenderPowers(300),
        "Done!",
        "300",
    )

    testSame(
        terminalRenderPowers(128),
        "Done!",
        "128",
    )
}

// shows counting from 1, 2, 4, 8, ... until
// a threshold is met, returning the last value
fun powers(): Int {
    return reactConsole(
        initialState = 1,
        stateToText = ::renderPowers,
        nextState = ::transitionPowers,
        isTerminalState = ::terminalPowers,
        terminalStateToText = ::terminalRenderPowers,
    )
}

@EnabledTest
fun testPowers() {
    testSame(
        captureResults(
            ::powers,
            "hi 1",
            "hi 2",
            "hi 4",
            "hi 8",
            "hi 16",
            "hi 32",
            "hi 64",
        ),
        CapturedResult(
            128,
            "Current number: 1",
            "Current number: 2",
            "Current number: 4",
            "Current number: 8",
            "Current number: 16",
            "Current number: 32",
            "Current number: 64",
            "Done!",
        ),
    )
}

fun main() {
    powers()
}

runEnabledTests(this)
main()
