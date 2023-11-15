// -----------------------------------------------------------------
// Project: Part 2, Summary
// -----------------------------------------------------------------

// Since working on part 1 of the project, you've learned many
// approaches that will allow us to improve both the design of
// data/functions, as well as add new functionality!
//
// == Data/Function Design ==
// - You'll enhance each flash card to support an arbitrary
//   number of "tags" (i.e., string labels).
// - You'll generalize the meaning of a deck, such as to be
//   agnostic as to the very meaning of cards (and thus
//   support a wider variety of decks).
// - You'll enhance the menu system to be re-usable, as
//   well as to support quitting (i.e., leave without forcing a
//   selection).
//
// == Application Features ==
// - You'll implement a second method for interpreting
//   self-reported correctness of a card, this time using
//   some machine learning (ML) to process natural language (NLP);
//   the user will be able to select which method to use (since
//   both methods have their tradeoffs!).
// - When a user doesn't get a card correct (via self-report),
//   that card is placed at the back of the deck; thus, a deck
//   is only completed when a user gets all cards correct.
// - You'll provide deck options that are a subset of cards
//   containing a particular tag (e.g., all "hard" cards, or
//   those in the topic of "science").
// - Once the program is run, the user will be able to study
//   as many decks as they wish, selecting subsequent decks
//   from the menu until they quit.

// Of course, we'll design this program step-by-step :)

// When designing this enhanced project, you are welcome to draw
// upon your project part 1, our sample solutions (for part 1, and
// any homework), and/or lecture notes as you see fit & helpful.

// Lastly, here are a few overall project requirements...
// - Now that mutation has been covered, you may use it (unless
//   otherwise stated in the instructions); however, your usage
//   will be evaluated based upon the guidelines from class.
// - As included in the instructions, all interactive parts of
//   this program MUST make effective use of the reactConsole
//   framework.
// - Staying consistent with our Style Guide...
//   * All functions must have:
//     a) a preceding comment specifying what it does
//     b) an associated @EnabledTest function with sufficient
//        tests using testSame
//   * All data must have:
//     a) a preceding comment specifying what it represents
//     b) associated representative examples
//     c) for classes with member functions, an associated
//        @EnabledTest function with sufficient tests for all
//        the member functions of the class
// - You will be evaluated on a number of criteria, including...
//   * Adherence to instructions and the Style Guide
//   * Correctly producing the functionality of the program
//   * Design decisions that include choice of tests, appropriate
//     application of programming approaches (e.g., sequence
//     abstractions, recursion, mutation), and task/type-driven
//     decomposition of functions.
//

// -----------------------------------------------------------------
// Flash Card data design
// (Hint: see Homework 5, Problem 3)
// -----------------------------------------------------------------

// TODO 1/1: Design the data type TaggedFlashCard to represent a
//           single flash card.
//
//           You should be able to represent the text prompt on
//           the front of the card, the text answer on the back,
//           as well as any number of textual tags (such as "hard"
//           or "science" -- this shouldn't come from any fixed
//           set of options, but truly open to however someone
//           wishes to categorize their cards).
//
//           Each card should have two member functions:
//           - isTagged, which determines if the card has a
//             supplied tag (e.g., has this card been tagged
//             as "hard"?)
//           - fileFormat, which produces a textual representation
//             of the card as "front|back|tag1,tag2,..."; that is
//             all three parts of the card separated with the pipe
//             ('|') character, and further separate any tags with
//             a comma
//
//           Include *at least* 3 example cards (which will come
//           in handy later for tests!), and make sure to test
//           the required member functions.
//

// (just useful values for
// the separation characters)
val sepCard = "|"
val sepTag = ","

// -----------------------------------------------------------------
// Files of tagged flash cards
// -----------------------------------------------------------------

// Now that we have our updated cards, let's update how we read
// them from files.

// TODO 1/2: Design the function stringToTaggedFlashCard that
//           takes a string, assumed to be in the format described
//           for the fileFormat member function above, and produces
//           the corresponding tagged flash card.
//
//           Hint: review part 1 of the project, TODO 2/3
//

// TODO 2/2: Design the function readTaggedFlashCardsFile that
//           takes a path to a file and produces a list of
//           tagged flash cards.
//
//           If the file does not exist, return an empty list.
//           Otherwise, you can assume that every line is
//           formatted in the string format we just worked with.
//
//           Hint:
//           - Review part 1 of the project, TODO 3/3
//           - We've provided an "example_tagged.txt" file that you
//             can use for testing if you'd like; also make sure to
//             test your function when the supplied file does not
//             exist!
//

// -----------------------------------------------------------------
// Deck design
// -----------------------------------------------------------------

// If you think about it, once a deck has been selected, our study
// application doesn't need much information about cards to work...
// in fact, it doesn't even need the concept of a card. Consider
// the following:
//

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

