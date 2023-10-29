// -----------------------------------------------------------------
// Homework 3, Problem 1
// -----------------------------------------------------------------
import khoury.EnabledTest
import khoury.fileExists
import khoury.fileReadAsList
import khoury.testSame

val nameUnknown = "PD"
val recordWin = "is crushing it"
val recordLoss = "needs support"
val recordUnknown = "is unknown"

// goes through each line and counts the score, wether the person won or lost
fun getScores(s: String): String {
    val n: List<String> = s.split(",")
    var w = 0
    var l = 0
    for (i in n.indices) {
        if (n[i] == "W") {
            w++
        } else {
            l++
        }
    }
    if (w >= l) {
        return recordWin
    } else {
        return recordLoss
    }
}

// seperates the string into a list and gets the first word on both of them
fun getInitials(s: String): String {
    val n: List<String> = s.split(" ")
    return "" + n[0].first() + n[1].first()
}

// builds the list and has a for loop to format it the correct way
// also checks if it is unable to be checked then it takes a different aproach
fun buildList(
    desiredSize: Int,
    nameP: String,
    wLPath: String,
): List<String> {
    val nameL: List<String> = fileReadAsList(nameP)
    val wlPath: List<String> = fileReadAsList(wLPath)
    val initials: List<String> = nameL.map(::getInitials)
    val scores: List<String> = wlPath.map(::getScores)
    var combine = mutableListOf<String>()

    for (i in 0..desiredSize - 1) {
        if (!fileExists(nameP) && !fileExists(wLPath)) {
            combine.add("")
        } else if (!fileExists(nameP) && fileExists(wLPath)) {
            combine.add("$nameUnknown ${scores[i]}")
        } else {
            if (i < scores.size) {
                combine.add(initials[i] + " " + scores[i])
            } else {
                combine.add(initials[i] + " " + recordUnknown)
            }
        }
    }
    return combine
}

fun main() {
    buildList(3, "./week3/names.txt", "./week3/records.txt")
}
main()

@EnabledTest
fun testBuildList() {
    testSame(
        buildList(2, "./week3/BADNAMES.TXT", "./week3/BADRECORDS.TXT"),
        listOf("", ""),
        "emptiness",
    )

    testSame(
        buildList(1, "./week3/BADNAMES.TXT", "./week3/records.txt"),
        listOf("PD is crushing it"),
        "PD",
    )

    testSame(
        buildList(3, "./week3/names.txt", "./week3/records.txt"),
        listOf(
            "HP is crushing it",
            "HG is crushing it",
            "RW is crushing it",
        ),
        "instructors",
    )

    testSame(
        buildList(7, "./week3/names.txt", "./week3/records.txt"),
        listOf(
            "HP is crushing it",
            "HG is crushing it",
            "RW is crushing it",
            "CC needs support",
            "NL is crushing it",
            "LL is unknown",
            "GW is unknown",
        ),
        "army",
    )
}

testBuildList()
