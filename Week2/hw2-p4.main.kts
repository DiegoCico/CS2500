// -----------------------------------------------------------------
// Homework 2, Problem 4
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.reactConsole
import khoury.testSame

val lyricsABC = "A-B-C"
val lyrics123 = "Easy as 1-2-3"
val lyricsDone = "Baby you and me girl!"
var numberOfLoops = 0

// Represents state in the song
// (no changes here)
enum class SongState {
    START,
    ABC,
    EASY123,
    DONE,
}

// store the states
data class Controller(var event: SongState)

// prints out each state
fun loopStateToText(c: Controller): String {
    return when (c.event) {
        SongState.ABC -> lyricsABC
        SongState.EASY123 -> lyrics123
        else -> lyricsDone
    }
}

// switches the states
fun loopNextState(
    c: Controller,
    s: String,
): Controller {
    if (c.event == SongState.ABC) {
        return Controller(SongState.EASY123)
    } else if (c.event == SongState.EASY123) {
        if (input() == "done") {
            return Controller(SongState.DONE)
        } else {
            numberOfLoops++
            return Controller(SongState.ABC)
        }
    }
    return Controller(SongState.ABC)
}

// finds out when it is done
fun isDone(c: Controller): Boolean {
    if (c.event == SongState.DONE) {
        return true
    }
    return false
}

fun loopSong(): Int {
    return reactConsole(
        initialState = Controller(SongState.ABC),
        stateToText = ::loopStateToText,
        nextState = ::loopNextState,
        isTerminalState = ::isDone,
    ).count
}

@EnabledTest
fun testLoopSong() {
    testSame(
        captureResults(
            ::loopSong,
            "",
            "done",
        ),
        CapturedResult(
            1,
            lyricsABC,
            lyrics123,
            lyricsDone,
        ),
        "quick",
    )

    testSame(
        captureResults(
            ::loopSong,
            "a",
            "b",
            "done",
            "c",
            "d",
            "done",
        ),
        CapturedResult(
            3,
            lyricsABC,
            lyrics123,
            lyricsABC,
            lyrics123,
            lyricsABC,
            lyrics123,
            lyricsDone,
        ),
        "longer",
    )
}

testLoopSong()
