val nums = listOf(1, 3, 5)
println(nums)

// adds 1 to a supplied number
fun add1(n: Int): Int {
    return n + 1
}
// @EnabledTest should be here!

// increment the numbers
val incremented = nums.map(::add1)
println("incremented: $incremented")

// adds a supplied number to a supplied sum
fun addToSum(
    sum: Int,
    n: Int,
): Int {
    return sum + n
}
// @EnabledTest should be here!

// sum the numbers
val summed = nums.fold(0, ::addToSum)
println("summed: $summed")

// adds 1 to the count, ignoring the value
fun addToCount(
    count: Int,
    n: Int,
): Int {
    return count + 1
}
// @EnabledTest should be here!

// count the enumbers (just an example, should just .size)
val counted = nums.fold(0, ::addToCount)
println("counted: $counted")
