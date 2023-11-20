// -----------------------------------------------------------------
// Project: Part 2, Summary
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
import khoury.isAnInteger

// -----------------------------------------------------------------
// Flash Card data design
// (Hint: see Homework 5, Problem 3)
// -----------------------------------------------------------------

// (just useful values for
// the separation characters)
val sepCard = "|"
val sepTag = ","

// flash card with a back and front and tags
data class TaggedFlashCard(val front: String, val back: String, val tag: List<String>) {
    // checks if it has the tag
    fun isTagged(s: String): Boolean {
        return s in tag
    }

    // prints out it in the right format
    fun fileFormat(): String {
        return this.front + sepCard + this.back + sepCard + this.tag.joinToString()
    }
}

// create 3 different tagged flash card
val t1 = TaggedFlashCard("Hi", "Bye", listOf("greetings", "leaving", "human-like"))
val t2 = TaggedFlashCard("1+1", "2", listOf("Math", "Addition", "easy"))
val t3 = TaggedFlashCard("What class is this", "Coding", listOf("coding", "FUNDIES", "easy-hard"))

@EnabledTest
fun testTaggedFlashCard() {
    testSame(
        t1.isTagged("leaving"),
        true,
        "Tag for t1",
    )

    testSame(
        t1.fileFormat(),
        "Hi|Bye|greetings, leaving, human-like",
        "fomrat for t1",
    )

    testSame(
        t2.isTagged("leaving"),
        false,
        "Tag for t2",
    )

    testSame(
        t2.fileFormat(),
        "1+1|2|Math, Addition, easy",
        "fomrat for t2",
    )

    testSame(
        t3.isTagged("FUNDIES"),
        true,
        "Tag for t3",
    )

    testSame(
        t3.fileFormat(),
        "What class is this|Coding|coding, FUNDIES, easy-hard",
        "fomrat for t3",
    )
}


// -----------------------------------------------------------------
// Files of tagged flash cards
// -----------------------------------------------------------------

val charSep = "|"

// cuts the string to put it in a flashcard data class using .slice
fun stringToTaggedFlashCard(s: String): TaggedFlashCard {
    val stringList = s.split(charSep)
    return TaggedFlashCard(stringList[0], stringList[1], stringList[2].split(","))
}

@EnabledTest
fun testStringToTaggedFlashCard() {
    testSame(
        stringToTaggedFlashCard("back|front|test"),
        TaggedFlashCard("back", "front", listOf("test")),
        "back,front",
    )

    testSame(
        stringToTaggedFlashCard("hi|bye|greetings,leaving"),
        TaggedFlashCard("hi", "bye", listOf("greetings", "leaving")),
        "hi,bye",
    )

    testSame(
        stringToTaggedFlashCard("||"),
        TaggedFlashCard("", "", listOf("")),
        "blank",
    )
}

// checks for the files and recursive puts them in
// list of flashcards and then it returns
fun readCardsFile(file: String): List<TaggedFlashCard> {
    if (!fileExists(file)) 
        return emptyList<TaggedFlashCard>()
    

    val fileList: List<String> = fileReadAsList(file)
    val flash: List<TaggedFlashCard> = fileList.map(::stringToTaggedFlashCard)
    return flash
}

@EnabledTest
fun testReadCardsFile() {
    testSame(
        readCardsFile("./project2/example_tagged.txt"),
        listOf(
            TaggedFlashCard("c", "3", listOf("hard", "science")),
            TaggedFlashCard("d", "4", listOf("hard")),
        ),
        "example.txt",
    )

    testSame(
        readCardsFile("./project2/BAD_NAME.txt"),
        listOf(),
        "EMPTY",
    )
}

// -----------------------------------------------------------------
// Deck design
// -----------------------------------------------------------------

// The deck is either exhausted,
// showing the question, or
// showing the answer
enum class DeckState {
    EXHAUSTED,
    QUESTION,
    ANSWER,
}

// Basic functionality of any deck
interface IDeck {
    // The state of the deck
    fun getState(): DeckState

    // The currently visible text
    // (or null if exhausted)
    fun getText(): String?

