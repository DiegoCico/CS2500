// -----------------------------------------------------------------
// Homework 4, Problem 2
// -----------------------------------------------------------------
import khoury.EnabledTest
import khoury.runEnabledTests
import khoury.testSame

// In this problem we'll practice a more functional approach:
// recursively iterating through self-referential lists...

// a self-referential list of numbers is either...
sealed class SRListNums {
    // no numbers
    object End : SRListNums()

    // an element, as well as
    // a reference to the rest
    // of the list (which could
    // be either...)
    data class Element(val num: Int, val rest: SRListNums) : SRListNums()
}

// example of building up [8, 6, 7, 5, 3, 0, 9]
val srEmpty = SRListNums.End
val sr9 = SRListNums.Element(9, srEmpty)
val sr09 = SRListNums.Element(0, sr9)
val sr309 = SRListNums.Element(3, sr09)
val sr5309 = SRListNums.Element(5, sr309)
val sr75309 = SRListNums.Element(7, sr5309)
val sr675309 = SRListNums.Element(6, sr75309)
val sr8675309 = SRListNums.Element(8, sr675309)

// gets the sealed class goes through all the elements inside of it
// and puts the num into a list
fun sRListToList(ss: SRListNums): List<Int> {
    var l = mutableListOf<Int>()

    fun putInList(sr: SRListNums): Int {
        when (sr) {
            is SRListNums.End -> 0
            is SRListNums.Element -> l.add(sr.num + putInList(sr.rest))
        }
        return 0
    }
    putInList(ss)
    return l.reversed()
}

@EnabledTest
fun testSRListToList() {
    testSame(
        sRListToList(srEmpty),
        emptyList<Int>(),
        "empty",
    )

    testSame(
        sRListToList(sr9),
        listOf(9),
        "[9]",
    )

    testSame(
        sRListToList(sr09),
        listOf(0, 9),
        "[0, 9]",
    )

    testSame(
        sRListToList(sr8675309),
        listOf(8, 6, 7, 5, 3, 0, 9),
        "jenny",
    )
}

// deletes the element looking it one by one until it recognizes the correct SRListNums used
fun listToSRList(l: List<Int>): SRListNums {
    if (l.isEmpty()) {
        return SRListNums.End
    } else {
        return SRListNums.Element(l[0], listToSRList(l.drop(1)))
    }
}

@EnabledTest
fun testListToSRList() {
    testSame(
        listToSRList(emptyList<Int>()),
        srEmpty,
        "empty",
    )

    testSame(
        listToSRList(listOf(9)),
        sr9,
        "[9]",
    )

    testSame(
        listToSRList(listOf(0, 9)),
        sr09,
        "[0, 9]",
    )

    testSame(
        listToSRList(listOf(8, 6, 7, 5, 3, 0, 9)),
        sr8675309,
        "jenny",
    )
}

// gets the even or non-positive numebrs and re runs the code till all the elements have been sorted through
fun filterSRList(
    sr: SRListNums,
    b: (Int) -> Boolean,
): SRListNums {
    if (sr is SRListNums.End) {
        return SRListNums.End
    } else {
        var el = sr as SRListNums.Element
        if (b(el.num)) {
            return SRListNums.Element(el.num, filterSRList(el.rest, b))
        } else {
            filterSRList(el.rest, b)
        }
    }
    return SRListNums.End
}

@EnabledTest
fun testFilterSRList() {
    // is a supplied number even?
    fun isEven(num: Int): Boolean {
        return num % 2 == 0
    }

    // is a supplied number non-positive?
    fun isNonPositive(num: Int): Boolean {
        return num <= 0
    }

    testSame(
        filterSRList(srEmpty, ::isEven),
        srEmpty,
        "empty",
    )

    testSame(
        filterSRList(sr8675309, ::isEven),
        SRListNums.Element(
            8,
            SRListNums.Element(
                6,
                SRListNums.Element(
                    0,
                    SRListNums.End,
                ),
            ),
        ),
        "even",
    )

    testSame(
        filterSRList(sr8675309, ::isNonPositive),
        SRListNums.Element(
            0,
            SRListNums.End,
        ),
        "non-positive",
    )
}

runEnabledTests(this)
