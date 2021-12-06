package day6

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day6 {

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.population(days = 80) shouldBe 5934
        println("Day  6, Puzzle 1: ${input.population(days = 80)} laternfish")
    }

    @Test
    fun puzzle2() {
        sample.population(days = 256) shouldBe 26984457539
        println("Day  6, Puzzle 2: ${input.population(days = 256)} laternfish")
    }

    fun List<Int>.population(days: Int) = map { it.withOffspring(days) }.sum()

    // modified iterative fibonacci
    fun Int.withOffspring(days: Int) = (1..days - this)
        .fold(listOf(1L) + List(8) { 0L }) { cycle, _ ->
            cycle.slice(1..6) + (cycle[7] + cycle.first()) + cycle.last() + cycle.first()
        }
        .sum()

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .trim()
        .split(Regex("\\D+"))
        .map { it.toInt() }
}
