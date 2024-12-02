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
        sample.dampenedSafeReports() shouldBe 4
        println("Day  2, Part 2: dampened safe reports are ${data.dampenedSafeReports()}")
    }


    fun Input.safeReports() = this
        .map { report -> report.checkTransients().any { safe -> safe != true } }
        .count { safe -> safe != true }


    fun Input.dampenedSafeReports() = this
        .map { report ->
            val firstCheck = report.checkTransients()
            val firstUnsafe = firstCheck.withIndex().firstOrNull() { (_, save) -> save != true }?.index
            val finalCheck = when (firstUnsafe) {
                null -> firstCheck
                else -> report.filterIndexed { i, _ -> i != firstUnsafe }.checkTransients()
            }
            finalCheck.any { safe -> safe != true }
        }
        .count { safe -> safe != true }


    fun List<Int>.checkTransients(): List<Boolean> {
        val trend = (this[0] - this[1]).sign
        return windowed(2)
            .map { (current, next) ->
                val dir = (current - next).sign
                val amount = abs(current - next)
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
