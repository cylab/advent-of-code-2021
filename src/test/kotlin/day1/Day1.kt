package day1

import day10.Day10
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Input = List<Int>

class Day1 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun puzzle1() {
        sample.countIncrements() shouldBe 7
        println("Day  1, Puzzle 1: ${data.countIncrements()} increments")
    }

    @Test
    fun puzzle2() {
        sample.countIncrements(3) shouldBe 5
        println("Day  1, Puzzle 2: ${data.countIncrements(3)} increments")
    }


    // since with the sliding window, all elements but the first and the last are overlapping,
    // we can simplify this to just compare the first and the last element and skip the sum
    fun Input.countIncrements(slidingWindow: Int = 1) = this
        .windowed(slidingWindow + 1)
        .filter { it.first() < it.last() }
        .size


    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.toInt() }
}

fun main() = Day1().run {
    puzzle1()
    puzzle2()
}
