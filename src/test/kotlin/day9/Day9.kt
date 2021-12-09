package day9

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

    data class Input(val heights: List<List<Int>>, val numX: Int, val numY: Int)

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    val NWSE = listOf(0 to -1, -1 to 0, 0 to 1, 1 to 0)

    @Test
    fun puzzle1() {
        sample.sumLows() shouldBe 15
        println("Day  9, Puzzle 1: ${input.sumLows()} lows")
    }

    @Test
    fun puzzle2() {
        sample.biggestBasins() shouldBe 1134
        println("Day  9, Puzzle 2: ${input.biggestBasins()} basins")
    }

    fun Input.sumLows() = lowPoints()
        .map { height(it) + 1 }
        .sum()

    fun Input.biggestBasins() = lowPoints()
        .map { basin(it).size }
        .sortedDescending()
        .take(3)
        .reduce(Int::times)


    fun Input.points() = (0 until numX).asSequence()
        .flatMap { x -> (0 until numY).asSequence().map { y -> x to y } }

    fun Input.lowPoints() = points()
        .filter { p -> NWSE.map { signum(height(p + it) - height(p)) }.sum() == 4 }

    fun Input.height(p: Point) = when {
        p.x in 0..numX && p.y in 0..numY -> heights[p.y][p.x]
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
        .let { Input(it, it.first().size, it.size) }
}
