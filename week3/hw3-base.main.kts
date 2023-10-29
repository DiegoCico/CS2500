// <file "p1">
// <solution>
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.fileReadAsList
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame
// </solution>
// -----------------------------------------------------------------
// Homework 3, Problem 1
// -----------------------------------------------------------------

// Let's write a function that pulls together two related files
// of data and produces some useful summary data.
//
// Before coding, look at the two supplied example files:
//
// - names.txt:   one name (First Last) per line
//
// - records.txt: each line is a comma-separated list of either
//                W or L (win or lose); these correspond to the
//                student names in the first file (i.e., line 1
//                of names corresponds to line 1 of records...
//                until we don't have records for some students)
//                and represent magical battles fought, and how
//                each worked out. For example, Harry Potter has
//                had 7 magical battles, having won 5 of them.
//
// To make sure the students get adequate support, you are going
// to make a useful function to pull all of this into a single
// list of helpful info.
//

// TODO 1/1: Finish designing the function buildList that accepts
//           three parameters...
//           1. The desired size of the resulting list (you can
//              assume this will be non-negative).
//           2. A file path that contains first/last names,
//              one per line.
//           3. A file path that contains win/loss records,
//              where each line looks like "W,L,W,..." (some
//              number of comma-separated W/L's.
//
//           The function must produce a list of strings using
//           the list constructor:
//
//           val resultList = List<String>(
//               desiredSize,
//               ::initFunction
//           )
//
//           Here is how you should populate the resulting list
//           with the initFunction...
//
//           - For each entry, produce a string that combines the
//             first/last initials of the name in the first file,
//             with a summarization of their win/loss record
//             (either "is crushing it" if there are at least as
//             many W's as L's, or "needs support" otherwise).
//
//           - If for a particular entry, there is neither a name
//             nor a summarization record, produce an empty string.
//
//           - If there is a name, but there isn't a win/loss
//             record, simply indicate "is unknown" for the
//             record summarization.
//
//           - If there isn't a name, but there is a win/loss
//             record, simply indicate "PD" as the initials
//             (standing for "Person Doe").
//
//           To help, you have been supplied some tests. Here are
//           some hints...
//
//           - The string.split(delimeter) function will be useful
//             to produce a sequence from a string when there is a
//             known pattern of separation (like spaces in the
//             names file, and commas in the win/loss records).
//             Note that splitting an empty string will result in
//             a list with one empty string, which is often not
//             what you want :(
//
//           - Use sequence.size to determine how many elements are
//             in a sequence; you can similarly use the `in`
//             operator to determine if a particular index is in
//             sequence.indices
//
//           - To produce your initialization function, you likely
//             will need it to define it within buildList to make
//             sure that information about the names and win/loss
//             records are in scope.
//
//           - And lastly, since this problem has so many pieces,
//             here's a suggested method of attack...
//             1. Start by getting the contents of the two files
//                using the fileReadAsList function from the
//                Khoury library; println to make sure they match
//                what you see in the files!
//             2. Try using one of the list functions from this
//                week (cough! map) to convert names to initials,
//                and win records to summaries; you can use
//                printing and tests to make sure this is working.
//             3. Now you are in reasonable shape to start on the
//                constructor/initialization functions described
//                above :)
//

val nameUnknown = "PD"
val recordWin = "is crushing it"
val recordLoss = "needs support"
val recordUnknown = "is unknown"

// <solution>
// Converts a two-word name to initials
fun nameToInitials(name: String): String {
    val firstLast = name.split(" ")

    return "${ firstLast[0][0].uppercase() }${ firstLast[1][0].uppercase() }"
}

@EnabledTest
fun testNameToInitials() {
    testSame(
        nameToInitials("Harry Potter"),
        "HP",
        "Harry",
    )

    testSame(
        nameToInitials("Hermione Granger"),
        "HG",
        "Hermione",
    )

    testSame(
        nameToInitials("Ron Weasley"),
        "RW",
        "Ron",
    )
}

// Converts a win/loss record to a summary
fun summarizeWL(wlText: String): String {
    fun helpWin(wl: String): Boolean {
        return wl == "W"
    }

    val record = if (!wlText.isEmpty()) wlText.split(",") else emptyList<String>()
    val numWins = record.filter(::helpWin).size
    val numLosses = record.filterNot(::helpWin).size

    return when (numWins >= numLosses) {
        true -> recordWin
        false -> recordLoss
    }
}

@EnabledTest
fun testSummarizeWL() {
    testSame(
        summarizeWL(""),
        recordWin,
        "empty",
    )

    testSame(
        summarizeWL("W,W,W"),
        recordWin,
        "3-0",
    )

    testSame(
        summarizeWL("L,L,W"),
        recordLoss,
        "1-3",
    )
}

