package day13

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

// poor mans point type
typealias Point = Pair<Int, Int>

val Point.x get() = first
val Point.y get() = second

class Day13 {
    data class Input(val points: List<Point>, val folds: List<Point>)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.folded(1).count() shouldBe 17
        println("Day 13, Part 1: ${data.folded(1).count()} points")
    }

    @Test
    fun part2() {
        sample.folded().plotted().trim() shouldBe """
                #####
                #   #
                #   #
                #   #
                #####
            """.trimIndent()
        println("Day 13, Part 2:\n${data.folded().plotted()}")
    }


    fun Input.folded(numFolds: Int = folds.size) = points
        .map { origin ->
            (0 until numFolds).map { n -> folds[n] }
                .fold(origin) { p, f -> Point(foldAt(f.x, p.x), foldAt(f.y, p.y)) }
        }
        .toSet()

    fun foldAt(f: Int, v: Int) = if (f in 1..v) f * 2 - v else v


    fun Set<Point>.plotted(): String {
        val max = Point(maxOf { it.x }, maxOf { it.y })
        var sheet = ""
        for (y in 0..max.y) {
            for (x in 0..max.x) {
                sheet += if (contains(Point(x, y))) "#" else " "
            }
            sheet += "\n"
        }
        return sheet
    }


    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.trim().split(Regex("\\W+")) }
        .partition { it[0] != "fold" }
        .let { segments ->
            Input(
                segments.first.map { Point(it[0].toInt(), it[1].toInt()) },
                segments.second.map {
                    when (it[2] == "x") {
                        true -> Point(it[3].toInt(), 0)
                        else -> Point(0, it[3].toInt())
                    }
                }
            )
        }
}

fun main() = Day13().run {
    part1()
    part2()
}
