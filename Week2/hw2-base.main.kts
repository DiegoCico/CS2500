// <file "p1">
// <solution>
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.isAnInteger
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame
// </solution>
// -----------------------------------------------------------------
// Homework 2, Problem 1
// -----------------------------------------------------------------

// TODO 1/1: Design the predicate startsWithY that determines if
//           the supplied string starts with the letter "y"
//           (either upper or lowercase).
//
//           Hints:
//            - The string.startsWith(prefix) function will help
//              evaluate the prefix (even if the string is too
//              short).
//            - The string.lowercase/uppercase() functions help
//              you not worry about case.
//            - Remember that "designing" a function means to
//              document and test it!
//
// <solution>
// Determines if the first letter of the string is "y"
fun startsWithY(s: String): Boolean {
    return s.uppercase().startsWith("Y")
}

@EnabledTest
fun testStartsWithY() {
    testSame(
        startsWithY(""),
        false,
        "empty",
    )

    testSame(
        startsWithY("yes"),
        true,
        "lowercase",
    )

    testSame(
        startsWithY("Yup!"),
        true,
        "uppercase",
    )

    testSame(
        startsWithY("howdy"),
        false,
        "something else",
    )
}
// </solution>
// <file "p2">
// -----------------------------------------------------------------
// Homework 2, Problem 2
// -----------------------------------------------------------------

// TODO 1/1: Finish designing the function wakeupTime that asks at
//           the console what hour a person likes to wake up in
//           the morning. Depending on their response, your
//           function provides a particular response. You should
//           NOT use reactConsole for this problem.
//
//           To help, you have been supplied a set of tests --
//           uncomment these and once your function passes, you
//           should be in good shape!
//
//           If you are having trouble, try calling your function
//           from main and debug!
//

val wakeupPrompt = "What hour in the morning (1-11) do you like to wakeup?"
val wakeupNotANumber = "You did not enter a number :("
val wakeupNotInRange = "You did not enter an hour from 1-11am"
val wakeupEarly = "Before 8am? Early bird catches the worm!"
val wakeupOther = "8am or later? Coffee time!"

// <solution>
// Provides a response about a typed wakeup hour
fun wakeupTime() {
    println(wakeupPrompt)

    val morningHour = input()

    if (!isAnInteger(morningHour)) {
        println(wakeupNotANumber)
    } else {
        val hourNum = morningHour.toInt()

        println(
            when {
                (hourNum < 1) || (hourNum > 11) -> wakeupNotInRange
                (hourNum < 8) -> wakeupEarly
                else -> wakeupOther
            },
        )
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
                wakeupPrompt,
                expectedOut,
            ),
            consoleIn,
        )
    }

    testHelp("howdy", wakeupNotANumber)
    testHelp("0", wakeupNotInRange)
    testHelp("12", wakeupNotInRange)
    testHelp("5", wakeupEarly)
    testHelp("8", wakeupOther)
    testHelp("11", wakeupOther)
}
// </solution>
// @EnabledTest
// fun testWakeupTime() {
//     // helps to test, given what is typed at the console
//     // and what the expected output should be
//     fun testHelp(
//         consoleIn: String,
//         expectedOut: String,
//     ) {
//         testSame(
//             captureResults(::wakeupTime, consoleIn),
//             CapturedResult(
//                 Unit,
//                 wakeupPrompt,
//                 expectedOut,
//             ),
//             consoleIn,
//         )
//     }

//     testHelp("howdy", wakeupNotANumber)
//     testHelp("0", wakeupNotInRange)
//     testHelp("12", wakeupNotInRange)
//     testHelp("5", wakeupEarly)
//     testHelp("8", wakeupOther)
//     testHelp("11", wakeupOther)
// }
// <file "p3">
// -----------------------------------------------------------------
// Homework 2, Problem 3
// -----------------------------------------------------------------

// We are making nametags for a (magical) student event, and want
// to make sure they have enough space for all the letters in each
// name. We are also formatting the nametags so that they have a
// consistent format: Last, First Middle - this way the teachers
// can accurately praise/critique their students.
//
// TODO 1/1: Given the data types and examples below, design the
//           function numCharsNeeded that takes a magical pair and
//           returns the number of characters that would be
//           necessary to represent the longer student's formatted
//           name.
//
//           For instance, if Hermione and Ron are paired, her
//           formatted name is "Granger, Hermione Jean" (22
//           characters) and his is "Weasley, Ron Bilius" (19
//           characters), and so the function should return 22.
//
//           Note the nested data types, and so duplicated work -
//           let type-driven development lead you to an effective
//           decomposition of well-designed functions :)
//

