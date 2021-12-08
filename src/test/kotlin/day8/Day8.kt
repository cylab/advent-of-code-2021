package day8

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.pow

typealias Input = List<Day8.Line>
typealias Segments = List<Char>

class Day8 {
    data class Line(val codes: List<Segments>, val display: List<Segments>)

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

    fun Input.count1478() = flatMap { it.display }.count { it.size in listOf(2, 3, 4, 7) }

    fun Input.sumValues() = map { it.decode() }.sum()

    fun Line.decode(): Int {
        val decoder = codes // make sure unique codes get decoded first
            .sortedByDescending { if (it.size in listOf(2, 3, 4, 7)) 8 else it.size }
            .fold(List(10) { listOf<Char>() }) { decoded, code ->
                val digit = when {
                    code.size == 2 -> 1
                    code.size == 3 -> 7
                    code.size == 4 -> 4
                    code.size == 7 -> 8
                    code.size == 6 && code.containsAll(decoded[4]) -> 9
                    code.size == 6 && code.containsAll(decoded[1]) -> 0
                    code.size == 6 -> 6
                    code.size == 5 && decoded[6].containsAll(code) -> 5
                    code.size == 5 && decoded[9].containsAll(code) -> 3
                    else -> 2
                }
                decoded.patched(digit, code)
            }
            .withIndex()
            .associate { (digit, code) -> code to digit }

        return display.reversed()
            .mapIndexed { n, digit -> decoder[digit]!! * 10f.pow(n) }
            .sum().toInt()
    }


    fun <T> List<T>.patched(setAt: Int, value: T) = mapIndexed { i, it -> if (setAt == i) value else it }

    fun String.parseLine() = split("|")
        .map { part -> part.trim().split(Regex("\\W+")).map { it.toList().sorted() } }
        .let { (codes, digits) -> Line(codes, digits) }

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.parseLine() }
}
