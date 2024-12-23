package aoc2021.day1

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Input = List<Int>

class Day1 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.countIncrements() shouldBe 7
        println("Day  1, Part 1: ${data.countIncrements()} increments")
    }

    @Test
    fun part2() {
        sample.countIncrements(3) shouldBe 5
        println("Day  1, Part 2: ${data.countIncrements(3)} increments")
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
    part1()
    part2()
}
