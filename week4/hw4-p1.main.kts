// -----------------------------------------------------------------
// Homework 4, Problem 1
// -----------------------------------------------------------------
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.runEnabledTests
import khoury.testSame

// TODO 1
data class Student(val first: String, val last: String, val id: Int)
val studentAlice = Student("alice", "faythe", 1)
val studentBob = Student("bob", "mallory", 6)
val studentCarlos = Student("carlos", "sybil", 7)

fun myForEachDo(
    l: List<Student>,
    f: (Student) -> Unit,
) {
    for (i in l) {
        f(i)
    }
}

// // says howdy to a student
fun howdyStudent(student: Student) {
    println("Howdy, ${student.first} ${student.last} (${student.id})!")
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

// TODO 2
// breaks the string into lists then checks each end with the start
fun isPalindrome(s: String): Boolean {
    val l: List<Char> = s.toList()
    for (i in 0..l.size / 2) {
        if (l[i] != l[(l.size - 1) - i]) {
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

// TODO 3

fun printTriangle(s: Int) {
    for (i in 1..s) {
        for (j in s - i..s - 1) {
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

runEnabledTests(this)