    // The number of question/answer pairs
    // (does not change when question are
    // cycled to the end of the deck)
    fun getSize(): Int

    // Shifts from question -> answer
    // (if not QUESTION state, returns the same IDeck)
    fun flip(): IDeck

    // Shifts from answer -> next question (or exhaustion);
    // if the current question was correct it is discarded,
    // otherwise cycled to the end of the deck
    // (if not ANSWER state, returns the same IDeck)
    fun next(correct: Boolean): IDeck
}

// creates a class that takes in a list of tages and a starting deck state
// follows the inheritate IDeck interface
class TFCListDeck(private val cards: List<TaggedFlashCard>, private val deckState: DeckState): IDeck{

    // get the current state
    override fun getState(): DeckState =  deckState

    // return the text depending on the state
    override fun getText(): String? {
        return when(deckState){
            DeckState.QUESTION -> cards[0].front
            DeckState.ANSWER -> cards[0].back
            DeckState.EXHAUSTED -> null
        }
    } 

    // get the size of the list
    override fun getSize(): Int =  cards.size

    // depending on the state it calls TFCListDeck, again and it drops
    override fun flip(): TFCListDeck{
        if(cards.isEmpty())
            return TFCListDeck(cards,DeckState.EXHAUSTED)
        return when(deckState){
            DeckState.QUESTION -> TFCListDeck(cards, DeckState.ANSWER)
            DeckState.ANSWER -> TFCListDeck(cards.drop(1), DeckState.QUESTION)
            else -> this
        }
    }

    // checks if the answer is correct and if it isnt it adds to the list
    override fun next(correct: Boolean): TFCListDeck{
        if(correct)
            if(cards.size == 1)
                return TFCListDeck(cards,DeckState.EXHAUSTED)
            else
                return TFCListDeck(cards.drop(1), DeckState.QUESTION)
        else
            return TFCListDeck(cards.drop(1) + cards[0], DeckState.QUESTION)
    }
}

@EnabledTest
fun testTFCListDeck(){
    testSame(
        TFCListDeck(listOf(t1,t2,t3), DeckState.QUESTION).getState(),
        DeckState.QUESTION,
        "deck QUESTION"
    )

    testSame(
        TFCListDeck(listOf(t1,t2,t3), DeckState.ANSWER).getState(),
        DeckState.ANSWER,
        "deck ANSWER"
    )

    testSame(
        TFCListDeck(listOf(t1,t2,t3), DeckState.EXHAUSTED).getState(),
        DeckState.EXHAUSTED,
        "deck EXHAUSTED"
    )

    testSame(
        TFCListDeck(listOf(t1), DeckState.QUESTION).getText(),
        "Hi",
        "text QUESTION"
    )

    testSame(
        TFCListDeck(listOf(t1), DeckState.ANSWER).getText(),
        "Bye",
        "text ANSWER"
    )
    
    testSame(
        TFCListDeck(listOf(t1,t2,t3), DeckState.EXHAUSTED).getText(),
        null,
        "text EXHAUSTED"
    )

    testSame(
        TFCListDeck(listOf(t1,t2,t3), DeckState.QUESTION).getSize(),
        3,
        "get Size"
    )

    testSame(
        TFCListDeck(listOf(t1,t2,t3), DeckState.QUESTION).next(true).next(false).next(true).getText(),
        "1+1",
        "incorrect"
    )
}

// creates a class that takes in the starting and ending int, as well as the DeckState
class PerfectSquaresDeck(private val start: Int, private val max: Int, private val deckState: DeckState): IDeck {
    // creates a list for the incorrect answers
    private var incorrect = emptyList<Int>()

    // returns the deckstate
    override fun getState(): DeckState = deckState

    // checks if it has gone through the whole start - max, if it hasent if keeps going
    // afterwards it goes through the incorrect list to the answers that are incorrect
    override fun getText(): String?{
        if(start >= max){
            return when(deckState){
            DeckState.QUESTION -> "$incorrect[0]^2 = ?"
            DeckState.ANSWER -> "${incorrect[0]*incorrect[0]}"
            DeckState.EXHAUSTED -> null
            } 
        } 
        return when(deckState){
            DeckState.QUESTION -> "$start^2 = ?"
            DeckState.ANSWER -> "${start*start}"
            DeckState.EXHAUSTED -> null
        } 
    }

