import java.lang.ClassLoader.getSystemResource
import java.lang.Integer.parseInt

fun day01Puzzle1() {
    val increments = day1Input
        .windowed(2)
        .filter { it[0] < it[1] }
        .size
    println("Day  1, Puzzle 1: $increments increments")
}

fun day01Puzzle2() {
    val increments = day1Input
        .windowed(3)
        .map { it.sum() }
        .windowed(2)
        .filter { it[0] < it[1] }
        .size
    println("Day  1, Puzzle 2: $increments increments")
}

private val day1Input get() = getSystemResource("day01_input.txt")
    .readText()
    .lines()
    .filter { it.isNotBlank() }
    .map { parseInt(it) }