// This contract of operations will allow our study application to
// work with a variety of sources, including lists and even code
// that never explicitly stores cards!
//
// (For a similar problem, see Homework 6, Problem 3, TODO 2,
// where you implemented stateful classes to integrate with an
// object-oriented reactConsole.)
//

// TODO 1/2: Design TFCListDeck to implement the IDeck interface
//           for a supplied list of tagged flash cards. For this
//           problem your class must have *no* mutable state and
//           all member data should be private.
//
//           When testing, make sure to test the behavior of all
//           the member functions of the interface in a variety
//           of situations.
//
//           Hint: using default arguments can make your class
//                 easier to create initially, see...
//
//           kotlinlang.org/docs/functions.html#default-arguments
//

// TODO 2/2: Now design PerfectSquaresDeck to implement the IDeck
//           interface. You are *not* allowed to generate any
//           flash cards, nor have mutable state; the goal is to
//           act as though it had a list produced by the
//           perfectSquares function in part 1 of the project,
//           but without ever having to generate all those cards!
//           Again, as is generally good practice, keep all your
//           member data private!
//
//           Hint: you will still need to keep track of the
//                 *sequence* of upcoming numbers (particularly
//                 as some may get cycled back due to incorrect
//                 responses).
//

// -----------------------------------------------------------------
// Menu design
// -----------------------------------------------------------------

// The chooseOption function in part 1 of the project was good, but
// let's see what we can do to improve upon it in two core ways...
//
// a) Part 1 allowed you to select from amongst decks, which means
//    you'd have to copy-paste if you wanted to have a menu of
//    other data (such as files, or months of the year); let's
//    make the function agnostic as to the type of the list items
//    being selected.
// b) Part 1 didn't allow for the possibility of not selecting an
//    option; let's add a quit feature!
//
// To help with (a), consider the following interface, which
// requires that a menu option be able to return a textual
// representation (that is then displayed in the menu!)...
//

// the only required capability for a menu option
// is to be able to render a title
interface IMenuOption {
    fun menuTitle(): String
}

// as well as the following general implementation (great for
// tests & examples), which satisfies the contract via pairing
// a value (of any type) with a name...

// a menu option with a single value and name
data class NamedMenuOption<T>(val option: T, val name: String) : IMenuOption {
    override fun menuTitle(): String = name
}

// individual examples, as well as a list
// (an example for a list of menu options!)
val opt1A = NamedMenuOption(1, "apple")
val opt2B = NamedMenuOption(2, "banana")
val optsExample = listOf(opt1A, opt2B)

// TODO 1/1: Finish designing the program chooseMenuOption that
//           takes a list (assumed to be non-empty) of any type
//           (as long as it implements the IMenuOption interface),
//           produces a corresponding numbered menu (1-# of list
//           items, each showing its menuTitle), and returns the
//           list item corresponding to the number entered (or null
//           if 0 was entered to indicate a desire to quit without
//           choosing an option). Keep displaying the menu until a
//           valid menu selection (or quitting) is indicated.
//
//           Hints:
//           - You'll find the code from chooseOption (in part 1)
//             to be a *very* good starting point.
//           - Homework 5, Problem 4, has a very similar interface,
//             which can give you an idea for how you'd use it.
//           - To help you get started, you have some examples
//             above and prompts below; a "stub" for the
//             chooseMenuOption function (to help with the
//             signature and overall structure); and a set of
//             tests that should pass once the program has been
//             completed.
//

// Some useful outputs
val menuPrompt = "Enter your choice (or 0 to quit)"
val menuQuit = "You quit"
val menuChoicePrefix = "You chose: "

// // Provides an interactive opportunity for the user to choose
// // an option or quit.
// fun <T : IMenuOption> chooseMenuOption(options: List<T>): T? {
//     // your code here!
//     // - call reactConsole (with appropriate handlers)
//     // - return the selected option (or null for quit)
// }

// @EnabledTest
// fun testChooseMenuOption() {
//     testSame(
//         captureResults(
//             { chooseMenuOption(listOf(opt1A)) },
//             "howdy",
//             "0",
//         ),
//         CapturedResult(
//             null,
//             "1. ${opt1A.name}",
//             "",
//             menuPrompt,
//             "1. ${opt1A.name}",
//             "",
//             menuPrompt,
//             menuQuit,
//         ),
//         "quit",
//     )

//     testSame(
//         captureResults(
//             { chooseMenuOption(optsExample) },
//             "hello",
//             "10",
//             "-3",
//             "1",
//         ),
//         CapturedResult(
//             opt1A,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "${menuChoicePrefix}${opt1A.name}",
//         ),
//         "1",
//     )

//     testSame(
//         captureResults(
//             { chooseMenuOption(optsExample) },
//             "3",
//             "-1",
//             "2",
//         ),
//         CapturedResult(
//             opt2B,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "1. ${opt1A.name}", "2. ${opt2B.name}", "", menuPrompt,
//             "${menuChoicePrefix}${opt2B.name}",
//         ),
//         "2",
//     )
// }

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

// runEnabledTests(this)
// main()
