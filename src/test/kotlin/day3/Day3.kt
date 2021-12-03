package day3

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.roundToInt

typealias Input = List<String>

class Day3 {

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.gammaAndEpsilon().let { (gamma, epsilon) ->
            println("gamma: $gamma, epsilon: $epsilon")
            gamma * epsilon shouldBe 198
        }
        input.gammaAndEpsilon().let { (gamma, epsilon) ->
            println("Day  3, Puzzle 1: ${gamma * epsilon} ratings")
        }
    }

    @Test
    fun puzzle2() {
        sample.oxygenAndCO2().let { (oxygen, co2) ->
            println("oxygen: $oxygen, c02: $co2")
            oxygen * co2 shouldBe 230
        }
        input.oxygenAndCO2().let { (oxygen, co2) ->
            println("Day  3, Puzzle 2: ${oxygen * co2} ratings")
        }
    }


    fun Input.gammaAndEpsilon() = gamma().let { Pair(it, epsilon(it)) }

    fun Input.oxygenAndCO2() = Pair(foldByBits({ gamma() }), foldByBits({ epsilon() }))


    fun Input.gamma() = this
        .fold(List(width) { 0f }) { acc, vals ->
            acc.mapIndexed { i, it -> it + vals[i].toString().toInt() }
        }
        .let { it.map { v -> (v / size).roundToInt() }.joinToString("").toInt(2) }

    fun Input.epsilon(gamma: Int = gamma()) = gamma.xor((1 shl width) - 1)


    fun Input.foldByBits(bitsFun: Input.() -> Int, bitNum: Int = width - 1): Int =
        when (size == 1 || bitNum < 0) {
            true -> get(0).toInt(2)
            else -> bitsFun().let { bits ->
                filter { it.toInt(2).xor(bits).and(1 shl bitNum) == 0 }
                    .foldByBits(bitsFun, bitNum - 1)
            }
        }


    val Input.width get() = get(0).length

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
}
