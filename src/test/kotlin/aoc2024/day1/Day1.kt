package aoc2024.day1

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs

typealias Input = Pair<List<Int>, List<Int>>

class Day1 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.totalDistance() shouldBe 11
        println("Day  1, Part 1: total distance is ${data.totalDistance()}")
    }

    @Test
    fun part2() {
        sample.similarity() shouldBe 31
        println("Day  1, Part 2: similarity score is ${data.similarityOptimized()}")
    }


    fun Input.totalDistance() = this
        .let { (leftColumn, rightColumn) ->
            leftColumn.sorted().zip(rightColumn.sorted())
        }
        .sumOf { (left, right) -> abs(left - right) }


    // brute force -> O(n^2)
    fun Input.similarity() = this
        .let { (leftColumn, rightColumn) ->
            leftColumn.sumOf { left -> left * rightColumn.count { it == left } }
        }

    // optimized -> O(n)
    fun Input.similarityOptimized() = this
        .let { (leftColumn, rightColumn) ->
            val leftFrequencies = leftColumn.groupingBy { it }.eachCount()
            val rightFrequencies = rightColumn.groupingBy { it }.eachCount()
            leftFrequencies
                .map { (id, count) -> id * count * (rightFrequencies[id] ?: 0) }
                .sum()
        }


    fun parse(resource: String): Input = javaClass.getResource(resource)!!
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.split(Regex(" +")) }
        .map { (left, right) -> Pair(left.toInt(), right.toInt()) }
        .unzip()
}

fun main() = Day1().run {
    part1()
    part2()
}
