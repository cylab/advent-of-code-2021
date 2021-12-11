package day9

import day10.Day10
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.signum
import java.util.Stack

// poor mans point type
typealias Point = Pair<Int, Int>

val Point.x get() = first
val Point.y get() = second
operator fun Point.plus(other: Point) = x + other.x to y + other.y

class Day9 {

    data class Input(val heights: List<List<Int>>, val xMax: Int, val yMax: Int)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    val NWSE = listOf(0 to -1, -1 to 0, 0 to 1, 1 to 0)

    @Test
    fun part1() {
        sample.sumLows() shouldBe 15
        println("Day  9, Puzzle 1: ${data.sumLows()} lows")
    }

    @Test
    fun part2() {
        sample.biggestBasins() shouldBe 1134
        println("Day  9, Puzzle 2: ${data.biggestBasins()} basins")
    }

    fun Input.sumLows() = lowPoints()
        .sumOf { height(it) + 1 }

    fun Input.biggestBasins() = lowPoints()
        .map { basin(it).size }
        .sortedDescending()
        .take(3)
        .reduce(Int::times)


    fun Input.points() = (0..xMax).asSequence()
        .flatMap { x -> (0..yMax).asSequence().map { y -> x to y } }

    fun Input.lowPoints() = points()
        .filter { p -> NWSE.sumOf { signum(height(p + it) - height(p)) } == 4 }

    fun Input.height(p: Point) = when {
        p.x in 0..xMax && p.y in 0..yMax -> heights[p.y][p.x]
        else -> 9
    }

    // flood fill
    fun Input.basin(p0: Point): List<Point> {
        val basin = mutableListOf<Point>()
        val toCheck = Stack<Point>().apply { push(p0) }
        while (toCheck.isNotEmpty()) {
            val pN = toCheck.pop()
            if (height(pN) != 9 && pN !in basin) {
                basin.add(pN)
                NWSE.forEach { toCheck.push(pN + it) }
            }
        }
        return basin
    }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { line -> line.map { it.digitToInt() } }
        .let { Input(it, it.first().size - 1, it.size - 1) }
}

fun main() = Day9().run {
    part1()
    part2()
}