// represents a person's first/middle/last names
data class Name(val first: String, val middle: String, val last: String)

val harryJPotter = Name("Harry", "James", "Potter")
val hermioneJeanGranger = Name("Hermione", "Jean", "Granger")
val ronBiliusWeasley = Name("Ron", "Bilius", "Weasley")

// represents a pairing of two names
data class MagicPair(val p1: Name, val p2: Name)

val magicHarryRon = MagicPair(harryJPotter, ronBiliusWeasley)
val magicHarryHermione = MagicPair(harryJPotter, hermioneJeanGranger)
val magicHermioneRon = MagicPair(hermioneJeanGranger, ronBiliusWeasley)
// <solution>

// Formats the supplied name as "Last, First Middle"
fun formatName(n: Name): String {
    return "${ n.last }, ${ n.first } ${ n.middle }"
}

@EnabledTest
fun testFormatName() {
    testSame(
        formatName(harryJPotter),
        "Potter, Harry James",
        "harry",
    )

    testSame(
        formatName(hermioneJeanGranger),
        "Granger, Hermione Jean",
        "hermione",
    )

    testSame(
        formatName(ronBiliusWeasley),
        "Weasley, Ron Bilius",
        "ron",
    )
}

// Calculates the number of characters needed to
// capture either of the names (if formatted)
fun numCharsNeeded(mpair: MagicPair): Int {
    return maxOf(
        formatName(mpair.p1).length,
        formatName(mpair.p2).length,
    )
}

@EnabledTest
fun testNumCharsNeeded() {
    testSame(
        numCharsNeeded(magicHermioneRon),
        22,
        "Hermione/Ron",
    )

    testSame(
        numCharsNeeded(magicHarryHermione),
        22,
        "Harry/Hermione",
    )

    testSame(
        numCharsNeeded(magicHarryRon),
        19,
        "Harry/Ron",
    )
}

// </solution>
// <file "p4">
// -----------------------------------------------------------------
// Homework 2, Problem 4
// -----------------------------------------------------------------

// Now it's time to write your first full, if not a bit silly, app!
//
// TODO 1/1: Finish designing the program loopSong, using
//           reactConsole, that prints...
//
//           A-B-C
//
//           then...
//
//           Easy as 1-2-3
//
//           repeatedly until the user types "done" in response
//           to "Easy as 1-2-3", at which point the program
//           prints...
//
//           Baby you and me girl!
//
//           and returns the number of times the song looped
//           (meaning, how many times it printed "Easy as 1-2-3").
//
//           To help, you have an enumeration that represents the
//           beginning of the song, and each subsequent situation.
//           You also have tests - you just need to finish
//           loopSong, as well as designing the helper functions
//           that handle each type of event.
//
//           Suggestions:
//           - First, design how you will represent the state of
//             your program; SongState is useful, but you'll also
//             need to keep track of how many times the song looped
//             (hmm... more than piece of data in a single value,
//             what helps us there?).
//           - Next, have loopSong call reactConsole, providing
//             the initial state (ideally an example), and a set
//             of yet-to-be-designed helper functions, not
//             forgetting to return the number of song loops.
//           - Now, figure out the parameter/return types of each
//             helper, based upon how reactConsole works with your
//             data design.
//           - Then, write some tests to capture how these helper
//             functions *should* operate...
//             * songStateToText: should be producing the
//                                appropriate lyrics based on the
//                                situation (irrespective of loop
//                                count).
//             * nextSongState: should be figuring out what is the
//                              next situation based upon the
//                              current situation and what was
//                              typed (which matters only when
//                              "Easy as 1-2-3" was printed).
//             * isDone: should only return true when the current
//                       situation is that the song is done!
//            - And lastly, write the code for your helpers (as
//              informed by the types they use and tests!). You
//              might do these last two steps for each helper
//              individually; you can then test piece-by-piece if
//              you comment-out our tests and loopSong.
//
//           As with any interactive program, feel free to debug
//           by directly calling your program in main!
//

val lyricsABC = "A-B-C"
val lyrics123 = "Easy as 1-2-3"
val lyricsDone = "Baby you and me girl!"

// Represents state in the song
// (no changes here)
enum class SongState {
    START,
    ABC,
    EASY123,
    DONE,
}

// <solution>

// Represents song situation, as well as how many times
// the program has looped
data class LoopState(val songSituation: SongState, val count: Int)

