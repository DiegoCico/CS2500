// -----------------------------------------------------------------
// Homework 3, Problem 2
// -----------------------------------------------------------------

// made 2 lists with flashcards inside of them one contning 2 words the other not
data class FlashCard(val front: String, val back: String)
val f1: FlashCard = FlashCard("what is a string", "texts")
val f2: FlashCard = FlashCard("what is a Int", "wholenumbers")
val f3: FlashCard = FlashCard("What is a double", "decimals")
val flashArray: List<FlashCard> = listOf(f1, f2, f3)
val f4: FlashCard = FlashCard("what your name", "Diego cicotoste")
val f5: FlashCard = FlashCard("what is your major", "Comp Sci/Neuro")
val f6: FlashCard = FlashCard("How old are you", "18 years old")
val flashArray2: List<FlashCard> = listOf(f4, f5, f6)

// made a place to name the decks and save the flashcards
data class Deck(val name: String, val cards: List<FlashCard>)
val d1: Deck = Deck("coding", flashArray)
val d2: Deck = Deck("personal", flashArray2)

// check if it is one word answers or more
fun areAllOneWordAnswers(d: Deck): Boolean {
    for (i in d.cards.indices) {
        if (d.cards[i].back.contains(" ")) {
            return true
        }
    }
    return false
}

// checks if the phrash is inside the deck
fun anyContainsPhrase(
    d: Deck,
    w: String,
): Boolean {
    for (i in d.cards.indices) {
        if (d.cards[i].back.contains(w)) {
            return true
        }
    }
    return false
}

// testing out everything
fun main() {
    println("FLASHCARDS ONE")
    println(d1)
    println("Contains space ? ${areAllOneWordAnswers(d1)}")
    println("FLASHCARDS TWO")
    println(d2)
    println("Contains space ? ${areAllOneWordAnswers(d2)}")

    println("checking if deck1 has the word diego in it")
    println(anyContainsPhrase(d1, "Diego"))
    println("checking if deck2 has the word diego in it")
    println(anyContainsPhrase(d2, "Diego"))
}

main()
