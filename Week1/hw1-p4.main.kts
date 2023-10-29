// -----------------------------------------------------------------
// Homework 1, Problem 4
// -----------------------------------------------------------------

val DAYS_PER_WEEK = "7"
val HOURS_PER_DAY = "24"
val sleep_hours = 8
val classes = 6
val hour_classes = 14

fun main() {
    println(
        "With $sleep_hours hours of sleep, $classes  that takes around $hour_classes hours a week you will have a total of ${math()} hours per week of free time",
    )
}

fun math(): Int {
    return (DAYS_PER_WEEK.toInt() * HOURS_PER_DAY.toInt()) - (sleep_hours * DAYS_PER_WEEK.toInt()) - (classes * hour_classes)
}

main()