    // gets the max 
    override fun getSize(): Int = max

    // checks if there is no incorrect and it has reached the max then deck is exhausted
    // it uses .also to modify the variable inside of it and drop the ones arelady incorrect
    // if not it goes throught the list
    // otherwise it keeps going through the start-max list
    override fun flip(): IDeck{
        if(start >= max+1 && incorrect.isEmpty())
            return PerfectSquaresDeck(start, max, DeckState.EXHAUSTED)
        else if(start >= max+1)
            return when(deckState){
                DeckState.QUESTION -> PerfectSquaresDeck(start, max, DeckState.ANSWER)
                DeckState.ANSWER -> PerfectSquaresDeck(start, max, DeckState.QUESTION).also {incorrect.drop(1)}
                else -> this
            }
        return when(deckState){
            DeckState.QUESTION -> PerfectSquaresDeck(start, max, DeckState.ANSWER)
            DeckState.ANSWER -> PerfectSquaresDeck(start+1, max, DeckState.QUESTION)
            else -> this
        }
    }

    // if it is correct it just ignores otherwise it adds to the incorrect list
    // uses .also to add the incorrect answers in order
    override fun next(correct: Boolean): IDeck{
        if(correct)
            if(start >= max+1 && incorrect.isEmpty())
                return PerfectSquaresDeck(start, max, DeckState.EXHAUSTED)
            return PerfectSquaresDeck(start, max, DeckState.QUESTION)
    
        return PerfectSquaresDeck(start, max, DeckState.QUESTION).also { incorrect = incorrect + start }
            
    }
}

@EnabledTest
fun testPerfectSquaresDeck(){
    testSame(
        PerfectSquaresDeck(0, 5, DeckState.QUESTION).getState(),
        DeckState.QUESTION,
        "deck QUESTION"
    )

    testSame(
        PerfectSquaresDeck(0, 5, DeckState.ANSWER).getState(),
        DeckState.ANSWER,
        "deck ANSWER"
    )

    testSame(
        PerfectSquaresDeck(0, 5, DeckState.EXHAUSTED).getState(),
        DeckState.EXHAUSTED,
        "deck EXHAUSTED"
    )

    testSame(
        PerfectSquaresDeck(0, 5, DeckState.QUESTION).getText(),
        "0^2 = ?",
        "text QUESTION"
    )

    testSame(
        PerfectSquaresDeck(0, 5, DeckState.ANSWER).getText(),
        "0",
        "text ANSWER"
    )

     testSame(
        PerfectSquaresDeck(5, 5, DeckState.EXHAUSTED).getText(),
        null,
        "text EXHAUSTED"
    )

    testSame(
        PerfectSquaresDeck(0, 5, DeckState.QUESTION).flip().flip().getText(),
        "1^2 = ?",
        "next Question"
    )

    testSame(
        PerfectSquaresDeck(0, 5, DeckState.ANSWER).flip().flip().getText(),
        "1",
        "next Answer"
    )

    testSame(
        PerfectSquaresDeck(3, 5, DeckState.ANSWER).next(false).next(true).next(true).getText(),
        "3^2 = ?",
        "incorrect"
    )
}



// -----------------------------------------------------------------
// Menu design
// -----------------------------------------------------------------

// the only required capability for a menu option
// is to be able to render a title
interface IMenuOption {
    fun menuTitle(): String
}

// a menu option with a single value and name
data class NamedMenuOption<T>(val option: T, val name: String) : IMenuOption {
    override fun menuTitle(): String = name
}

// puts the options in the format we were given
fun <T> choicesToText(lines: List<T>): String {
    fun initFunc(i: Int): String {
        return if (i < lines.size) {
            "${i + 1}. ${lines[i]}"
        } else {
            "\n$menuPrompt"
        }
    }

    return linesToString(List(lines.size + 1, ::initFunc))
}

// Some useful outputs
val menuPrompt = "Enter your choice (or 0 to quit)"
val menuQuit = "You quit"
val menuChoicePrefix = "You chose: "

