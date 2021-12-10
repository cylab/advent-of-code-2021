package day5

import day10.Day10
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.signum
import kotlin.math.abs
import kotlin.math.max

class Day5 {

    data class Line(val points: List<Pair<Int, Int>>, val straight: Boolean)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun puzzle1() {
        sample.filter { it.straight }.overlaps() shouldBe 5
        println("Day  5, Puzzle 1: ${data.filter { it.straight }.overlaps()} overlaps")
    }

    @Test
    fun puzzle2() {
        sample.overlaps() shouldBe 12
        println("Day  5, Puzzle 2: ${data.overlaps()} overlaps")
    }

    fun List<Line>.overlaps() = flatMap { it.points }
        .groupBy { it }
        .filter { it.value.size >= 2 }
        .size

    fun String.createLine() = trim()
        .split(Regex("\\D+"))
        .map { it.toInt() }
        .let { (x1, y1, x2, y2) ->
            val xLen = abs(x2 - x1)
            val yLen = abs(y2 - y1)
            val xDir = signum(x2 - x1)
            val yDir = signum(y2 - y1)
            assert(xLen == 0 || yLen == 0 || xLen == yLen) // only straight and diagonal lines!
            Line(
                points = (0..max(xLen, yLen)).map { n -> Pair(x1 + n * xDir, y1 + n * yDir) },
                straight = xLen == 0 || yLen == 0
            )
        }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .trim()
        .lines()
        .map { it.createLine() }
}

fun main() = Day5().run {
    puzzle1()
    puzzle2()
}
