// -----------------------------------------------------------------
// Project: Part 1, Summary
// -----------------------------------------------------------------

import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.fileExists
import khoury.fileReadAsList
import khoury.linesToString
import khoury.reactConsole
import khoury.runEnabledTests
import khoury.testSame

// cards
data class FlashCard(val front: String, val back: String)
val f1: FlashCard = FlashCard("what is a string", "texts")
val f2: FlashCard = FlashCard("what is a Int", "wholenumbers")
val f3: FlashCard = FlashCard("What is a double", "decimals")
val flashArray: List<FlashCard> = listOf(f1, f2, f3)
val f4: FlashCard = FlashCard("what your name", "Diego cicotoste")
val f5: FlashCard = FlashCard("what is your major", "Comp Sci")
val f6: FlashCard = FlashCard("How old are you", "18 years old")
val flashArray2: List<FlashCard> = listOf(f4, f5, f6)

// Decks
data class Deck(val name: String, val cards: List<FlashCard>)
val d1: Deck = Deck("coding", flashArray)
val d2: Deck = Deck("personal", flashArray2)

// -----------------------------------------------------------------
// Generating flash cards
// -----------------------------------------------------------------

// squares the number given
fun kthPerfectSquare(a: Int): Int {
    return (a + 1) * (a + 1)
}

// ads the number inside an List of Flashcards and it returns it when it is done
fun perfectSquares(count: Int): List<FlashCard> {
    fun initFunc(i: Int): FlashCard {
        return FlashCard("${i + 1}^2 = ?", "${kthPerfectSquare(i)}")
    }

    return List<FlashCard>(count, ::initFunc)
}

@EnabledTest
fun testPerfectSquares() {
    testSame(
        perfectSquares(1),
        listOf(
            FlashCard("1^2 = ?", "1"),
        ),
        "one",
    )

    testSame(
        perfectSquares(2),
        listOf(
            FlashCard("1^2 = ?", "1"),
            FlashCard("2^2 = ?", "4"),
        ),
        "two",
    )

    testSame(
        perfectSquares(3),
        listOf(
            FlashCard("1^2 = ?", "1"),
            FlashCard("2^2 = ?", "4"),
            FlashCard("3^2 = ?", "9"),
        ),
        "three",
    )

    testSame(
        perfectSquares(5),
        listOf(
            FlashCard("1^2 = ?", "1"),
            FlashCard("2^2 = ?", "4"),
            FlashCard("3^2 = ?", "9"),
            FlashCard("4^2 = ?", "16"),
            FlashCard("5^2 = ?", "25"),
        ),
        "five",
    )
}

// -----------------------------------------------------------------
// Files of cards
// -----------------------------------------------------------------

val charSep = "|"

// returns the front of the card with the | and the back of the card in a string
fun cardToString(f: FlashCard): String {
    return "" + f.front + charSep + f.back
}

@EnabledTest
fun testcardToString() {
    testSame(
        cardToString(f1),
        "what is a string|texts",
        "one",
    )
    testSame(
        cardToString(f3),
        "What is a double|decimals",
        "three",
    )
}

// cuts the string to put it in a flashcard data class using .slice
fun stringToCard(s: String): FlashCard {
    return FlashCard(s.slice(0..s.indexOf(charSep) - 1), s.slice(s.indexOf(charSep) + 1..s.length - 1))
}

@EnabledTest
fun testStringToCard() {
    testSame(
        stringToCard("back|front"),
        FlashCard("back", "front"),
        "back,front",
    )

    testSame(
        stringToCard("hi|bye"),
        FlashCard("hi", "bye"),
        "hi,bye",
    )

    testSame(
        stringToCard("|"),
        FlashCard("", ""),
        "blank",
    )
}

// checks for the files and recursive puts them in
// list of flashcards and then it returns
fun readCardsFile(p: String): List<FlashCard> {
    if (!fileExists(p)) {
        return emptyList<FlashCard>()
    }

    val pl: List<String> = fileReadAsList(p)
    val flash: List<FlashCard> = pl.map(::stringToCard)
    return flash
}

@EnabledTest
fun testReadCardsFile() {
    testSame(
        readCardsFile("./project1/example.txt"),
        listOf(
            FlashCard("front 1", "back 1"),
            FlashCard("front 2", "back 2"),
        ),
        "back,front",
    )
}

// -----------------------------------------------------------------
// Processing a self-report
// (Hint: see Homework 2)
// -----------------------------------------------------------------

// checks wether it is a yes, otherwise it is a no
fun isPositive(s: String): Boolean {
    if (s.first().lowercase() == "y") {
        return true
    }
    return false
}