// Provides an interactive opportunity for the user to choose
// an option or quit.
fun <T : IMenuOption> chooseMenuOption(options: List<T>): T? {
    //code here!
    // gets the name of the menuTitle
    fun getOptionName(option: T): String = option.menuTitle()

    // calls 2 helper functions to help format it the way we are supposed to format
    fun renderOptions(l: List<T>): String {
        return choicesToText(l.map(::getOptionName))
    }

    //  gets the string and checks if it is a int
    // if it is a string it checks if it is in the list
    // if it is not it returns -1
    // if the string is -1 then returns -1
    fun keepIfValid(ip: String): Int {
        if(!isAnInteger(ip))
            return options.size+1
        val choice = ip.toInt() - 1

        return if (choice in 0 until options.size) 
            choice
        else if(choice == -1)
            -1
        else 
            -2
        
    }

    // calls a helper function that checks if it is a valid answers then returns int
    fun transitionOptionChoice(ignoredState: Int, kbInput: String): Int {
        return keepIfValid(kbInput)
    }

    // checks if index is in options
    fun validChoiceEntered(state: Int): Boolean {
        return if (state == -1) true else state in 0 until options.size
    }

    // announces the choice
    fun renderChoice(state: Int): String {
        return if (state == -1) {
            menuQuit
        } else {
            "$menuChoicePrefix${getOptionName(options[state])}"
        }
    }

    // call reactConsole (with appropriate handlers)
    // return the selected option (or null for quit)
    val a = reactConsole(
        initialState = -2,
        stateToText = { renderOptions(options) },
        nextState = ::transitionOptionChoice,
        isTerminalState = ::validChoiceEntered,
        terminalStateToText = ::renderChoice
    )

    return if (a == -1) null else options[a]
}


@EnabledTest
fun testChooseMenuOption() {
    val opt1A = NamedMenuOption(1, "apple")
    val opt2B = NamedMenuOption(2, "banana")
    val optsExample = listOf(opt1A, opt2B)

    testSame(
        captureResults(
            { chooseMenuOption(listOf(opt1A)) },
            "howdy",
            "0",
        ),
        CapturedResult(
            null,
            "1. ${opt1A.name}",
            "",
            menuPrompt,
            "1. ${opt1A.name}",
            "",
            menuPrompt,
            menuQuit,
        ),
        "quit",
    )

    testSame(
        captureResults(
            { chooseMenuOption(optsExample) },
            "hello",
            "10",
            "-3",
            "1",
        ),
        CapturedResult(
            opt1A,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "${menuChoicePrefix}${opt1A.name}",
        ),
        "1",
    )

    testSame(
        captureResults(
            { chooseMenuOption(optsExample) },
            "3",
            "-1",
            "2",
        ),
        CapturedResult(
            opt2B,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
            "${menuChoicePrefix}${opt2B.name}",
        ),
        "2",
    )
}

// -----------------------------------------------------------------
// Machine learning for sentiment analysis
// -----------------------------------------------------------------

typealias PositivityClassifier = (String) -> Boolean

data class LabeledExample<E, L>(val example: E, val label: L)

val datasetYN: List<LabeledExample<String, Boolean>> =
    listOf(
        LabeledExample("yes", true),
        LabeledExample("y", true),
        LabeledExample("indeed", true),
        LabeledExample("aye", true),
        LabeledExample("oh yes", true),
        LabeledExample("affirmative", true),
        LabeledExample("roger", true),
        LabeledExample("uh huh", true),
        LabeledExample("true", true),
        // just a visual separation of
        // the positive/negative examples
        LabeledExample("no", false),
        LabeledExample("n", false),
        LabeledExample("nope", false),
        LabeledExample("negative", false),
        LabeledExample("nay", false),
        LabeledExample("negatory", false),
        LabeledExample("uh uh", false),
        LabeledExample("absolutely not", false),
        LabeledExample("false", false),
    )

// Heuristically determines if the supplied string
// is positive based upon the first letter being Y
fun isPositiveSimple(s: String): Boolean {
    return s.uppercase().startsWith("Y")
}

