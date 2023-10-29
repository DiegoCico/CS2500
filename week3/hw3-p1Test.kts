// -----------------------------------------------------------------
// Homework 3, Problem 1
// -----------------------------------------------------------------
import khoury.EnabledTest
import khoury.fileExists
import khoury.fileReadAsList
import khoury.testSame



// Let's write a function that pulls together two related files
// of data and produces some useful summary data.
//
// Before coding, look at the two supplied example files:
//
// - names.txt:   one name (First Last) per line
//
// - records.txt: each line is a comma-separated list of either
//                W or L (win or lose); these correspond to the
//                student names in the first file (i.e., line 1
//                of names corresponds to line 1 of records...
//                until we don't have records for some students)
//                and represent magical battles fought, and how
//                each worked out. For example, Harry Potter has
//                had 7 magical battles, having won 5 of them.
//
// To make sure the students get adequate support, you are going
// to make a useful function to pull all of this into a single
// list of helpful info.
//
// TODO 1/1: Finish designing the function buildList that accepts
//           three parameters...
//           1. The desired size of the resulting list (you can
//              assume this will be non-negative).
//           2. A file path that contains first/last names,
//              one per line.
//           3. A file path that contains win/loss records,
//              where each line looks like "W,L,W,..." (some
//              number of comma-separated W/L's.

//
//           The function must produce a list of strings using
//           the list constructor:
//
//           val resultList = List<String>(
//               desiredSize,
//               ::initFunction
//           )
//
//           Here is how you should populate the resulting list
//           with the initFunction...
//
//           - For each entry, produce a string that combines the
//             first/last initials of the name in the first file,
//             with a summarization of their win/loss record
//             (either "is crushing it" if there are at least as
//             many W's as L's, or "needs support" otherwise).
//
//           - If for a particular entry, there is neither a name
//             nor a summarization record, produce an empty string.
//
//           - If there is a name, but there isn't a win/loss
//             record, simply indicate "is unknown" for the
//             record summarization.
//
//           - If there isn't a name, but there is a win/loss
//             record, simply indicate "PD" as the initials
//             (standing for "Person Doe").
//
//           To help, you have been supplied some tests. Here are
//           some hints...
//
//           - The string.split(delimeter) function will be useful
//             to produce a sequence from a string when there is a
//             known pattern of separation (like spaces in the
//             names file, and commas in the win/loss records).
//             Note that splitting an empty string will result in
//             a list with one empty string, which is often not
//             what you want :(
//
//           - Use sequence.size to determine how many elements are
//             in a sequence; you can similarly use the `in`
//             operator to determine if a particular index is in
//             sequence.indices
//
//           - To produce your initialization function, you likely
//             will need it to define it within buildList to make
//             sure that information about the names and win/loss
//             records are in scope.
//
//           - And lastly, since this problem has so many pieces,
//             here's a suggested method of attack...
//             1. Start by ge  tting the contents of the two files
//                using the fileReadAsList function from the
//                Khoury library; println to make sure they match
//                what you see in the files!
//             2. Try using one of the list functions from this
//                week (cough! map) to convert names to initials,
//                and win records to summaries; you can use
//                printing and tests to make sure this is working.
//             3. Now you are in reasonable shape to start on the
//                constructor/initialization functions described
//                above :)
//

val nameUnknown = "PD"
val recordWin = "is crushing it"
val recordLoss = "needs support"
val recordUnknown = "is unknown"

// gets all the names in each respected element
// the for loop jumps by 2 to skip the first and last name
// gets the first inital of first and last and then puts it in a list
fun getInitials(nameP: String): List<String> {
    var nameL: List<String> = fileReadAsList(nameP)
    var initialL: List<String> = emptyList<String>()
    for (i in nameL.indices step 2) {
        initialL.add(nameL[i].first() + nameL[i + 1].first())
    }
    return initialL
}

// gets all the win and lose ratio
// puts them in an list and calculates the W/L ratio
// returns the list of all the win and lose
fun getScores(wlPath: String): List<String> {
    var originalL: List<String> = fileReadAsList(wlPath)
    var wlList: List<String> = emptyList<String>()

    for (i in originalL.indices) {
        var currentLine: List<String> = originalL[i].split(",")
        var w: Int = 0
        var l: Int = 0
        for (j in currentLine.indices) {
            if (currentLine[i] == "W") {
                w++
            } else if (currentLine[i] == "L") {
                l++
            }
        }
        if (w > l) {
            wlList.add("W")
        } else {
            wlList.add("L")
        }
    }
    return wlList
}

fun buildList(
    desiredSize: Int,
    nameP: String,
    wLPath: String,
): List<String> {
    val p1: Boolean = fileExists(nameP)
    val p2: Boolean = fileExists(wLPath)
    var combine = emptyList<String>()

    if (p1 && p2) {
        var nameL: List<String> = getInitials(nameP)
        var score: List<String> = getScores(wLPath)
        for (i in desiredSize) {
            combine.add(
                "$nameL[i]" +
                    if (i < score.desiredSize) {
                        when (score[i]) {
                            "W" -> recordWin
                            else -> recordLoss
                        }
                    } else {
                        recordUnknown
                    },
            )
        }
    } else if (!p1 && p2) {
        var score: List<String> = getScores(wLPath)
        for (i in desiredSize) {
            combine.add(
                "$nameUnknown" +
                    if (i < score.desiredSize) {
                        when (score[i]) {
                            "W" -> recordWin
                            else -> recordLoss
                        }
                    } else {
                        recordUnknown
                    },
            )
        }
    } else {
        for (i in desiredSize) {
            combine.add("   ")
        }
    }
    return combine
}

@EnabledTest
fun testBuildList() {
    testSame(
        buildList(2, "BADNAMES.TXT", "BADRECORDS.TXT"),
        listOf("", ""),
        "emptiness",
    )

    testSame(
        buildList(1, "BADNAMES.TXT", "records.txt"),
        listOf("PD is crushing it"),
        "PD",
    )

    testSame(
        buildList(3, "names.txt", "records.txt"),
        listOf(
            "HP is crushing it",
            "HG is crushing it",
            "RW is crushing it",
        ),
        "instructors",
    )

    testSame(
        buildList(7, "names.txt", "records.txt"),
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