val loopStart = LoopState(SongState.START, 0)
val loopEasy1 = LoopState(SongState.EASY123, 1)
val loopDone1 = LoopState(SongState.DONE, 1)
val loopABC1 = LoopState(SongState.ABC, 1)
val loopEasy2 = LoopState(SongState.EASY123, 2)
val loopDone2 = LoopState(SongState.DONE, 2)

// Produces the lyrics associated with each song state
fun loopStateToText(loopState: LoopState): String {
    // easily could be a helper, if lyrics
    // were useful elsewhere in the program
    return when (loopState.songSituation) {
        SongState.START -> lyricsABC
        SongState.ABC -> lyricsABC
        SongState.EASY123 -> lyrics123
        SongState.DONE -> lyricsDone
    }
}

@EnabledTest
fun testLoopStateToText() {
    testSame(
        loopStateToText(loopStart),
        lyricsABC,
        "start",
    )

    testSame(
        loopStateToText(loopEasy1),
        lyrics123,
        "123",
    )

    testSame(
        loopStateToText(loopABC1),
        lyricsABC,
        "abc",
    )

    testSame(
        loopStateToText(loopDone1),
        lyricsDone,
        "done",
    )
}

// Start goes to 123; then loop on ABC/123 until
// "done" is typed at 123, at which point done!
fun nextLoopState(
    loopState: LoopState,
    typedInput: String,
): LoopState {
    // produces the next loop state based upon the next
    // song situation and whether the count should be incremented
    fun helpNextLoopState(
        newSituation: SongState,
        incrementCount: Boolean,
    ): LoopState {
        return LoopState(
            newSituation,
            when (incrementCount) {
                true -> loopState.count + 1
                false -> loopState.count
            },
        )
    }

    return when (loopState.songSituation) {
        SongState.START -> helpNextLoopState(SongState.EASY123, true)
        SongState.ABC -> helpNextLoopState(SongState.EASY123, true)
        SongState.EASY123 ->
            helpNextLoopState(
                when {
                    typedInput == "done" -> SongState.DONE
                    else -> SongState.ABC
                },
                false,
            )
        SongState.DONE -> loopState
    }
}

@EnabledTest
fun testNextLoopState() {
    testSame(
        nextLoopState(loopStart, "done"),
        loopEasy1,
        "start",
    )

    testSame(
        nextLoopState(loopEasy1, "done"),
        loopDone1,
        "123-1/done",
    )

    testSame(
        nextLoopState(loopEasy1, "howdy"),
        loopABC1,
        "123-1/not done",
    )

    testSame(
        nextLoopState(loopABC1, "done"),
        loopEasy2,
        "abc",
    )

    testSame(
        nextLoopState(loopEasy2, "done"),
        loopDone2,
        "123-2/done",
    )
}

// the program is over when the state is DONE
fun isDone(loopState: LoopState): Boolean {
    return (loopState.songSituation == SongState.DONE)
}

@EnabledTest
fun testIsDone() {
    testSame(
        isDone(loopStart),
        false,
        "start",
    )

    testSame(
        isDone(loopEasy1),
        false,
        "123-1",
    )

    testSame(
        isDone(loopDone1),
        true,
        "done-1",
    )

    testSame(
        isDone(loopABC1),
        false,
        "abc-1",
    )

    testSame(
        isDone(loopEasy2),
        false,
        "123-2",
    )

    testSame(
        isDone(loopDone2),
        true,
        "done-2",
    )
}

// Loops on ABC/123 till "done" is typed
// on "Easy as 1-2-3" (no changes here)
fun loopSong(): Int {
    return reactConsole(
        initialState = loopStart,
        stateToText = ::loopStateToText,
        nextState = ::nextLoopState,
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
// </solution>
// @EnabledTest
// fun testLoopSong() {
//     testSame(
//         captureResults(
//             ::loopSong,
//             "",
//             "done",
//         ),
//         CapturedResult(
//             1,
//             lyricsABC,
//             lyrics123,
//             lyricsDone,
//         ),
//         "quick",
//     )

//     testSame(
//         captureResults(
//             ::loopSong,
//             "a",
//             "b",
//             "done",
//             "c",
//             "d",
//             "done",
//         ),
//         CapturedResult(
//             3,
//             lyricsABC,
//             lyrics123,
//             lyricsABC,
//             lyrics123,
//             lyricsABC,
//             lyrics123,
//             lyricsDone,
//         ),
//         "longer",
//     )
// }
// <solution>
fun main() {
    loopSong()
}

runEnabledTests(this)
main()
// </solution>
