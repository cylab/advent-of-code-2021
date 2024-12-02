package aoc2024.day2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.math.sign

typealias Input = List<List<Int>>

class Day2 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.safeReports() shouldBe 2
        println("Day  2, Part 1: safe reports are ${data.safeReports()}")
    }

    @Test
    fun part2() {
        sample.dampenedSafeReportsOptimized() shouldBe 4
        println("Day  2, Part 2: dampened safe reports are ${data.dampenedSafeReportsOptimized()}")
    }


    fun Input.safeReports() = this
        .map { report -> report.checkTransitions().all { safe -> safe == true } }
        .count { safe -> safe == true }


    fun Input.dampenedSafeReports() = this
        .map { report ->
            report.indices
                .map { skip ->
                    report.filterIndexed { i, _ -> i != skip }.checkTransitions().all { safe -> safe == true }
                }
                .any { safe -> safe == true }
        }
        .count { safe -> safe == true }


    // this version reduced the report-variants by first searching for an unsafe transition and removes involved values
    // the first element also has to be removed in one variant, since it defines the direction of the trend
    fun Input.dampenedSafeReportsOptimized() = this
        .mapIndexed { reportIndex, report ->
            val firstUnsafe = report.checkTransitions().withIndex().firstOrNull { (_, save) -> save != true }?.index
            when (firstUnsafe) {
                null -> true
                else -> listOf(0, firstUnsafe, firstUnsafe + 1)
                    .map { skip ->
                        report.filterIndexed { i, _ -> i != skip }.checkTransitions().all { safe -> safe == true }
                    }
                    .any { safe -> safe == true }
            }
        }
        .count { safe -> safe == true }


    fun List<Int>.checkTransitions(): List<Boolean> {
        val trend = (this[0] - this[1]).sign
        return windowed(2)
            .map { (left, right) ->
                val dir = (left - right).sign
                val amount = abs(left - right)
                dir == trend && amount in 1..3
            }
    }

    fun parse(resource: String): Input = javaClass.getResource(resource)!!
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.split(Regex(" +")).map { it.toInt() } }
}

fun main() = Day2().run {
    part1()
    part2()
}
