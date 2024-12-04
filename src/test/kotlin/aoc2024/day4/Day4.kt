package aoc2024.day4

import aoc2024.day2.Day2
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlin.math.min

class Day4 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.numXmas() shouldBe 18
        println("Day  4, Part 1: num XMAS are ${data.numXmas()}")
    }

    @Test
    fun part2() {
        sample.numCrossMas() shouldBe 9
        println("Day  4, Part 2: num X-MAS are ${data.numCrossMas()}")
    }


    fun String.numXmas() = (lines() + columns() + diagonals(4))
        .sumOf { line -> Regex("""(?=XMAS)|(?=SAMX)""").findAll(line).count() }


    fun String.numCrossMas() = lines().run {
        val ms = setOf('M', 'S')
        (1..first().length - 2)
            .flatMap { x -> (1..size - 2).map { y -> Pair(x, y) } }
            .count { (x, y) ->
                get(y)[x] == 'A' && diagChars(y, x).containsAll(ms) && diagChars(y, x, 1).containsAll(ms)
            }
    }

    fun List<String>.diagChars(y: Int, x: Int, dir: Int = -1) = setOf(get(y - 1)[x + 1 * dir], get(y + 1)[x - 1 * dir])


    fun String.columns() = lines().let { lines ->
        lines.first().indices.map {
            lines.map { line -> line[it] }.joinToString("")
        }
    }

    fun String.diagonals(minLength: Int = 1) = lines().let { lines ->
        val xRange = lines.first().indices
        val yRange = lines.indices
        val maxLength = max(xRange.last, yRange.last) + 1
        val maxDiagonals = xRange.last + yRange.last + 1

        (-1..1 step 2).flatMap { dir ->
            (minLength - 1..(maxDiagonals - minLength)).map { offset ->
                val xStart = if (dir > 0) max(0, xRange.last - offset) else min(offset, xRange.last)
                val yStart = max(0, offset - xRange.last)
                generateSequence(Pair(xStart, yStart)) { (x, y) -> Pair(x + dir, y + 1) }
                    .take(maxLength)
                    .filter { (x, y) -> x in xRange && y in yRange }
                    .map { (x, y) -> lines[y][x] }
                    .joinToString("")
            }
        }
    }


    fun parse(resource: String): String = javaClass.getResource(resource)!!
        .readText().trim()
}

fun main() = Day2().run {
    part1()
    part2()
}
