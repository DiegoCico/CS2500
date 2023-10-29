// <file "p1">
// <solution>
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.runEnabledTests
import khoury.testSame
// </solution>
// -----------------------------------------------------------------
// Homework 4, Problem 1
// -----------------------------------------------------------------

// In this problem we'll practice with imperative `for` loops.
// As such, your solutions should NOT use the list abstractions
// we learned last week.
//

// TODO 1/3: Finish designing the function myForEachDo that takes
//           a list of students, and a function that performs an
//           action given a student. Of course this means you need
//           to finish designing the data type Student, which
//           contains a student's first name, last name, and ID.
//
//           You have been supplied tests that should pass once
//           these steps have been completed - you should NOT
//           change the supplied code, which means the design
//           of your data type AND function must be driven by
//           the tests.
//

// <solution>
// represents a student's name and id
data class Student(
    val first: String,
    val last: String,
    val id: Int,
)

val studentAlice = Student("alice", "faythe", 1)
val studentBob = Student("bob", "mallory", 6)
val studentCarlos = Student("carlos", "sybil", 7)

// says howdy to a student
fun howdyStudent(student: Student) {
    println("Howdy, ${ student.first } ${ student.last } (${ student.id })!")
}

val howdyAlice = "Howdy, alice faythe (1)!"
val howdyBob = "Howdy, bob mallory (6)!"
val howdyCarlos = "Howdy, carlos sybil (7)!"

@EnabledTest
fun testHowdyStudent() {
    fun helpAlice() {
        howdyStudent(studentAlice)
    }

    fun helpBob() {
        howdyStudent(studentBob)
    }

    fun helpCarlos() {
        howdyStudent(studentCarlos)
    }

    testSame(
        captureResults(::helpAlice),
        CapturedResult(Unit, howdyAlice),
        "alice",
    )

    testSame(
        captureResults(::helpBob),
        CapturedResult(Unit, howdyBob),
        "bob",
    )

    testSame(
        captureResults(::helpCarlos),
        CapturedResult(Unit, howdyCarlos),
        "carlos",
    )
}
// </solution>
// val studentAlice = Student("alice", "faythe", 1)
// val studentBob = Student("bob", "mallory", 6)
// val studentCarlos = Student("carlos", "sybil", 7)

// // says howdy to a student
// fun howdyStudent(student: Student) {
//     println("Howdy, ${ student.first } ${ student.last } (${ student.id })!")
// }

// val howdyAlice = "Howdy, alice faythe (1)!"
// val howdyBob = "Howdy, bob mallory (6)!"
// val howdyCarlos = "Howdy, carlos sybil (7)!"

// @EnabledTest
// fun testHowdyStudent() {
//     fun helpAlice() {
//         howdyStudent(studentAlice)
//     }

//     fun helpBob() {
//         howdyStudent(studentBob)
//     }

//     fun helpCarlos() {
//         howdyStudent(studentCarlos)
//     }

//     testSame(
//         captureResults(::helpAlice),
//         CapturedResult(Unit, howdyAlice),
//         "alice",
//     )

//     testSame(
//         captureResults(::helpBob),
//         CapturedResult(Unit, howdyBob),
//         "bob",
//     )

//     testSame(
//         captureResults(::helpCarlos),
//         CapturedResult(Unit, howdyCarlos),
//         "carlos",
//     )
// }

// @EnabledTest
// fun testForEachDo() {
//     fun helpTest(students: List<Student>) {
//         myForEachDo(students, ::howdyStudent)
//     }

//     fun helpTestAll() {
//         helpTest(
//             listOf(
//                 studentAlice,
//                 studentBob,
//                 studentCarlos,
//             ),
//         )
//     }

//     fun helpTestNone() {
//         helpTest(emptyList<Student>())
//     }

//     testSame(
//         captureResults(::helpTestNone),
//         CapturedResult(Unit),
//         "none",
//     )

//     testSame(
//         captureResults(::helpTestAll),
//         CapturedResult(
//             Unit,
//             howdyAlice,
//             howdyBob,
//             howdyCarlos,
//         ),
//         "all",
//     )
// }

// <solution>
// replicates a special case of forEach
fun myForEachDo(
    students: List<Student>,
    action: (Student) -> Unit,
) {
    for (s in students) {
        action(s)
    }
}