// tests that an element of the dataset matches
// with expectation of its correctness on a
// particular classifier
fun helpTestElement(
    index: Int,
    expectedIsCorrect: Boolean,
    isPos: PositivityClassifier,
) {
    testSame(
        isPos(datasetYN[index].example),
        when (expectedIsCorrect) {
            true -> datasetYN[index].label
            false -> !datasetYN[index].label
        },
        when (expectedIsCorrect) {
            true -> datasetYN[index].example
            false -> "${ datasetYN[index].example } <- WRONG"
        },
    )
}

@EnabledTest
fun testIsPositiveSimple() {
    val classifier = ::isPositiveSimple

    // correctly responds with positive
    for (i in 0..1) {
        helpTestElement(i, true, classifier)
    }

    // incorrectly responds with negative
    for (i in 2..8) {
        helpTestElement(i, false, classifier)
    }

    // correctly responds with negative, sometimes
    // due to luck (i.e., anything not starting
    // with the letter Y is assumed negative)
    for (i in 9..17) {
        helpTestElement(i, true, classifier)
    }
}

typealias EvaluationFunction<T> = (T) -> Int

fun <T> topK(
    possibilities: List<T>,
    k: Int,
    evalFunc: EvaluationFunction<T>,
): List<T> {
    // associate each item with its score
    val itemsWithScores =
        possibilities.map {
            Pair(
                it,
                evalFunc(it),
            )
        }

    // sort by score
    val sortedByEval =
        itemsWithScores.sortedByDescending {
            it.second
        }

    // strip away score
    val sortedWithoutScores =
        sortedByEval.map {
            it.first
        }

    // get the first-k (i.e., top-k via score)
    return sortedWithoutScores.take(k)
}

@EnabledTest
fun testTopK() {
    testSame(
        topK(listOf("Hi", "Bye", "See Yea", "Night"), 3,{ s -> s.length }),
        listOf("See Yea", "Night", "Bye"),
        "longest String",
    )

    testSame(
        topK(listOf(1, 6, 3, 9, 7, 3, 5), 3,{ n -> n }),
        listOf(9, 7, 6),
        "Biggest Number",
    )

    testSame(
        topK(listOf("Hi", "Bye", "See Yea", "Night"), 2, { s -> s.length * -1 },),
        listOf("Hi", "Bye"),
        "Shortest String",
    )

    testSame(
        topK(listOf(1, 6, 3, 9, 7, 3, 5), 2, { n -> n * -1 },),
        listOf(1, 3),
        "Smallest Number",
    )

    testSame(
        topK(emptyList<Int>(), 5, { s -> s }),
        listOf(),
        "empty/INT",
    )

    testSame(
        topK(emptyList<String>(),  5, { s -> s.length },),
        listOf(),
        "empty/STRING",
    )
}

fun levenshteinDistance(
    str1: String,
    str2: String,
): Int {
    return levenshteinHelper(str1, str2, str1.length, str2.length)
}

fun levenshteinHelper(
    str1: String,
    str2: String,
    i: Int,
    j: Int,
): Int {
    if (i == 0) {
        return j
    }

    if (j == 0) {
        return i
    }

    return if (str1[i - 1] == str2[j - 1]) {
        levenshteinHelper(str1, str2, i - 1, j - 1)
    } else {
        1 +
            minOf(
                levenshteinHelper(str1, str2, i - 1, j),
                levenshteinHelper(str1, str2, i, j - 1),
                levenshteinHelper(str1, str2, i - 1, j - 1),
            )
    }
}

@EnabledTest
fun testLevenshteinDistance() {
    testSame(
        levenshteinDistance("", "howdy"),
        5,
        "'', 'howdy'",
    )

    testSame(
        levenshteinDistance("howdy", ""),
        5,
        "'howdy', ''",
    )

    testSame(
        levenshteinDistance("howdy", "howdy"),
        0,
        "'howdy', 'howdy'",
    )

    testSame(
        levenshteinDistance("kitten", "sitting"),
        3,
        "'kitten', 'sitting'",
    )

    testSame(
        levenshteinDistance("sitting", "kitten"),
        3,
        "'sitting', 'kitten'",
    )
}

