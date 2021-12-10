package day10

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.Stack

typealias Input = List<Line>
typealias Line = List<Char>

class Day10 {
    enum class ChunkTypes(val start: Char, val end: Char, val corrupted: Long, val incomplete: Long) {
        ROUND('(', ')', 3, 1), SQUARE('[', ']', 57, 2), CURLY('{', '}', 1197, 3), POINTY('<', '>', 25137, 4)
    }

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun puzzle1() {
        sample.corruption() shouldBe 26397
        println("Day 10, Puzzle 1: ${data.corruption()} corruption score")
    }

    @Test
    fun puzzle2() {
        sample.incompleteness() shouldBe 288957
        println("Day 10, Puzzle 1: ${data.incompleteness()} incompleteness score")
        data.incompleteness() shouldNotBe 44087106 // my first wrong solution due to using Int instead of Long! :((
    }


    fun Input.corruption() = sumOf { it.errors().first }

    fun Input.incompleteness() = map { it.errors().second }
        .filter { it != 0L }
        .sorted()
        .let { it[it.size / 2] }


    fun Line.errors(): Pair<Long, Long> {
        val opened = Stack<ChunkTypes>()
        onEach {
            val type = it.chunkType()
            when {
                (it == type.start) -> opened.push(type)
                opened.pop() != type -> return Pair(type.corrupted, 0)
            }
        }
        return Pair(0, opened.incompleteScore())
    }

    fun Stack<ChunkTypes>.incompleteScore() = asReversed()
        .fold(0L) { score, it -> score * 5 + it.incomplete }

    fun Char.chunkType() = ChunkTypes.values()
        .first { this == it.end || this == it.start }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.toList() }
}

fun main() = Day10().run {
    puzzle1()
    puzzle2()
}

