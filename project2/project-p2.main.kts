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
    override fun flip(): IDeck{
        if(cards.isEmpty())
            return TFCListDeck(cards,DeckState.EXHAUSTED)
        return when(deckState){
            DeckState.QUESTION -> TFCListDeck(cards, DeckState.ANSWER)
            DeckState.ANSWER -> TFCListDeck(cards.drop(1), DeckState.QUESTION)
            else -> this
        }
    }

    // checks if the answer is correct and if it isnt it adds to the list
    override fun next(correct: Boolean): IDeck{
        if(correct)
            return TFCListDeck(cards, deckState)
        else
            return TFCListDeck(cards + cards[0], deckState)
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
        "Hi",
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
    override fun next(correct: Boolean): IDeck{
        if(correct)
            return PerfectSquaresDeck(start, max, DeckState.QUESTION)
        else
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

// In part 1 of the project, you designed isPositive as a way to
// interpret whether a student's self-report was positive or
// negative; in the world of Machine Learning (a subfield of
// Artificial Intelligence, or AI), this is an approach to
// "sentiment analysis" - a problem in Natural Language Processing
// (NLP) that seeks to analyze text to understand the emotional
// tone of some text.
//
// In this context, what you built was a "binary classifier" of
// text, meaning it output one of two values according to the input
// string. In Kotlin we can describe this input-output relationship
// using the following shortcut...

typealias PositivityClassifier = (String) -> Boolean

// This code simply means we can now use PositivityClassifier
// anywhere we would have used the type on the right (e.g.,
// as the type in a function's parameter or return type).
//
// Our goal is now to try and use a more sophisticated approach
// to sentiment analysis - one that learns positivity/negativity
// based upon a dataset of supplied examples. To represent such a
// dataset, consider the following type...

data class LabeledExample<E, L>(val example: E, val label: L)

// This associates a "label" (such as positive vs negative, or
// cat video vs boring) with an example. Here is one such dataset:

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

// FYI: we call this dataset "balanced" since it has an equal
//      number of examples of the labels (i.e., # true and #false).
//      Such a balance is *one* tool (of many) when trying to avoid
//      algorithmic bias (en.wikipedia.org/wiki/Algorithmic_bias).

// Notice that our simple heuristic of the first letter is pretty
// good according to this dataset, but will make some lucky
// guesses (e.g., "false") and some actual mistakes (e.g., "true").
// We have provided below that code, as well as a set of tests that
// reference our labeled dataset - make sure you understand all of
// this code (including the comments in the tests about when & how
// the heuristic is predictably getting the answer wrong).

// // Heuristically determines if the supplied string
// // is positive based upon the first letter being Y
// fun isPositiveSimple(s: String): Boolean {
//     return s.uppercase().startsWith("Y")
// }

// // tests that an element of the dataset matches
// // with expectation of its correctness on a
// // particular classifier
// fun helpTestElement(
//     index: Int,
//     expectedIsCorrect: Boolean,
//     isPos: PositivityClassifier,
// ) {
//     testSame(
//         isPos(datasetYN[index].example),
//         when (expectedIsCorrect) {
//             true -> datasetYN[index].label
//             false -> !datasetYN[index].label
//         },
//         when (expectedIsCorrect) {
//             true -> datasetYN[index].example
//             false -> "${ datasetYN[index].example } <- WRONG"
//         },
//     )
// }

// @EnabledTest
// fun testIsPositiveSimple() {
//     val classifier = ::isPositiveSimple

//     // correctly responds with positive
//     for (i in 0..1) {
//         helpTestElement(i, true, classifier)
//     }

//     // incorrectly responds with negative
//     for (i in 2..8) {
//         helpTestElement(i, false, classifier)
//     }

//     // correctly responds with negative, sometimes
//     // due to luck (i.e., anything not starting
//     // with the letter Y is assumed negative)
//     for (i in 9..17) {
//         helpTestElement(i, true, classifier)
//     }
// }

// One approach we *could* take is just to have the computer learn
// by rote memorization: that is, respond with the labeled answer
// from the dataset. But what about if the student supplies an
// input not in this list? The approach we'll try as a way to
// handle this situation is the following...
// - If the response is known in the dataset (independent of
//   upper/lower-case), use the associated label
// - Otherwise...
//   Find the 3 "closest" examples and respond with a majority
//   vote of their associated labels
//
// This algorithm will represent our attempt to "generalize"
// from the dataset; we know we'll always get certain responses
// correct, and we'll let our dataset inform the response of
// unknown inputs. As with all approaches based upon machine
// learning, this approach is likely to make mistakes (even those
// that we'll find confusing/comical), and so we should be
// judicious in how we apply the system in the world.
//
// Now let's build up this classifier, step-by-step :)
//

// TODO 1/5: When finding closest examples, and majority vote, it
//           will be helpful to be able to get the "top-k" of a
//           list by some measure; meaning, a function that can
//           get the top-3 strings in a list by length, but
//           equally identify the top-1 (i.e., best) song by
//           ratings. To help, consider the following definition
//           of an "evaluation" function: one that takes an input
//           of some type and associates an output "score" (where
//           bigger scores are understood to be better):

typealias EvaluationFunction<T> = (T) -> Int

//          Design the function topK that takes a list of
//          items, k (assumed to be a postive integer), and a
//          corresponding evaluation function, and then returns
//          the k items in the list that get the highest score
//          (if there are ties, you are free to return any of the
//          winners; if there aren't enough items in the list,
//          return as many as you can).
//
//          Hint: You did this problem in Homework 7, Problem 1
//                - To simplify, you can avoid the ItemScore type
//                  by using the built-in `zip` function that you
//                  implemented in Homework 7, Problem 3.
//                - Later functions will use topK and assume the
//                  parameter ordering is as described above (which
//                  is a small swap from the sample solution).
//

// TODO 2/5: Great! Now we have to answer the question from before:
//           what does it mean for two strings to be "close"?
//           There are actually multiple reasonable ways of
//           capturing such a distance, one of which is the
//           Levenshtein Distance, which describes the minimum
//           number of single-character changes (e.g., adding a
//           character, removing one, or substituting) required to
//           change one sequence into another
//           (https://en.wikipedia.org/wiki/Levenshtein_distance).
//           Your task is to design the function
//           levenshteinDistance that computes this distance for
//           two supplied strings.
//
//           Hint: Homework 7, Problem 2 :)
//

// TODO 3/5: Great! Now let's design a "k-Nearest Neighbor"
//           classifier (you can read online description, such as
//           on Wikipedia, for lots of details & variants, but
//           we'll give you all the information you need here).
//
//           The goal here: given a dataset of labeled examples,
//           a distance function, and a number k, let the k
//           closest elements of the dataset "vote" (with their
//           label) as to what the label of a new element
//           should be. To be clear, here is a way of describing
//           a distance function, producing a integer distance
//           between two elements of a type...

typealias DistanceFunction<T> = (T, T) -> Int

//           Since this method might give an incorrect response,
//           we'll return not only predicted label, but the number
//           of "votes" received for that label (out of k)...

data class ResultWithVotes<L>(val label: L, val votes: Int)

//           Your task is to uncomment and then *test* the supplied
//           nnLabel function (note: you might need to fix up the
//           ordering of your topK arguments to play nicely with
//           the code here - you should NOT change this function).
//           You'll find guiding comments to help.
//

// // uses k-nearest-neighbor (kNN) to predict the label
// // for a supplied example given a labeled dataset
// // and distance function
// fun <E, L> nnLabel(
//     queryExample: E,
//     dataset: List<LabeledExample<E, L>>,
//     distFunc: DistanceFunction<E>,
//     k: Int,
// ): ResultWithVotes<L> {
//     // 1. Use topK to find the k-closest dataset elements:
//     //    finding the elements whose negated distance is the
//     //    greatest is the same as finding those that are closest.
//     val closestK =
//         topK(dataset, k) {
//             -distFunc(queryExample, it.example)
//         }

//     // 2. Discard the examples, we only care about their labels
//     val closestKLabels = closestK.map { it.label }

//     // 3. For each distinct label, count up how many time it
//     //    showed up in step #2
//     //    (Note: once we know the Map type, there are WAY simpler
//     //           ways to do this!)
//     val labelsWithCounts =
//         closestKLabels.distinct().map {
//                 label ->
//             Pair(
//                 // first = label
//                 label,
//                 // second = number of votes
//                 closestKLabels.filter({ it == label }).size,
//             )
//         }

//     // 4. Use topK to get the label with the greatest count
//     val topLabelWithCount = topK(labelsWithCounts, 1, { it.second })[0]

//     // 5. Return both the label and the number of votes (of k)
//     return ResultWithVotes(
//         topLabelWithCount.first,
//         topLabelWithCount.second,
//     )
// }

// @EnabledTest
// fun testNNLabel() {
//     // don't change this dataset:
//     // think of them as points on a line...
//     // (with ? referring to the example below)
//     //
//     //       a   a       ?       b           b
//     // |--- --- --- --- --- --- --- --- --- ---|
//     //   1   2   3   4   5   6   7   8   9  10
//     val dataset =
//         listOf(
//             LabeledExample(2, "a"),
//             LabeledExample(3, "a"),
//             LabeledExample(7, "b"),
//             LabeledExample(10, "b"),
//         )

//     // A simple distance: just the absolute value
//     fun myAbsVal(
//         a: Int,
//         b: Int,
//     ): Int {
//         val diff = a - b

//         return when (diff >= 0) {
//             true -> diff
//             false -> -diff
//         }
//     }

//     // TODO: to demonstrate that you understand how kNN is
//     //       supposed to work (and what the supplied code returns),
//     //       you are going to write tests here for a selection of
//     //       cases that use the dataset and distance function above.
//     //
//     //       To help you get started, consider testing for point 5,
//     //       with k=3:
//     //       a) All the points with their distances are...
//     //          a = |2 - 5| = 3
//     //          a = |3 - 5| = 3
//     //          b = |7 - 5| = 2
//     //          b = |10 - 5| = 5
//     //       b) SO, the labels of the three closest are...
//     //          a (2 votes)
//     //          b (1 vote)
//     //       c) SO, kNN in this situation would predict the label
//     //          for this point to be "a", with confidence 2/3 (medium)
//     //
//     //       We capture this test as...
//     //

//     testSame(
//         nnLabel(5, dataset, ::myAbsVal, k = 3),
//         ResultWithVotes("a", 2),
//         "NN: 5->a, 2/3",
//         // medium confidence
//     )

//     //       Now your task is to write tests for the following
//     //       additional cases...
//     //       1. 1 (k=1)
//     //       2. 1 (k=2)
//     //       3. 10 (k=1)
//     //       4. 10 (k=2)
// }

// TODO 4/5: Ok - now it's time to put some pieces together!!
//           Finish designing the function yesNoClassifier below -
//           you've been provided with guiding steps, as well as
//           tests that should pass, including those that are
//           incorrect (with lots of confidence!).
//

// we'll generally use k=3 in our classifier
val classifierK = 3

// fun yesNoClassifier(s: String): ResultWithVotes<Boolean> {
//     // 1. Convert the input to lowercase
//     //    (since) the data set is all lowercase

//     // 2. Check to see if the lower-case input
//     //    shows up exactly within the dataset
//     //    (you can assume there are no duplicates)

//     // 3. If the input was found, simply return its label with 100%
//     //    confidence (3/3); otherwise, return the result of
//     //    performing a 3-NN classification using the dataset and
//     //    Levenshtein distance metric.
// }

// @EnabledTest
// fun testYesNoClassifier() {
//     testSame(
//         yesNoClassifier("YES"),
//         ResultWithVotes(true, 3),
//         "YES: 3/3",
//     )

//     testSame(
//         yesNoClassifier("no"),
//         ResultWithVotes(false, 3),
//         "no: 3/3",
//     )

//     testSame(
//         yesNoClassifier("nadda"),
//         ResultWithVotes(false, 2),
//         "nadda: 2/3",
//     ) // pretty good ML!

//     testSame(
//         yesNoClassifier("yerp"),
//         ResultWithVotes(true, 3),
//         "yerp: 3/3",
//     ) // pretty good ML!

//     testSame(
//         yesNoClassifier("ouch"),
//         ResultWithVotes(true, 3),
//         "ouch: 3/3",
//     ) // seems very confident in this wrong answer...

//     testSame(
//         yesNoClassifier("now"),
//         ResultWithVotes(false, 3),
//         "now 3/3",
//     ) // seems very confident, given the input doesn't make sense?
// }

// TODO 5/5: Now that you have a sense of how this approach works,
//           including some of the (confident) mistakes it can make,
//           uncomment the following lines to have a classifier
//           (that we could use side-by-side with our heuristic).

// fun isPositiveML(s: String): Boolean = yesNoClassifier(s).label

// @EnabledTest
// fun testIsPositiveML() {
//     // correctly responds with positive (rote memorization)
//     for (i in 0..8) {
//         helpTestElement(i, true, ::isPositiveML)
//     }

//     // correctly responds with negative (rote memorization)
//     for (i in 9..17) {
//         helpTestElement(i, true, ::isPositiveML)
//     }
// }

// -----------------------------------------------------------------
// Final app!
// -----------------------------------------------------------------

// Whew! You've done a lot :)
//
// Now let's put it together and study!!
//

// TODO 1/2: Design the program studyDeck2 that uses the
//           reactConsole function to study through a
//           supplied deck using a supplied classifier to
//           interpret self-reported correctness.
//
//           The program should produce the following data:
//

// represents the result of a study session:
// how many questions were originally in the deck,
// how many total attempts were required to get
// them all correct!
data class StudyDeckResult(val numQuestions: Int, val numAttempts: Int)

//           Look back to the process you followed for studyDeck in
//           part 1 of the project: you'll first want to design a
//           state type, then build the main reactConsole function,
//           and finally design all the handlers (and don't forget
//           to test ALL functions, including the program!).
//
//           In case it helps, here's a trace of a short example
//           study session (using the simple classifier), with
//           notes indicated by "<--"
//
//           What is the capital of Massachusetts, USA?
//           Think of the result? Press enter to continue
//                               <-- user just pressed enter, so ""
//           Boston
//           Correct? (Y)es/(N)o
//           yup
//           What is the capital of California, USA?
//           Think of the result? Press enter to continue
//
//           Sacramento
//           Correct? (Y)es/(N)o
//           no :(                     <-- cycles Cali to the back!
//           What is the capital of the United Kingdom?
//           Think of the result? Press enter to continue
//
//           London
//           Correct? (Y)es/(N)o
//           YES!
//           What is the capital of California, USA?
//           Think of the result? Press enter to continue
//
//           Sacramento
//           Correct? (Y)es/(N)o
//           yessir!
//           Questions: 3, Attempts: 4 <-- useful summary of return
//

// Some useful prompts
val studyThink = "Think of the result? Press enter to continue"
val studyCheck = "Correct? (Y)es/(N)o"

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

// some useful labels
val optSimple = "Simple Self-Report Evaluation"
val optML = "ML Self-Report Evaluation"

// -----------------------------------------------------------------

// fun main() {
// }

runEnabledTests(this)
// main()