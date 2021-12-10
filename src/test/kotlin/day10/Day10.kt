package day10

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.Stack

typealias Input = List<Line>
typealias Line = List<Char>
typealias Incomplete = Stack<ChunkType>

enum class ChunkType(val start: Char, val end: Char, val score: Long, val index: Long) {
    ROUND('(', ')', 3, 1),
    SQUARE('[', ']', 57, 2),
    CURLY('{', '}', 1197, 3),
    POINTY('<', '>', 25137, 4)
}

data class Error(val corrupted: ChunkType? = null, val incomplete: Incomplete? = null)

class Day10 {

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


    fun Input.corruption() = mapNotNull { it.error().corrupted }
        .sumOf { it.score }

    fun Input.incompleteness() = mapNotNull { it.error().incomplete }
        .filter { it.isNotEmpty() }
        .map { it.score() }
        .sorted()
        .let { it[it.size / 2] }


    fun Line.error(): Error {
        val started = Incomplete()
        onEach {
            val type = chunkType(it)
            when {
                (it == type.start) -> started.push(type)
                started.pop() != type -> return Error(corrupted = type)
            }
        }
        return Error(incomplete = started)
    }

    fun chunkType(char: Char) = ChunkType.values()
        .first { char == it.start || char == it.end }

    fun Incomplete.score() = asReversed()
        .fold(0L) { score, it -> score * 5 + it.index }

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

