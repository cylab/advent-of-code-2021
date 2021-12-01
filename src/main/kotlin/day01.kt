import java.lang.ClassLoader.getSystemResource
import java.lang.Integer.parseInt

fun day01Puzzle1() {
    println("Day  1, Puzzle 1: ${day1Input.countIncrements()} increments")
}

fun day01Puzzle2() {
    println("Day  1, Puzzle 2: ${day1Input.countIncrements(3)} increments")
}

private fun List<Int>.countIncrements(slidingWindow: Int = 1) = this
    // since with the sliding window, all elements but the first and the last are overlapping, we can simplify this
    // to just compare the first and the last element and skip the sum
    .windowed(slidingWindow + 1)
    .filter { it.first() < it.last() }
    .size

private val day1Input get() = getSystemResource("day01_input.txt")
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .map { parseInt(it) }