typealias DistanceFunction<T> = (T, T) -> Int

data class ResultWithVotes<L>(val label: L, val votes: Int)

// uses k-nearest-neighbor (kNN) to predict the label
// for a supplied example given a labeled dataset
// and distance function
fun <E, L> nnLabel(
    queryExample: E,
    dataset: List<LabeledExample<E, L>>,
    distFunc: DistanceFunction<E>,
    k: Int,
): ResultWithVotes<L> {
    // 1. Use topK to find the k-closest dataset elements:
    //    finding the elements whose negated distance is the
    //    greatest is the same as finding those that are closest.
    val closestK =
        topK(dataset, k) {
            -distFunc(queryExample, it.example)
        }

    // 2. Discard the examples, we only care about their labels
    val closestKLabels = closestK.map { it.label }

    // 3. For each distinct label, count up how many time it
    //    showed up in step #2
    //    (Note: once we know the Map type, there are WAY simpler
    //           ways to do this!)
    val labelsWithCounts =
        closestKLabels.distinct().map {
                label ->
            Pair(
                // first = label
                label,
                // second = number of votes
                closestKLabels.filter({ it == label }).size,
            )
        }

    // 4. Use topK to get the label with the greatest count
    val topLabelWithCount = topK(labelsWithCounts, 1, { it.second })[0]

    // 5. Return both the label and the number of votes (of k)
    return ResultWithVotes(
        topLabelWithCount.first,
        topLabelWithCount.second,
    )
}

@EnabledTest
fun testNNLabel() {

    //       a   a       ?       b           b
    // |--- --- --- --- --- --- --- --- --- ---|
    //   1   2   3   4   5   6   7   8   9  10
    val dataset =
        listOf(
            LabeledExample(2, "a"),
            LabeledExample(3, "a"),
            LabeledExample(7, "b"),
            LabeledExample(10, "b"),
        )

    // A simple distance: just the absolute value
    fun myAbsVal(
        a: Int,
        b: Int,
    ): Int {
        val diff = a - b

        return when (diff >= 0) {
            true -> diff
            false -> -diff
        }
    }

    testSame(
        nnLabel(5, dataset, ::myAbsVal, k = 3),
        ResultWithVotes("a", 2),
        "NN: 5->a, 2/3",
    )

    testSame(
        nnLabel(1, dataset, ::myAbsVal, k = 1),
        ResultWithVotes("a", 1),
        "NN: 1->a, 1/3",
    )

    testSame(
        nnLabel(1, dataset, ::myAbsVal, k = 2),
        ResultWithVotes("a", 2),
        "NN: 1->a, 2/3",
    )

    testSame(
        nnLabel(10, dataset, ::myAbsVal, k = 1),
        ResultWithVotes("b", 1),
        "NN: 10->b, 1/3",
    )

    testSame(
        nnLabel(10, dataset, ::myAbsVal, k = 2),
        ResultWithVotes("b", 2),
        "NN: 10->b, 2/3",
    )
}

// we'll generally use k=3 in our classifier
val classifierK = 3

fun yesNoClassifier(s: String): ResultWithVotes<Boolean> {
    // 1. Convert the input to lowercase
    //    (since) the data set is all lowercase
    var lowerS = s.lowercase()

    // 2. Check to see if the lower-case input
    //    shows up exactly within the dataset
    //    (you can assume there are no duplicates)
    for(i in datasetYN.indices)
        if(datasetYN[i].example == lowerS)
            return ResultWithVotes(datasetYN[i].label, classifierK)
    

    // 3. If the input was found, simply return its label with 100%
    //    confidence (3/3); otherwise, return the result of
    //    performing a 3-NN classification using the dataset and
    //    Levenshtein distance metric.

    return nnLabel(lowerS, datasetYN, ::levenshteinDistance, classifierK)
}

