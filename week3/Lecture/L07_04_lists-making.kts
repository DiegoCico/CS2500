// Lists: sequence of ANY type :)

// by hand
val myList1 = listOf("a", "b", "c")
val myList2 = listOf(1, 2, 3)

val myList3 =
    listOf(
        MyPeep("Harry Potter", "The Chosen One"),
        MyPeep("Hermione Granger", "Ms. Granger"),
        MyPeep("Ron Weasley", "Ron"),
    )

// empty, needs type
val myListEmptyNums = emptyList<Int>()
val myListEmptyStrings = emptyList<String>()

// adding lists
val myTotalList = listOf("a", "b", "c") + listOf("1", "2", "3")

// splitting strings
val myCommaSeparatedValues = "apple,banana,carrot".split(",")

// splitting files :)
// import khoury.fileReadAsList
// val myFileLines = fileReadAsList("howdy.txt")

// construction
// say how many you want, then provide a function that...
// takes the current index and produces the value
// (useful for the homework!)
// Note: return type of the function determines
//       the type of the list!

fun addOne(index: Int): Int {
    return index + 1
}

// basically listOf(1, 2, 3, 4, 5)
val my1Through5 = List<Int>(5, ::addOne)
