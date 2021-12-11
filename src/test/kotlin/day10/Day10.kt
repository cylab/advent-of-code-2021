package day10

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.Stack

typealias Input = List<Line>
typealias Line = List<Char>
typealias StartedChunks = Stack<Day10.ChunkType>

class Day10 {

    enum class ChunkType(val rank: Long, val start: Char, val end: Char, val score: Long) {
        ROUND(1, '(', ')', 3),
        SQUARE(2, '[', ']', 57),
        CURLY(3, '{', '}', 1197),
        POINTY(4, '<', '>', 25137)
    }

    data class Status(val corrupted: ChunkType? = null, val incomplete: StartedChunks? = null)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.corruption() shouldBe 26397
        println("Day 10, Part 1: ${data.corruption()} corruption score")
    }

    @Test
    fun part2() {
        sample.incompleteness() shouldBe 288957
        println("Day 10, Part 1: ${data.incompleteness()} incompleteness score")
        data.incompleteness() shouldNotBe 44087106 // my first wrong solution due to using Int instead of Long! :((
    }


    fun Input.corruption() = mapNotNull { line -> line.check().corrupted }
        .sumOf { it.score }

    fun Input.incompleteness() = mapNotNull { line -> line.check().incomplete }
        .map { it.valuate() }
        .sorted()
        .let { it[it.size / 2] }


    fun Line.check(): Status {
        val started = StartedChunks()
        onEach { char ->
            val type = chunkType(char)
            when {
                char == type.start -> started.push(type)
                started.pop() != type -> return Status(corrupted = type)
            }
        }
        return if (started.isEmpty()) Status() else Status(incomplete = started)
    }

    fun chunkType(char: Char) = ChunkType.values()
        .first { char == it.start || char == it.end }

    fun StartedChunks.valuate() = asReversed()
        .fold(0L) { score, it -> score * 5 + it.rank }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.toList() }
}

fun main() = Day10().run {
    part1()
    part2()
}
