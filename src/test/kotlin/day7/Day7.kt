package day7

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Math.abs

class Day7 {

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.fuel() shouldBe 37
        println("Day  6, Puzzle 1: ${input.fuel()} fuel")
    }

    @Test
    fun puzzle2() {
        sample.fuel { it.gauss() } shouldBe 168
        println("Day  6, Puzzle 2: ${input.fuel { it.gauss() }} fuel")
    }

    fun Int.gauss() = (this * (this + 1)) / 2

    fun List<Int>.fuel(costFun: (Int) -> Int = { it }): Int {
        val mean = sum() / size
        val fuelStart = map { costFun(abs(it - mean)) }.sum()
        val fuelAtLeft = map { costFun(abs(it - (mean - 1))) }.sum()
        val fuelAtRight = map { costFun(abs(it - (mean + 1))) }.sum()
        val dir = when {
            fuelStart < fuelAtLeft && fuelStart < fuelAtRight -> return fuelStart
            (fuelAtLeft - fuelStart) < (fuelAtRight - fuelStart) -> -1
            else -> 1

        }
        var currentFuel = fuelStart
        var distance = 1
        while (true) {
            val nextFuel = map { costFun(abs(it - (mean + dir * distance))) }.sum()
            if (nextFuel > currentFuel) {
                println("pos: ${mean + dir * (distance - 1)}")
                return currentFuel
            }
            currentFuel = nextFuel
            distance++
        }
    }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .trim()
        .split(Regex("\\D+"))
        .map { it.toInt() }
}