@EnabledTest
fun testForEachDo() {
    fun helpTest(students: List<Student>) {
        myForEachDo(students, ::howdyStudent)
    }

    fun helpTestAll() {
        helpTest(
            listOf(
                studentAlice,
                studentBob,
                studentCarlos,
            ),
        )
    }

    fun helpTestNone() {
        helpTest(emptyList<Student>())
    }

    testSame(
        captureResults(::helpTestNone),
        CapturedResult(Unit),
        "none",
    )

    testSame(
        captureResults(::helpTestAll),
        CapturedResult(
            Unit,
            howdyAlice,
            howdyBob,
            howdyCarlos,
        ),
        "all",
    )
}
// </solution>
// TODO 2/3: Finish designing the predicate isPalindrome that
//           determines if a supplied string has the same
//           characters front-to-back as back-to-front. Since
//           we are practicing use of for-loops, we want you to
//           do this in a very specific way, so here is the
//           pseudocode for the algorithm we have in-mind:
//
//           1. Loop through all the valid indices of the
//              string...
//
//              a) if the character at that index (let's call
//                 it `i` for the moment) is not the same as
//                 `i` away from the last index, then the word
//                 is not a palindrome.
//
//           2. If you get through the end of the word, it is a
//              valid palindrome!
//
//           You have been supplied tests (comparing to a simpler
//           way to solve this problem).
//
//           Notes:
//           - As with many sequences, strings have .indices and
//             .lastIndex that you'll find quite useful.
//           - Following a known approach to solving a problem is
//             fairly common when designing software; to read more
//             about the terms in the description...
//             * https://en.wikipedia.org/wiki/Algorithm
//             * https://en.wikipedia.org/wiki/Pseudocode
//

// <solution>
// Determines if the string is the same in both directions
fun isPalindrome(s: String): Boolean {
    for (i in s.indices) {
        if (s[i] != s[s.lastIndex - i]) {
            return false
        }
    }
    return true
}

@EnabledTest
fun testIsPalindrome() {
    // simpler way of solving the problem :)
    fun shouldBe(s: String): Boolean {
        return s == s.reversed()
    }

    fun helpTest(s: String) {
        testSame(
            isPalindrome(s),
            shouldBe(s),
            s,
        )
    }

    helpTest("howdy")
    helpTest("mom")
    helpTest("taco cat".replace(" ", ""))
    helpTest("hoot")
    helpTest("toot")
}
// </solution>
// @EnabledTest
// fun testIsPalindrome() {
//     // simpler way of solving the problem :)
//     fun shouldBe(s: String): Boolean {
//         return s == s.reversed()
//     }

//     fun helpTest(s: String) {
//         testSame(
//             isPalindrome(s),
//             shouldBe(s),
//             s,
//         )
//     }

//     helpTest("howdy")
//     helpTest("mom")
//     helpTest("taco cat".replace(" ", ""))
//     helpTest("hoot")
//     helpTest("toot")
// }

// TODO 3/3: Finish designing the function printTriangle that
//           makes some simple ASCII art...
//
//           https://en.wikipedia.org/wiki/ASCII_art
//
//           In particular, your function will accept a positive
//           integer and then print to the screen that large of
//           an isosceles right-triangle made of *'s.
//
//           For example, the first three such triangles...
//
//           *
//
//           *
//           **
//
//           *
//           **
//           ***
//
//           So you'll have as many lines as the supplied number,
//           and each line has an increasing number of *'s.
//
//           While you could construct a string, instead we want
//           you to apply "nested" for-loops: that is, a loop
//           within a loop. The outer loop should handle breaks
//           between lines (via println), whereas the inner loop
//           should use print to output the appropriate number of
//           *'s - you have tests to help!
//
//           And since you are crushing it, here's a flower...
//
//           @}-,-`-.
//

// <solution>
// makes a triangle of *'s of
// the supplied size
fun printTriangle(n: Int) {
    for (row in 1..n) {
        for (col in 1..row) {
            print("*")
        }
        println()
    }
}

@EnabledTest
fun testPrintTriangle() {
    fun helpTest1() {
        printTriangle(1)
    }

    fun helpTest2() {
        printTriangle(2)
    }

    fun helpTest3() {
        printTriangle(3)
    }

    fun helpTest10() {
        printTriangle(10)
    }

    testSame(
        captureResults(::helpTest1),
        CapturedResult(
            Unit,
            "*",
        ),
        "1",
    )

    testSame(
        captureResults(::helpTest2),
        CapturedResult(
            Unit,
            "*",
            "**",
        ),
        "2",
    )

    testSame(
        captureResults(::helpTest3),
        CapturedResult(
            Unit,
            "*",
            "**",
            "***",
        ),
        "3",
    )

    testSame(
        captureResults(::helpTest10),
        CapturedResult(
            Unit,
            "*",
            "**",
            "***",
            "****",
            "*****",
            "******",
            "*******",
            "********",
            "*********",
            "**********",
        ),
        "10",
    )
}
// </solution>
// @EnabledTest
// fun testPrintTriangle() {
//     fun helpTest1() {
//         printTriangle(1)
//     }

//     fun helpTest2() {
//         printTriangle(2)
//     }

//     fun helpTest3() {
//         printTriangle(3)
//     }

//     fun helpTest10() {
//         printTriangle(10)
//     }

//     testSame(
//         captureResults(::helpTest1),
//         CapturedResult(
//             Unit,
//             "*",
//         ),
//         "1",
//     )

