fun <T> test(l: MutableList<T>, func: (MutableList<T>) -> List<T>): List<T> {
    return func(l)
}

    var l = mutableListOf(1, 2, 3, 4, 5) // Example list of integers

    val modifiedList = test(l, { t: MutableList<Int> -> t.map { it * 2 } })

    println(modifiedList) // Output: [2, 4, 6, 8, 10]