@EnabledTest
fun testYesNoClassifier() {
    testSame(
        yesNoClassifier("YES"),
        ResultWithVotes(true, 3),
        "YES: 3/3",
    )

    testSame(
        yesNoClassifier("no"),
        ResultWithVotes(false, 3),
        "no: 3/3",
    )

    testSame(
        yesNoClassifier("nadda"),
        ResultWithVotes(false, 2),
        "nadda: 2/3",
    ) // pretty good ML!

    testSame(
        yesNoClassifier("yerp"),
        ResultWithVotes(true, 3),
        "yerp: 3/3",
    ) // pretty good ML!

    testSame(
        yesNoClassifier("ouch"),
        ResultWithVotes(true, 3),
        "ouch: 3/3",
    ) // seems very confident in this wrong answer...

    testSame(
        yesNoClassifier("now"),
        ResultWithVotes(false, 3),
        "now 3/3",
    ) // seems very confident, given the input doesn't make sense?
}

fun isPositiveML(s: String): Boolean = yesNoClassifier(s).label

@EnabledTest
fun testIsPositiveML() {
    // correctly responds with positive (rote memorization)
    for (i in 0..8) {
        helpTestElement(i, true, ::isPositiveML)
    }

    // correctly responds with negative (rote memorization)
    for (i in 9..17) {
        helpTestElement(i, true, ::isPositiveML)
    }
}

// -----------------------------------------------------------------
// Final app!
// -----------------------------------------------------------------

// Some useful prompts
val studyThink = "Think of the result? Press enter to continue"
val studyCheck = "Correct? (Y)es/(N)o"

data class StudyDeckResult(val numQuestions: Int, val numAttempts: Int)

data class StudyState(val cards: IDeck, val result: StudyDeckResult)

fun studyStateToText(state: StudyState): String {
    return when(state.cards.getState()){
        DeckState.QUESTION -> ""+state.cards.getText() + "\n$studyThink"
        DeckState.ANSWER -> ""+state.cards.getText() + "\n$studyCheck"
        DeckState.EXHAUSTED -> ""+state.cards.getText()
    }
}

@EnabledTest
fun testStudyStateToText(){
    testSame(
        studyStateToText(StudyState(TFCListDeck(listOf(t1,t2), DeckState.QUESTION), StudyDeckResult(5,0))),
        "Hi\n$studyThink",
        "QUESTION"
    )

    testSame(
        studyStateToText(StudyState(TFCListDeck(listOf(t1,t2), DeckState.ANSWER), StudyDeckResult(5,0))),
        "Bye\n$studyCheck",
        "ANSWER"
    )

    testSame(
        studyStateToText(StudyState(TFCListDeck(listOf(t1,t2), DeckState.EXHAUSTED), StudyDeckResult(5,0))),
        "null",
        "EXHAUSTED"
    )
}

fun studyAnswerHelper(s: String): Boolean {
    return yesNoClassifier(s).label
}

fun studyNextState(state: StudyState, userInput: String): StudyState {
    val questions = state.result.numQuestions
    val attempt = state.result.numAttempts
    return when(state.cards.getState()){
        DeckState.QUESTION -> StudyState(state.cards.flip(), StudyDeckResult(questions, attempt))
        DeckState.ANSWER -> StudyState(state.cards.next(studyAnswerHelper(userInput)), StudyDeckResult(questions, attempt+1))
        DeckState.EXHAUSTED -> StudyState(state.cards, StudyDeckResult(questions, attempt))
    }
}

// @EnabledTest
// fun testStudyNextState(){
//     testSame(
//         studyNextState(StudyState(TFCListDeck(listOf(t1,t2), DeckState.QUESTION), StudyDeckResult(5,0)), "yes"),
//         StudyState(TFCListDeck(listOf(t1,t2), DeckState.ANSWER), StudyDeckResult(5,0)),
//         "QUESTION"
//     )
// }

fun isDone(state: StudyState): Boolean {
    return state.cards.getState() == DeckState.EXHAUSTED
}

@EnabledTest
fun testIsDone(){
    testSame(
        isDone(StudyState(TFCListDeck(listOf(t1,t2), DeckState.QUESTION), StudyDeckResult(5,0))),
        false,
        "QUESTION"
    )

    testSame(
        isDone(StudyState(TFCListDeck(listOf(t1,t2), DeckState.ANSWER), StudyDeckResult(5,0))),
        false,
        "ANSWER"
    )

    testSame(
        isDone(StudyState(TFCListDeck(listOf(t1,t2), DeckState.EXHAUSTED), StudyDeckResult(5,0))),
        true,
        "EXHAUSTED"
    )
}