//     testSame(
//         captureResults(::helpTest2),
//         CapturedResult(
//             Unit,
//             "*",
//             "**",
//         ),
//         "2",
//     )

//     testSame(
//         captureResults(::helpTest3),
//         CapturedResult(
//             Unit,
//             "*",
//             "**",
//             "***",
//         ),
//         "3",
//     )

//     testSame(
//         captureResults(::helpTest10),
//         CapturedResult(
//             Unit,
//             "*",
//             "**",
//             "***",
//             "****",
//             "*****",
//             "******",
//             "*******",
//             "********",
//             "*********",
//             "**********",
//         ),
//         "10",
//     )
// }
// <file "p2">
// -----------------------------------------------------------------
// Homework 4, Problem 2
// -----------------------------------------------------------------

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

// TODO 1/3: Finish designing the function sRListToList, which
//           converts a self-referential list of integers to an
//           equivalent Kotlin list. You have tests to help, and
//           may type-driven development guide you to a well-
//           designed solution :)
//

// <solution>
// converts a self-referential list to an equivalent
// Kotlin list
fun sRListToList(srList: SRListNums): List<Int> {
    return when (srList) {
        is SRListNums.End -> emptyList<Int>()
        is SRListNums.Element -> listOf(srList.num) + sRListToList(srList.rest)
    }
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
// </solution>
// @EnabledTest
// fun testSRListToList() {
//     testSame(
//         sRListToList(srEmpty),
//         emptyList<Int>(),
//         "empty",
//     )

//     testSame(
//         sRListToList(sr9),
//         listOf(9),
//         "[9]",
//     )

//     testSame(
//         sRListToList(sr09),
//         listOf(0, 9),
//         "[0, 9]",
//     )

//     testSame(
//         sRListToList(sr8675309),
//         listOf(8, 6, 7, 5, 3, 0, 9),
//         "jenny",
//     )
// }

// TODO 2/3: Now try the opposite - listToSRList.
//
//           Hint: think about one element of the Kotlin list
//                 at a time, from left-to-right; if you had
//                 nothing in the list, what would that be?
//                 otherwise, how could you then make a self-
//                 refential list recursively?
//

// <solution>
// converts from a Kotlin list to a self-referential one
fun listToSRList(kList: List<Int>): SRListNums {
    return when (kList.isEmpty()) {
        true -> SRListNums.End
        false ->
            SRListNums.Element(
                kList[0],
                listToSRList(kList.drop(1)),
            )
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
// </solution>
// @EnabledTest
// fun testListToSRList() {
//     testSame(
//         listToSRList(emptyList<Int>()),
//         srEmpty,
//         "empty",
//     )

//     testSame(
//         listToSRList(listOf(9)),
//         sr9,
//         "[9]",
//     )

//     testSame(
//         listToSRList(listOf(0, 9)),
//         sr09,
//         "[0, 9]",
//     )

//     testSame(
//         listToSRList(listOf(8, 6, 7, 5, 3, 0, 9)),
//         sr8675309,
//         "jenny",
//     )
// }

// TODO 3/3: Lastly, finish designing filterSRList as a way to do
//           what filter does for Kotlin lists. Let type-driven
//           programming be your guide, and you have tests
//           for clarity!
//
//           Note: you should NOT use the conversion functions
//                 above just to use Kotlin's filter - try to do
//                 it recursively!
//

// <solution>
fun filterSRList(
    srList: SRListNums,
    pred: (Int) -> Boolean,
): SRListNums {
    return when (srList) {
        is SRListNums.End -> srList
        is SRListNums.Element ->
            when (pred(srList.num)) {
                false -> filterSRList(srList.rest, pred)
                true ->
                    SRListNums.Element(
                        srList.num,
                        filterSRList(srList.rest, pred),
                    )
            }
    }
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
// </solution>
// @EnabledTest
// fun testFilterSRList() {
//     // is a supplied number even?
//     fun isEven(num: Int): Boolean {
//         return num % 2 == 0
//     }

//     // is a supplied number non-positive?
//     fun isNonPositive(num: Int): Boolean {
//         return num <= 0
//     }

//     testSame(
//         filterSRList(srEmpty, ::isEven),
//         srEmpty,
//         "empty",
//     )

//     testSame(
//         filterSRList(sr8675309, ::isEven),
//         SRListNums.Element(
//             8,
//             SRListNums.Element(
//                 6,
//                 SRListNums.Element(
//                     0,
//                     SRListNums.End,
//                 ),
//             ),
//         ),
//         "even",
//     )

//     testSame(
//         filterSRList(sr8675309, ::isNonPositive),
//         SRListNums.Element(
//             0,
//             SRListNums.End,
//         ),
//         "non-positive",
//     )
// }
// <solution>
fun main() {
}

runEnabledTests(this)
main()
// </solution>
