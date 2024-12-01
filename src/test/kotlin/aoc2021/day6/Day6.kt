package aoc2021.day6

import aoc2021.day10.Day10
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.Collections.rotate

class Day6 {

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.population(days = 80) shouldBe 5934
        sample.population_mutable(days = 80) shouldBe 5934
        println("Day  6, Part 1: ${data.population(days = 80)} laternfish")
    }

    @Test
    fun part2() {
        sample.population(days = 256) shouldBe 26984457539
        sample.population_mutable(days = 256) shouldBe 26984457539
        println("Day  6, Part 2: ${data.population(days = 256)} laternfish")
    }

    // modified iterative fibonacci with immutable data structures
    fun List<Int>.population(days: Int) = this
        .fold(List(9) { 0L }) { initial, dueIn ->
            initial.mapIndexed { i, fish -> if (i == dueIn) fish + 1L else fish }
        }
        .let { withOffspring(it, days) }
        .sum()

    fun withOffspring(initial: List<Long>, days: Int) = (1..days).fold(initial) { cycle, _ ->
        cycle.slice(1..6) + (cycle[7] + cycle.first()) + cycle.last() + cycle.first()
    }

    // the mutable variant is easier to understand and probably much faster
    fun List<Int>.population_mutable(days: Int): Long {
        val cycle = MutableList(9) { 0L }
        map { dueIn -> cycle[dueIn] += 1L }
        repeat(days) {
            cycle[7] += cycle.first()
            rotate(cycle, -1)
        }
        return cycle.sum()
    }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .trim()
        .split(Regex("\\D+"))
        .map { it.toInt() }
}

fun main() = Day6().run {
    part1()
    part2()
}
