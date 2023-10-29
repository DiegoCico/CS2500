import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.isAnInteger
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame

// TODO: design the program getValid that continually prompts
//       for a valid integer to be typed

// ******************************************************************
// Data
// ******************************************************************

// represents either not having a valid number
// or having one (and its associated value)
sealed class GetValidState {
    object Invalid : GetValidState()

    data class ValidNum(val num: Int) : GetValidState()
}

val stateInvalid = GetValidState.Invalid
val state1 = GetValidState.ValidNum(1)
val state7 = GetValidState.ValidNum(7)

val prompt: String = "Please enter an integer"

// ******************************************************************
// Functions
// ******************************************************************

// converts a state to either a prompt or the valid number
fun renderGetValid(state: GetValidState): String {
    return when (state) {
        is GetValidState.Invalid -> prompt
        is GetValidState.ValidNum -> "Got: ${ state.num }"
    }
}

@EnabledTest
fun testRenderGetValid() {
    testSame(
        renderGetValid(stateInvalid),
        prompt,
        "invalid",
    )

    testSame(
        renderGetValid(state1),
        "Got: 1",
        "1",
    )

    testSame(
        renderGetValid(state7),
        "Got: 7",
        "7",
    )
}

// Indicates whether a valid number was typed,
// basically ignoring the current state
fun transitionGetValid(
    state: GetValidState,
    typedInput: String,
): GetValidState {
    return when (isAnInteger(typedInput)) {
        true -> GetValidState.ValidNum(typedInput.toInt())
        false -> GetValidState.Invalid
    }
}

@EnabledTest
fun testTransitionGetValid() {
    testSame(
        transitionGetValid(stateInvalid, "howdy"),
        stateInvalid,
        "howdy",
    )

    testSame(
        transitionGetValid(stateInvalid, "1"),
        state1,
        "1",
    )

    testSame(
        transitionGetValid(stateInvalid, "7"),
        state7,
        "7",
    )
}

// terminal state is having a valid number
fun hasValidNumber(state: GetValidState): Boolean {
    return when (state) {
        is GetValidState.Invalid -> false
        is GetValidState.ValidNum -> true
    }
}

@EnabledTest
fun testHasValidNumber() {
    testSame(
        hasValidNumber(stateInvalid),
        false,
        "invalid",
    )

    testSame(
        hasValidNumber(state1),
        true,
        "1",
    )

    testSame(
        hasValidNumber(state7),
        true,
        "7",
    )
}

// Asks for a valid integer until one is supplied
fun getValid(): Int {
    val result =
        reactConsole(
            initialState = GetValidState.Invalid,
            stateToText = ::renderGetValid,
            nextState = ::transitionGetValid,
            isTerminalState = ::hasValidNumber,
        )

    // we know result is a ValidNum,
    // Kotlin can't be sure!
    return when (result) {
        is GetValidState.Invalid -> -1
        is GetValidState.ValidNum -> result.num
    }
}

@EnabledTest
fun testGetValid() {
    testSame(
        captureResults(::getValid, "1"),
        CapturedResult(
            1,
            prompt,
            "Got: 1",
        ),
    )

    testSame(
        captureResults(::getValid, "howdy", "7"),
        CapturedResult(
            7,
            prompt,
            prompt,
            "Got: 7",
        ),
    )
}

fun main() {
    println("returned: ${ getValid() }")
}

runEnabledTests(this)
main()
