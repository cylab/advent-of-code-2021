package day8

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.pow

typealias Input = List<Day8.Line>
typealias Code = List<Char>

class Day8 {
    data class Line(val codes: List<Code>, val display: List<Code>)

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.count1478() shouldBe 26
        println("Day  8, Puzzle 1: ${input.count1478()} easy codes")
    }

    @Test
    fun puzzle2() {
        sample.sumValues() shouldBe 61229
        println("Day  8, Puzzle 2: ${input.sumValues()} summed values")
    }

    fun Input.count1478() = flatMap { it.display }.count { it.is1478() }

    fun Input.sumValues() = map { it.decode() }.sum()

    fun Line.decode(): Int {
        val decoded = Array<Code>(10) { emptyList() }
        codes.sortedByDescending { if (it.is1478()) 8 else it.size } // make sure unique codes get decoded first
            .forEach {
                val digit = when {
                    it.size == 2 -> 1
                    it.size == 3 -> 7
                    it.size == 4 -> 4
                    it.size == 7 -> 8
                    it.size == 6 && it.containsAll(decoded[4]) -> 9
                    it.size == 6 && it.containsAll(decoded[1]) -> 0
                    it.size == 6 -> 6
                    it.size == 5 && decoded[6].containsAll(it) -> 5
                    it.size == 5 && decoded[9].containsAll(it) -> 3
                    else -> 2
                }
                decoded[digit] = it
            }

        return display.reversed()
            .mapIndexed { n, code -> decoded.indexOf(code) * 10f.pow(n) }
            .sum().toInt()
    }

    fun Code.is1478() = size in listOf(2, 3, 4, 7)

    fun String.parseLine() = trim()
        .split(Regex("\\W+"))
        .map { it.toList().sorted() }
        .run { Line(take(10), takeLast(4)) }

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.parseLine() }
}