import java.lang.ClassLoader.getSystemResource

private val day1Sample get() = parse("day01_sample.txt")
private val day1Input get() = parse("day01_input.txt")

fun day01Puzzle1() {
    println("Day  1, Puzzle 1: ${day1Input.countIncrements()} increments")
}

fun day01Puzzle2() {
    println("Day  1, Puzzle 2: ${day1Input.countIncrements(3)} increments")
}

// since with the sliding window, all elements but the first and the last are overlapping,
// we can simplify this to just compare the first and the last element and skip the sum
private fun List<Int>.countIncrements(slidingWindow: Int = 1) = this
    .windowed(slidingWindow + 1)
    .filter { it.first() < it.last() }
    .size

private fun parse(resource: String) = getSystemResource(resource)
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .map { it.toInt() }
