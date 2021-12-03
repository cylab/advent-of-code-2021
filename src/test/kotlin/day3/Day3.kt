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

    fun Input.oxygenAndCO2() = Pair(findByBits({ gamma() }), findByBits({ epsilon() }))


    fun Input.gamma() =
        fold(List(width) { 0f }) { acc, line -> acc.mapIndexed { i, it -> it + line.slice(i..i).toInt() } }
            .foldIndexed(0) { i, acc, it -> acc + (it / size).roundToInt().shl(width - i - 1) }

    fun Input.epsilon(gamma: Int = gamma()) = gamma.xor((1 shl width) - 1)


    tailrec fun Input.findByBits(bitsFun: Input.() -> Int, pos: Int = width - 1): Int {
        val bits = bitsFun()
        val res = filter { it.toInt(2).xor(bits).and(1 shl pos) == 0 }
        return if (res.size == 1) res.first().toInt(2) else res.findByBits(bitsFun, pos - 1)
    }

    val Input.width get() = first().length

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
}