// Combines entries from a file of people and win/loss records
fun buildList(
    numEntries: Int,
    pathNames: String,
    pathWinLoss: String,
): List<String> {
    val names = fileReadAsList(pathNames).map(::nameToInitials)
    val records = fileReadAsList(pathWinLoss).map(::summarizeWL)

    fun initFunc(index: Int): String {
        if ((index in names.indices) || (index in records.indices)) {
            val name = if (index in names.indices) names[index] else nameUnknown
            val record = if (index in records.indices) records[index] else recordUnknown

            return "$name $record"
        } else {
            return ""
        }
    }

    return List<String>(numEntries, ::initFunc)
}

@EnabledTest
fun testBuildList() {
    testSame(
        buildList(2, "BADNAMES.TXT", "BADRECORDS.TXT"),
        listOf("", ""),
        "emptiness",
    )

    testSame(
        buildList(1, "BADNAMES.TXT", "records.txt"),
        listOf("PD is crushing it"),
        "PD",
    )

    testSame(
        buildList(3, "names.txt", "records.txt"),
        listOf(
            "HP is crushing it",
            "HG is crushing it",
            "RW is crushing it",
        ),
        "instructors",
    )

    testSame(
        buildList(7, "names.txt", "records.txt"),
        listOf(
            "HP is crushing it",
            "HG is crushing it",
            "RW is crushing it",
            "CC needs support",
            "NL is crushing it",
            "LL is unknown",
            "GW is unknown",
        ),
        "army",
    )
}

// </solution>
// @EnabledTest
// fun testBuildList() {
//     testSame(
//         buildList(2, "BADNAMES.TXT", "BADRECORDS.TXT"),
//         listOf("", ""),
//         "emptiness",
//     )
//
//     testSame(
//         buildList(1, "BADNAMES.TXT", "records.txt"),
//         listOf("PD is crushing it"),
//         "PD",
//     )
//
//     testSame(
//         buildList(3, "names.txt", "records.txt"),
//         listOf(
//             "HP is crushing it",
//             "HG is crushing it",
//             "RW is crushing it",
//         ),
//         "instructors",
//     )
//
//     testSame(
//         buildList(7, "names.txt", "records.txt"),
//         listOf(
//             "HP is crushing it",
//             "HG is crushing it",
//             "RW is crushing it",
//             "CC needs support",
//             "NL is crushing it",
//             "LL is unknown",
//             "GW is unknown",
//         ),
//         "army",
//     )
// }
// <file "p2">
// -----------------------------------------------------------------
// Homework 3, Problem 2
// -----------------------------------------------------------------

// TODO 1/4: Design the data type FlashCard to represent a single
//           flash card. You should be able to represent the text
//           prompt on the front of the card as well as the text
//           answer on the back. Include at least 3 example cards
//           (which will come in handy later for tests!).
//

// <solution>
// Represents a flash card, including the text on
// both the front (prompt) and back (answer).
data class FlashCard(val front: String, val back: String)

val qMA = "What is the capital of Massachusetts, USA?"
val aMA = "Boston"

val qCA = "What is the capital of California, USA?"
val aCA = "Sacramento"

val qUK = "What is the capital of the United Kingdom?"
val aUK = "London"

val fcMA = FlashCard(qMA, aMA)
val fcCA = FlashCard(qCA, aCA)
val fcUK = FlashCard(qUK, aUK)
// </solution>
// TODO 2/4: Design the data type Deck to represent a deck of
//           flash cards. The deck should have a name, as well
//           as a sequence of flash cards.
//
//           Include at least 2 example decks based upon the
//           card examples above.
//

// <solution>
val nameUS = "US Capitals"
val nameWorld = "World Capitals"

// Represents a deck with a name and list of cards
data class Deck(val name: String, val cards: List<FlashCard>)

val deckUS = Deck(nameUS, listOf(fcMA, fcCA))
val deckWorld = Deck(nameWorld, listOf(fcMA, fcCA, fcUK))
// </solution>
// TODO 3/4: Design the predicate areAllOneWordAnswers that
//           determines if the backs of all the cards in a deck
//           are a single word (i.e., have no spaces, which
//           includes a card with a blank back).
//
//           Hint: hidden in the name of this function is a
//                 reminder of a useful list function to use :)
//

// A couple potentially helpful examples for tests
// val fcEmptyBack = FlashCard("Front", "")
// val fcLongBack = FlashCard("Front", "Long answer")

