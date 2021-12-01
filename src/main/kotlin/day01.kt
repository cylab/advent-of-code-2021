import java.lang.ClassLoader.getSystemResource
import java.lang.Integer.parseInt

fun day01Puzzle1() {
    val increments = getSystemResource("day01_puzzle1_input.txt")
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { parseInt(it) }
        .windowed(2)
        .filter { it[0] < it[1] }
        .size
    println("Day  1, Puzzle 1: $increments increments")
}
