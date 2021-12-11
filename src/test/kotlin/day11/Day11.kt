package day11

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.Int.Companion.MAX_VALUE

// poor mans point type
typealias Point = Pair<Int, Int>

val Point.x get() = first
val Point.y get() = second
operator fun Point.plus(other: Point) = x + other.x to y + other.y

class Day11 {

    data class Grid(val data: List<MutableList<Int>>, val xMax: Int, val yMax: Int, val size: Int)

    // make sure to read fresh data on each call
    val sample get() = parse("sample.txt")
    val data get() = parse("data.txt")

    val ADJACENT = listOf(
        Point(0, -1), Point(-1, -1), Point(-1, 0), Point(-1, 1),
        Point(0, 1), Point(1, 1), Point(1, 0), Point(1, -1)
    )

    @Test
    fun part1() {
        sample.countFlashes() shouldBe 1656
        println("Day  11, Part 1: ${data.countFlashes()} flashes")
    }

    @Test
    fun part2() {
        sample.stepWhereAllFlash() shouldBe 195
        println("Day 11, Part 2: ${data.stepWhereAllFlash()}. step")
    }

    fun Grid.countFlashes() = (1..100).sumOf { step() }

    fun Grid.stepWhereAllFlash() = (1..MAX_VALUE).asSequence().first { step() == size }

    fun Grid.step(): Int {
        var count = 0
        var visit = points()
        do {
            val flashes = visit
                .onEach { p -> data[p.y][p.x] += 1 }
                .filter { p -> data[p.y][p.x] >= 10 }
                .onEach { p -> data[p.y][p.x] = 0 }
                .distinct() // make sure to not visit neigbours of the same octopus twice!
            count += flashes.size
            visit = flashes
                .flatMap { adjacent(it) }
                .filter { p -> data[p.y][p.x] > 0 } // ignore already flashed in this step!
        } while (visit.isNotEmpty())
        return count
    }

    fun Grid.points() = (0..yMax).flatMap { y -> (0..xMax).map { x -> x to y } }

    fun Grid.adjacent(p0: Point) = ADJACENT
        .map { offset -> p0 + offset }
        .filter { it.x in 0..xMax && it.y in 0..yMax }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { line -> line.map { it.digitToInt() }.toMutableList() }
        .run { Grid(this, first().size - 1, size - 1, first().size * size) }
}

fun main() = Day11().run {
    part1()
    part2()
}
