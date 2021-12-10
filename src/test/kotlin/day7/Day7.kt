package day7

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.signum
import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.abs

class Day7 {

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.fuel() shouldBe 37
        sample.fuel_mutable() shouldBe 37
        println("Day  6, Puzzle 1: ${input.fuel()} fuel")
    }

    @Test
    fun puzzle2() {
        sample.fuel { it.gauss() } shouldBe 168
        sample.fuel_mutable { it.gauss() } shouldBe 168
        println("Day  6, Puzzle 2: ${input.fuel { it.gauss() }} fuel")
    }

    fun Int.gauss() = (this * (this + 1)) / 2

    // searching through the positions starting at the mean average in the direction
    // where the fuel consumption decreases, until a minimum is found ( the consumption increases again)
    // this assumes all cost functions produce only one local minimum in the position set
    fun List<Int>.fuel(costFun: (Int) -> Int = { it }): Int {
        val mean = sum() / size
        val fuelN0 = sumOf { costFun(abs(mean - it)) }
        val fuelN1 = sumOf { costFun(abs(mean + 1 - it)) }
        val dir = signum(fuelN0 - fuelN1)

        return (1..MAX_VALUE).asSequence()
            .map { n -> sumOf { costFun(abs(mean + n * dir - it)) } }
            .windowed(2)
            .first { (current, next) -> current < next }[0]
    }

    // mutable variant to show what is actually happening
    fun List<Int>.fuel_mutable(costFun: (Int) -> Int = { it }): Int {
        val mean = sum() / size
        val fuelN0 = sumOf { costFun(abs(mean - it)) }
        val fuelN1 = sumOf { costFun(abs(mean + 1 - it)) }
        val dir = signum(fuelN0 - fuelN1)

        var current = fuelN0
        for (n in 1..MAX_VALUE) {
            val next = sumOf { costFun(abs(mean + n * dir - it)) }
            when {
                current < next -> break
                else -> current = next
            }
        }
        return current
    }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .trim()
        .split(Regex("\\D+"))
        .map { it.toInt() }
}
