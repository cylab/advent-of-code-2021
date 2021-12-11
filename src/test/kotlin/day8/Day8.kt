package day8

import day10.Day10
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.pow

typealias Input = List<Day8.Line>
typealias Code = List<Char>
typealias Decoder = Array<Code>

class Day8 {
    data class Line(val codes: List<Code>, val display: List<Code>)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.count1478() shouldBe 26
        println("Day  8, Part 1: ${data.count1478()} easy codes")
    }

    @Test
    fun part2() {
        sample.sumValues() shouldBe 61229
        println("Day  8, Part 2: ${data.sumValues()} summed values")
    }

    fun Input.count1478() = flatMap { it.display }.count { it.is1478() }

    fun Input.sumValues() = sumOf { it.decode() }

    fun Line.decode(): Int {
        val decoder = Decoder(10) { emptyList() }
        codes.descendingByUniquenessAndSegmentCount().forEach {
            val digit = when {
                it.size == 2 -> 1
                it.size == 3 -> 7
                it.size == 4 -> 4
                it.size == 7 -> 8
                it.size == 6 && it.containsAll(decoder[4]) -> 9
                it.size == 6 && it.containsAll(decoder[1]) -> 0
                it.size == 6 -> 6
                it.size == 5 && decoder[6].containsAll(it) -> 5
                it.size == 5 && decoder[9].containsAll(it) -> 3
                else -> 2
            }
            decoder[digit] = it
        }
        return decoder.decode(display)
    }

    fun Decoder.decode(codes: List<Code>) =
        codes.reversed().mapIndexed { n, code -> indexOf(code) * 10f.pow(n) }.sum().toInt()

    fun List<Code>.descendingByUniquenessAndSegmentCount() = sortedByDescending { if (it.is1478()) 8 else it.size }

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

fun main() = Day8().run {
    part1()
    part2()
}