@EnabledTest
fun testIsPositive() {
    fun helpTest(
        str: String,
        expected: Boolean,
    ) {
        testSame(isPositive(str), expected, str)
    }

    helpTest("yes", true)
    helpTest("Yes", true)
    helpTest("YES", true)
    helpTest("yup", true)

    helpTest("nope", false)
    helpTest("NO", false)
    helpTest("nah", false)
    helpTest("not a chance", false)
    helpTest("indeed", false)
}

// -----------------------------------------------------------------
// Choosing a deck from a menu
// -----------------------------------------------------------------

val promptMenu = "Enter your choice"

// makes a list and adds all the options inside of it
// aftewards it uses khoury linesToString function and puts
// it on each line
fun choicesToText(lines: List<String>): String {
    fun initFunc(i: Int): String {
        if (i < lines.size) {
            return "" + (i + 1) + ". " + lines[i]
        }
            return "\n$promptMenu"

    }

    return linesToString(List<String>(lines.size + 1, ::initFunc))
}

@EnabledTest
fun testChoicesToText() {
    val optA = "apple"
    val optB = "banana"
    val optC = "carrot"

    testSame(
        choicesToText(listOf(optA)),
        linesToString(
            "1. $optA",
            "",
            promptMenu,
        ),
        "one",
    )

    testSame(
        choicesToText(listOf(optA, optB, optC)),
        linesToString(
            "1. $optA",
            "2. $optB",
            "3. $optC",
            "",
            promptMenu,
        ),
        "three",
    )
}

fun chooseOption(decks: List<Deck>): Deck {
    // gets the name of the deck
    fun getDeckName(d: Deck): String {
        return d.name
    }

    fun renderDeckOptions(state: Int): String {
        return choicesToText(decks.map(::getDeckName))
    }

    // gets the valid integer and returns it if it is valid
    fun keepIfValid(
        ip: String,
        s: IntRange,
    ): Int {
        if (ip.toInt() - 1 <= s.last() || ip.toInt() - 1 >= 0) {
            return ip.toInt() - 1
        } else {
            return -1
        }
    }

    fun transitionOptionChoice(
        ignoredState: Int,
        kbInput: String,
    ): Int {
        return keepIfValid(kbInput, decks.indices)
    }

    // checks if index is in decks
    fun validChoiceEntered(state: Int): Boolean {
        return state in decks.indices
    }

    // the prints out the choice the user selected
    fun choiceAnnouncement(s: String): String {
        return "you chose: $s"
    }

    // announces the choice
    fun renderChoice(state: Int): String {
        return choiceAnnouncement(getDeckName(decks[state]))
    }

    return decks[
        reactConsole(
            initialState = -1,
            stateToText = ::renderDeckOptions,
            nextState = ::transitionOptionChoice,
            isTerminalState = ::validChoiceEntered,
            terminalStateToText = ::renderChoice,
        ),
    ]
}

// -----------------------------------------------------------------
// Studying a deck
// -----------------------------------------------------------------

enum class CardStage {
    FRONT,
    BACK,
    DONE,
}

data class StudyState(val d: List<FlashCard>, val side: CardStage, val score: Int)

// Determines if the list is empty (and so the program is done)
fun isListEmpty(numList: List<FlashCard>): Boolean {
    return numList.isEmpty()
}

@EnabledTest
fun testIsListEmpty() {
    testSame(
        isListEmpty(listOf()),
        true,
        "true",
    )

    testSame(
        isListEmpty(listOf(FlashCard("a", "b"), FlashCard("a", "b"), FlashCard("a", "b"))),
        false,
        "false",
    )
}

// rotates between cards, if done prints out the score
fun deckToText(state: StudyState): String {
    if (isListEmpty(state.d)) {
        return "You got: ${state.score}"
    }

    return when (state.side) {
        CardStage.FRONT -> state.d[0].front + "\n" + studyThink
        CardStage.BACK -> state.d[0].back + "\n" + studyCheck
        else -> state.d[0].front
    }
}

@EnabledTest
fun testDeckToText() {
    testSame(
        deckToText(StudyState(emptyList<FlashCard>(), CardStage.BACK, 0)),
        "You got: 0",
        "empty list",
    )
}

// adds the score in if correct
// otherwise just goes to the next card
fun deckNextState(
    state: StudyState,
    userInput: String,
): StudyState {
    if (isListEmpty(state.d)) {
        return state
    }

    fun helpNextState(
        newState: CardStage,
        newList: List<FlashCard>,
        add: Boolean,
    ): StudyState {
        if (!add) {
            return StudyState(newList, newState, state.score)
        } else {
            val newScore = state.score + 1
            return StudyState(newList, newState, newScore)
        }
    }

    return when (state.side) {
        CardStage.FRONT -> helpNextState(CardStage.BACK, state.d, false)
        CardStage.BACK -> helpNextState(CardStage.FRONT, state.d.drop(1), isPositive(userInput))
        else -> helpNextState(CardStage.FRONT, state.d, false)
    }
}

