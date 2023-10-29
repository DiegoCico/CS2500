// -----------------------------------------------------------------
// Homework 5, Problem 4
// -----------------------------------------------------------------

import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

interface INamedThing {
    fun getName(): String
}

data class NamedInt(val num: Int) : INamedThing {
    // name is the number
    override fun getName(): String = "$num"
}

data class NamedPerson(val first: String, val last: String) : INamedThing {
    // name is combo of first/last
    override fun getName(): String = "$first $last"
}

data class SamedNamedInt(val num: Int) : INamedThing {
    // name is always the same
    override fun getName(): String = "Inigo Montoya"
}

// uses {} to go through each funciton in the list and run them
fun renderNamedThings(l: List<INamedThing>): String {
    return l.joinToString { it.getName() }
}

@EnabledTest
fun testRenderNamedThings() {
    testSame(
        renderNamedThings(emptyList<INamedThing>()),
        "",
        "empty",
    )

    testSame(
        renderNamedThings(
            listOf(
                NamedInt(0),
                NamedInt(42),
                NamedPerson("Harry", "Potter"),
                NamedPerson("Hermione", "Granger"),
                SamedNamedInt(42),
                SamedNamedInt(0),
            ),
        ),
        "0, 42, Harry Potter, Hermione Granger, Inigo Montoya, Inigo Montoya",
        "non-empty",
    )
}
runEnabledTests(this)
