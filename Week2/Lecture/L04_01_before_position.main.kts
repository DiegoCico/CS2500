import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.runEnabledTests
import khoury.testSame

//User defined types!

//We want to represent 2d positions in Cartesian Plain (grid)
//In Kotlin this is called a data class

data class Position(val: x:Int, val y:Int)

//Now that we created a new type how do we use it
val positionOrigin = Position(0,0)
val position10 = Position(1,0)
val thing = position10.x                                 


// creates a position by adding the coordinates of two others
//create function called addPositions
//takes in two positions and returns a position which is the two
//added together. Positions are added component by component

fun addPositions(p1:Position, p2:Position): Position {
    return Position(p1.x + p2.x, p1.y+p2.y)
}


@EnabledTest
fun testAddPositions() {
    testSame(
        addPositions(positionOrigin, positionOrigin),
        positionOrigin,
        "2 x origin",
    )

    testSame(
        addPositions(positionOrigin, position10),
        position10,
        "origin + (1,0)",
    )

    testSame(
        addPositions(position10, positionOrigin),
        position10,
        "(1,0) + origin",
    )
}





val prompt = "Enter a number"

// gets a number from the console
// (assumes you type something reasonable)

//write a function to get integer from user
//call it typedNum 
//it takes in nothing and returns an Int


fun typedNum(): Int {
    println(prompt)
    return input().toInt()
}

@EnabledTest
fun testTypedNum() {
    testSame(
        captureResults(::typedNum, "0"),
        CapturedResult(0, prompt),
        "0",
    )

    testSame(
        captureResults(::typedNum, "13"),
        CapturedResult(13, prompt),
        "13",
    )
}

// gets a position from the console
//now write a function to get a position from 
//console use the typedNum function
//return a postion

fun typedPosition(): Position {
    
}

@EnabledTest
fun testTypedPosition() {
    testSame(
        captureResults(::typedPosition, "0", "0"),
        CapturedResult(
            positionOrigin,
            prompt,
            prompt,
        ),
        "0, 0",
    )

    testSame(
        captureResults(::typedPosition, "1", "0"),
        CapturedResult(
            position10,
            prompt,
            prompt,
        ),
        "1, 0",
    )
}

fun main() {
    // get two positions from user by calling the function
    //call them p1 and p2
   

    // add p1 and p2 using the add positions function and show the result!
 
 
    println("$p1 + $p2 = $p3")
}

runEnabledTests(this)
main()