@EnabledTest
fun testDeckNextState() {
    testSame(
        deckNextState(StudyState(emptyList<FlashCard>(), CardStage.FRONT, 0), ""),
        StudyState(emptyList<FlashCard>(), CardStage.FRONT, 0),
        "emptyList",
    )
}

// checks if the state is empty
fun isEmptyBool(state: StudyState): Boolean {
    return isListEmpty(state.d)
}

@EnabledTest
fun testIsEmptyBool() {
    testSame(
        isListEmpty(listOf()),
        true,
        "true",
    )

    testSame(
        isListEmpty(listOf(FlashCard("a", "b"), FlashCard("a", "b"), FlashCard("a", "b"))),
        false,
        "false",
    )
}

fun studyDeck(d: Deck): Int {
    return reactConsole(
        StudyState(d.cards, CardStage.FRONT, 0),
        ::deckToText,
        ::deckNextState,
        ::isEmptyBool,
    ).score
}

val studyThink = "Think of the result? Press enter to continue"
val studyCheck = "Correct? (Y)es/(N)o"

// -----------------------------------------------------------------
// Final app!
// -----------------------------------------------------------------

// creates the options and runs all the code
fun chooseAndStudy(): Int {
    // 1. Construct a list of options
    // (ala the instructions above)
    val deckOptions =
        listOf(
            Deck("file", readCardsFile("./project1/testRun.txt")),
            Deck("squares", perfectSquares(3)),
            d1,
            // deck hand-coded
        )

    // 2. Use chooseOption to let the user
    //    select a deck
    val deckChosen = chooseOption(deckOptions)

    // 3. Let the user study, return the
    //    number correctly answered
    return studyDeck(deckChosen)
}

@EnabledTest
fun testChooseAndStudy() {
    testSame(
        captureResults(
            ::chooseAndStudy,
            "1",
            "",
            "y",
            "",
            "y",
            "",
            "y",
            "",
            "y",
            "",
        ),
        CapturedResult(
            4,
            "1. file",
            "2. squares",
            "3. coding",
            "",
            "Enter your choice",
            "you chose: file",
            "how are you",
            "Think of the result? Press enter to continue",
            "good",
            "Correct? (Y)es/(N)o",
            "where is brazil",
            "Think of the result? Press enter to continue",
            "south america",
            "Correct? (Y)es/(N)o",
            "1+1",
            "Think of the result? Press enter to continue",
            "2",
            "Correct? (Y)es/(N)o",
            "2+2",
            "Think of the result? Press enter to continue",
            "4",
            "Correct? (Y)es/(N)o",
            "You got: 4",
        ),
        "file",
    )

    testSame(
        captureResults(
            ::chooseAndStudy,
            "2",
            "",
            "y",
            "",
            "y",
            "",
            "y",
            "",
        ),
        CapturedResult(
            3,
            "1. file",
            "2. squares",
            "3. coding",
            "",
            "Enter your choice",
            "you chose: squares",
            "1^2 = ?",
            "Think of the result? Press enter to continue",
            "1",
            "Correct? (Y)es/(N)o",
            "2^2 = ?",
            "Think of the result? Press enter to continue",
            "4",
            "Correct? (Y)es/(N)o",
            "3^2 = ?",
            "Think of the result? Press enter to continue",
            "9",
            "Correct? (Y)es/(N)o",
            "You got: 3",
        ),
        "squares",
    )

    testSame(
        captureResults(
            ::chooseAndStudy,
            "3",
            "",
            "y",
            "",
            "y",
            "",
            "y",
            "",
        ),
        CapturedResult(
            3,
            "1. file",
            "2. squares",
            "3. coding",
            "",
            "Enter your choice",
            "you chose: coding",
            "what is a string",
            "Think of the result? Press enter to continue",
            "texts",
            "Correct? (Y)es/(N)o",
            "what is a Int",
            "Think of the result? Press enter to continue",
            "wholenumbers",
            "Correct? (Y)es/(N)o",
            "What is a double",
            "Think of the result? Press enter to continue",
            "decimals",
            "Correct? (Y)es/(N)o",
            "You got: 3",
        ),
        "hand-code",
    )
}

// -----------------------------------------------------------------

fun main() {
    chooseAndStudy()
}

// runEnabledTests(this)
main()



