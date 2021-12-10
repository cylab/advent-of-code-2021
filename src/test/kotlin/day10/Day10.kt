package day10

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import java.util.Stack

typealias Input = List<Line>
typealias Line = List<Char>

class Day10 {
    enum class Delimiter(val start: Char, val end: Char, val corrupted: Int, val incomplete: Int) {
        ROUND('(', ')', 3, 1), SQUARE('[', ']', 57, 2), CURLY('{', '}', 1197, 3), POINTY('<', '>', 25137, 4)
    }

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.incompleteness() shouldBe 26397
        println("Day 10, Puzzle 1: ${input.incompleteness()} error score")
    }

    @Test
    fun puzzle2() {
        sample.corruption() shouldBe 288957
        println("Day 10, Puzzle 1: ${input.corruption()} incomplete score")
        input.corruption() shouldNotBe 44087106 // meine Lösung ist falsch, obwohl beim sample richtig :((
    }

    fun Input.incompleteness() = sumOf { it.errors().first }

    fun Input.corruption() = map { it.errors().second }
        .filter { it != 0 }
        .sorted()
        .let { it[it.size / 2] }


    fun Line.errors(): Pair<Int, Int> {
        val opened = Stack<Delimiter>()
        onEach {
            val chunkDelimiter = it.toDelimiter()
            when (it == chunkDelimiter.start) {
                true -> opened.push(chunkDelimiter)
                else -> {
                    val expected = opened.pop()
                    if (expected != chunkDelimiter) {
                        return chunkDelimiter.corrupted to 0
                    }
                }
            }
        }
        return 0 to opened.incompleteScore()
    }

    fun Stack<Delimiter>.incompleteScore() = asReversed().fold(0) { score, it -> score * 5 + it.incomplete }

    fun Char.toDelimiter() = Delimiter.values().first { this == it.end || this == it.start }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.toList() }
}
