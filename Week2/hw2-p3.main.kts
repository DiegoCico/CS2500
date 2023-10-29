// -----------------------------------------------------------------
// Homework 2, Problem 3
// -----------------------------------------------------------------

// represents a person's first/middle/last names
data class Name(val first: String, val middle: String, val last: String)

val HJP = Name("Harry", "James", "Potter")
val HJG = Name("Hermione", "Jean", "Granger")
val RBW = Name("Ron", "Bilius", "Weasley")

// represents a pairing of two names
data class MagicPair(val p1: Name, val p2: Name)

val MAGIC_HARRY_RON = MagicPair(HJP, RBW)
val MAGIC_HARRY_HERMIONE = MagicPair(HJP, HJG)
val MAGIC_HERMIONE_RON = MagicPair(HJG, RBW)

// it gets the magic pair class it formats it correctly
// then if checks which one is bigger and it prints out the bigger number
fun numCharsNeeded(pair: MagicPair): Int {
    val n1: String = pair.p1.last + ", " + pair.p1.first + " " + pair.p1.middle
    val n2: String = pair.p2.last + ", " + pair.p2.first + " " + pair.p2.middle

    if (n1.length > n2.length) {
        return n1.length
    } else {
        return n2.length
    }
}

fun main() {
    println(numCharsNeeded("MAGIC_HERMIONE_RON: " + MAGIC_HERMIONE_RON))
    println(numCharsNeeded("MAGIC_HARRY_RON:  " + MAGIC_HARRY_RON))
    println(numCharsNeeded("MAGIC_HARRY_HERMIONE:  " + MAGIC_HARRY_HERMIONE))
}
main()