fun isDoneToText(state: StudyState): String{
    return "Questions: ${state.result.numQuestions} Attempts: ${state.result.numAttempts}"
} 

@EnabledTest
fun testIsDoneToText(){
    testSame(
        isDoneToText(StudyState(TFCListDeck(listOf(t1,t2), DeckState.QUESTION), StudyDeckResult(5,8))),
        "Questions: 5 Attempts: 8",
        "QUESTION"
    )

    testSame(
        isDoneToText(StudyState(TFCListDeck(listOf(t1,t2), DeckState.ANSWER), StudyDeckResult(2,10))),
        "Questions: 2 Attempts: 10",
        "ANSWER"
    )

    testSame(
        isDoneToText(StudyState(TFCListDeck(listOf(t1,t2), DeckState.EXHAUSTED), StudyDeckResult(1,1))),
        "Questions: 1 Attempts: 1",
        "EXHAUSTED"
    )
}

fun studyDeck2(deck: IDeck){

     reactConsole(
        StudyState(deck, StudyDeckResult(deck.getSize(), 0)),
        ::studyStateToText,
        ::studyNextState,
        ::isDone,
        ::isDoneToText
    )
}

@EnabledTest
fun testStudyDeck2(){
    fun studyDeckWorld() {
         studyDeck2(TFCListDeck(listOf(t1,t2), DeckState.QUESTION))
    }

    testSame(
        captureResults(
            ::studyDeckWorld,
            "",
            "No",
            "",
            "nope!",
            "",
            "yes",
            "",
            "yes!",
        ),
        CapturedResult(
            Unit,
            "Hi", studyThink,
            "Bye", studyCheck,
            "1+1", studyThink,
            "2", studyCheck,
            "Hi", studyThink,
            "Bye", studyCheck,
            "1+1", studyThink,
            "2", studyCheck,
            "Questions: 2 Attempts: 4",
        ),
        "mix"
    )
}

// TODO 2/2: Finally, design the program study2 that...
//           a) Uses chooseMenuOption to select from amongst a
//              list of decks; the options must include at least
//              one deck read from a file (using
//              readTaggedFlashCardsFile), one generated by code
//              (using PerfectSquaresDeck), and one that filters
//              based upon a tag being present (e.g., only
//              "hard" cards from a list; this may be the cards
//              read from a file).
//           b) If the menu in (a) didn't result in quitting, then
//              uses chooseMenuOption again to select from amongst
//              the two sentiment analysis functions.
//           c) If the menu in (b) didn't result in quitting, then
//              uses studyDeck2 to study through the selected deck
//              with the selected sentiment analysis function.
//           d) Returns to (a) and continues until either of the
//              two menus indicate a desire to quit.
//
//           Make sure to provide tests that capture (at least)...
//           - Quitting at the selection of decks
//           - Quitting at the selection of sentiment analysis
//             functions
//           - Studying through at least one deck
//

fun chooseAndStudy(){
    // 1. Construct a list of options
    // (ala the instructions above)
    val deckOptions =
        listOf(
            NamedMenuOption(TFCListDeck(listOf(t1,t2,t3), DeckState.QUESTION), "Pre-Build"),
            NamedMenuOption(PerfectSquaresDeck(0,5,DeckState.QUESTION), "Squares"),
            NamedMenuOption(TFCListDeck(readCardsFile("./project2/example_tagged.txt"), DeckState.QUESTION), "Files")
        )

    // 2. Use chooseOption to let the user
    //    select a deck
    val deckChosen: NamedMenuOption<IDeck>  = chooseMenuOption(deckOptions)

    // 3. Let the user study, return the
    //    number correctly answered
    studyDeck2(deckChosen.option)
}

// some useful labels
val optSimple = "Simple Self-Report Evaluation"
val optML = "ML Self-Report Evaluation"

// -----------------------------------------------------------------

// runEnabledTests(this)
// main()
chooseAndStudy()
// studyDeck2(listOf(t1,t2))