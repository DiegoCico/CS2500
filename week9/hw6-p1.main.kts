// -----------------------------------------------------------------
// Homework 6, Problem 1
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

fun myInplaceMap(l: MutableList<Int>): List<Int>{
    return l.map{it*2}
}

@EnabledTest
fun testMyInplaceMap(){
    testSame(
        myInplaceMap(mutableListOf()),
        listOf(),
        "EMPTY"
    )

    testSame(
        myInplaceMap(mutableListOf(1,2,3)),
        listOf(2,4,6),
        "123"
    )
}

runEnabledTests(this)