// <solution>
val fcEmptyBack = FlashCard("Front", "")
val fcLongBack = FlashCard("Front", "Long answer")

// Does the back of this card not contain a space?
fun oneWordBack(fc: FlashCard): Boolean {
    fun helpOneWord(s: String): Boolean {
        return (s.isEmpty()) || (s.split(" ").size == 1)
    }

    return helpOneWord(fc.back)
}

@EnabledTest
fun testOneWordBack() {
    testSame(
        oneWordBack(fcEmptyBack),
        true,
        "empty",
    )

    testSame(
        oneWordBack(fcMA),
        true,
        "MA",
    )

    testSame(
        oneWordBack(fcLongBack),
        false,
        "Long",
    )
}

// determines if the backs of all the cards are a single word
fun areAllOneWordAnswers(d: Deck): Boolean {
    return d.cards.all(::oneWordBack)
}

@EnabledTest
fun testAreAllOneWordAnswers() {
    testSame(
        areAllOneWordAnswers(deckUS),
        true,
        "US",
    )

    testSame(
        areAllOneWordAnswers(deckWorld),
        true,
        "World",
    )

    testSame(
        areAllOneWordAnswers(
            Deck(
                "Test",
                listOf(
                    fcMA,
                    FlashCard("Question", "Long answer"),
                ),
            ),
        ),
        false,
        "Made up",
    )
}
// </solution>
// TODO 4/4: Design the predicate anyContainsPhrase that determines
//           if any of the cards in a deck contain the supplied
//           phrase.
//
//           Hints:
//           - string1.contains(string2) will be quite useful
//             here :)
//           - Again, the name of this function hints at a useful
//             list function we learned!
//
// <solution>

fun containsPhrase(
    deck: Deck,
    phrase: String,
): Boolean {
    fun helpContainsPhrase(fc: FlashCard): Boolean {
        return fc.front.contains(phrase) ||
            fc.back.contains(phrase)
    }

    return deck.cards.any(::helpContainsPhrase)
}

@EnabledTest
fun testContainsPhrase() {
    testSame(
        containsPhrase(deckWorld, "howdy"),
        false,
        "bad",
    )

    testSame(
        containsPhrase(deckWorld, "capital of"),
        true,
        "capital of",
    )

    testSame(
        containsPhrase(deckWorld, "London"),
        true,
        "London",
    )
}
// </solution>
// <file "p3">
// -----------------------------------------------------------------
// Homework 3, Problem 3
// -----------------------------------------------------------------

// TODO 1/1: Design the program showNumbers that uses reactConsole
//           to simply print to the screen all the numbers in a
//           supplied list. For example, running...
//
//           showNumbers(listOf(5, 4, 3, 2, 1))
//
//           should show at the terminal...
//
//
//           5
//
//           4
//
//           3
//
//           2
//
//           1
//
//           Done!
//
//
//           (Note: this assumes the user doesn't type anything
//           between the numbers; even if they did, you should
//           ignore it in the transition function.)
//
//           Remember to start by thinking about your state
//           representation. While there are multiple approaches,
//           here are a couple suggestions:
//
//           1. Represent state as a list; transitioning to the
//              next list then involves list.drop(1) to produce
//              a new list without the first element of the old
//              list; you'll want to check for list.isEmpty()
//
//           2. Design a data class that keeps the supplied list
//              AND the current index (that you can increment
//              until it is no longer valid)
//
//           To help, you have a couple tests, which don't at all
//           depend on how *you* choose to represent state :)
//
//           If you need a refresher on reactConsole, recall that
//           there are a series of videos/files on Canvas walking
//           you through the design process and some example
//           programs :)
//

// <solution>

// State Representation #1: just the list,
//                          and we drop away the first element

// State Representation #2: structure that keeps the list,
//                          along with the current index

data class ListAndIndex(val numList: List<Int>, val index: Int)

val laiInit = ListAndIndex(listOf(5, 4, 3, 2, 1), 0)
val lai1 = ListAndIndex(listOf(5, 4, 3, 2, 1), 1)
val laiPenultimate = ListAndIndex(listOf(5, 4, 3, 2, 1), 4)
val laiDone = ListAndIndex(listOf(5, 4, 3, 2, 1), 5)

// -----------------------------------------------------------------

// Determines if the list is empty (and so the program is done)
fun isListEmpty(numList: List<Int>): Boolean {
    return numList.isEmpty()
}

@EnabledTest
fun testIsListEmpty() {
    testSame(
        isListEmpty(emptyList<Int>()),
        true,
        "empty",
    )

    testSame(
        isListEmpty(listOf(5, 4, 3, 2, 1)),
        false,
        "non-empty",
    )
}

