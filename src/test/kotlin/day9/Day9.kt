package day9

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.signum
import java.util.Stack

typealias Point = Pair<Int, Int>

class Day9 {

    data class Input(val heights: List<List<Int>>, val numX: Int, val numY: Int)

    val NWSE = listOf(0 to -1, -1 to 0, 0 to 1, 1 to 0)

    val sample = parse("sample.txt")
    val input = parse("input.txt")

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
        .map { (x, y) -> height(x, y) + 1 }
        .sum()


    fun Input.biggestBasins() = lowPoints()
        .map { (x, y) -> basinSize(x, y) }
        .sortedDescending()
        .take(3)
        .reduce(Int::times)

    fun Input.lowPoints() = indices().filter { (x, y) -> isLowPoint(x, y) }

    fun Input.isLowPoint(x: Int, y: Int) = NWSE
        .map { (dx, dy) -> signum(height(x + dx, y + dy) - height(x, y)) }
        .sum() == 4


    fun Input.indices() = (0 until numX).asSequence().flatMap { x -> (0 until numY).asSequence().map { y -> x to y } }

    fun Input.height(x: Int, y: Int) = if (x < 0 || x >= numX || y < 0 || y >= numY) 9 else heights[x][y]

    // flood fill
    fun Input.basinSize(x0: Int, y0: Int): Int {
        val basin = mutableListOf<Point>()
        val toCheck = Stack<Point>().apply { push(x0 to y0) }
        while (toCheck.isNotEmpty()) {
            toCheck.pop().let { point ->
                val (x, y) = point
                if (height(x, y) != 9 && point !in basin) {
                    basin.add(point)
                    NWSE.forEach { (dx, dy) -> toCheck.push(x + dx to y + dy) }
                }
            }
        }
        return basin.size
    }

    operator fun Point.plus(other: Point) = first + other.first to second + other.second

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { line -> line.map { it.digitToInt() } }
        .let { Input(it, it.first().size, it.size) }
}
