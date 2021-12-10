package day3

import day10.Day10
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

typealias Input = List<String>

class Day3 {

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun puzzle1() {
        sample.gammaAndEpsilon() shouldBe 198
        println("Day  3, Puzzle 1: ${data.gammaAndEpsilon()} ratings")
    }

    @Test
    fun puzzle2() {
        sample.oxygenAndCO2() shouldBe 230
        println("Day  3, Puzzle 2: ${data.oxygenAndCO2()} ratings")
    }


    fun Input.gammaAndEpsilon() = gamma().let { it * epsilon(it) }

    fun Input.oxygenAndCO2() = findByBits({ gamma() }) * findByBits({ epsilon() })


    fun Input.gamma() = this
        .fold(List(width) { 0f }) { acc, line ->
            acc.mapIndexed { i, it -> it + line.slice(i..i).toInt() }
        }
        .foldIndexed(0) { i, acc, it ->
            acc + (it / size).roundToInt().shl(width - i - 1)
        }

    fun Input.epsilon(gamma: Int = gamma()) = gamma.xor((1 shl width) - 1)


    tailrec fun Input.findByBits(bitsFun: Input.() -> Int, pos: Int = width - 1): Int {
        val bits = bitsFun()
        val matchesAtPos = filter { it.toInt(2).xor(bits).and(1 shl pos) == 0 }
        return when (matchesAtPos.size == 1) {
            true -> matchesAtPos.first().toInt(2)
            else -> matchesAtPos.findByBits(bitsFun, pos - 1)
        }
    }

    val Input.width get() = first().length

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
}

fun main() = Day3().run {
    puzzle1()
    puzzle2()
}