// Determines if the current index is valid
// with respect to the list
fun isIndexInvalid(state: ListAndIndex): Boolean {
    return state.index !in state.numList.indices
}

@EnabledTest
fun testIsIndexInvalid() {
    testSame(
        isIndexInvalid(laiInit),
        false,
        "init",
    )

    testSame(
        isIndexInvalid(lai1),
        false,
        "1",
    )

    testSame(
        isIndexInvalid(laiPenultimate),
        false,
        "penultimate",
    )

    testSame(
        isIndexInvalid(laiDone),
        true,
        "invalid",
    )
}

// Outputs the first number, or Done! if empty
fun renderList1(numList: List<Int>): String {
    return when (isListEmpty(numList)) {
        true -> "Done!"
        false -> "${ numList[0] }"
    }
}

@EnabledTest
fun testRenderList1() {
    testSame(
        renderList1(emptyList<Int>()),
        "Done!",
        "empty",
    )

    testSame(
        renderList1(listOf(5, 4, 3, 2, 1)),
        "5",
        "non-empty",
    )
}

// Outputs the current number, or Done! if empty
fun renderList2(state: ListAndIndex): String {
    return when (isIndexInvalid(state)) {
        false -> "${ state.numList[ state.index ] }"
        true -> "Done!"
    }
}

@EnabledTest
fun testRenderList2() {
    testSame(
        renderList2(laiInit),
        "5",
        "init",
    )

    testSame(
        renderList2(lai1),
        "4",
        "1",
    )

    testSame(
        renderList2(laiPenultimate),
        "1",
        "penultimate",
    )

    testSame(
        renderList2(laiDone),
        "Done!",
        "done",
    )
}

// Moves to the next element in the list,
// ignoring the keyboard input
fun transitionList1(
    numList: List<Int>,
    ignoredInput: String,
): List<Int> {
    return when (isListEmpty(numList)) {
        true -> numList
        false -> numList.drop(1)
    }
}

@EnabledTest
fun testTransitionList1() {
    testSame(
        transitionList1(listOf(5, 4, 3, 2, 1), ""),
        listOf(4, 3, 2, 1),
        "5 -> 4",
    )

    testSame(
        transitionList1(listOf(1), "howdy"),
        emptyList<Int>(),
        "1 -> 0",
    )
}

// Moves to the next element in the list,
// ignoring the keyboard input
fun transitionList2(
    state: ListAndIndex,
    ignoredInput: String,
): ListAndIndex {
    return when (isIndexInvalid(state)) {
        false ->
            ListAndIndex(
                numList = state.numList,
                index = state.index + 1,
            )
        true -> state
    }
}

@EnabledTest
fun testTransitionList2() {
    testSame(
        transitionList2(laiInit, ""),
        lai1,
        "init -> 1",
    )

    testSame(
        transitionList2(laiPenultimate, "howdy"),
        laiDone,
        "penultimate -> done",
    )
}

// Outputs all the numbers in a supplied list
// (shows two different versions, given
//  different state representations; use
//  just one at a time)
fun showNumbers(numList: List<Int>) {
    reactConsole(
        initialState = numList,
        stateToText = ::renderList1,
        nextState = ::transitionList1,
        isTerminalState = ::isListEmpty,
    )

    // reactConsole(
    //     initialState = ListAndIndex(numList, 0),
    //     stateToText = ::renderList2,
    //     nextState = ::transitionList2,
    //     isTerminalState = ::isIndexInvalid,
    // )
}

// one set of final tests, doesn't depend upon
// internal state representation :)
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
// </solution>
// @EnabledTest
// fun testShowNumbers() {
//     // makes a captureResults-friendly function :)
//     fun helpTest(numList: List<Int>): () -> Unit {
//         fun showMyNumbers() {
//             showNumbers(numList)
//         }

//         return ::showMyNumbers
//     }

//     testSame(
//         captureResults(
//             helpTest(emptyList<Int>()),
//             "",
//         ),
//         CapturedResult(
//             Unit,
//             "Done!",
//         ),
//         "empty",
//     )

//     testSame(
//         captureResults(
//             helpTest(listOf(5, 4, 3, 2, 1)),
//             "",
//             "",
//             "",
//             "",
//             "",
//             "",
//         ),
//         CapturedResult(
//             Unit,
//             "5",
//             "4",
//             "3",
//             "2",
//             "1",
//             "Done!",
//         ),
//         "5/4/3/2/1",
//     )
// }
// <solution>
fun main() {
    showNumbers(listOf(5, 4, 3, 2, 1))
}

runEnabledTests(this)
main()
// </solution>
