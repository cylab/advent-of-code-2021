package day2

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Input = List<Pair<String, Int>>

class Day2 {
    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.combinedPosAndDepth() shouldBe 150
        println("Day  2, Puzzle 1: ${input.combinedPosAndDepth()} combined steering")
    }

    @Test
    fun puzzle2() {
        sample.combinedPosAndAim() shouldBe 900
        println("Day  2, Puzzle 2: ${input.combinedPosAndAim()} combined steering")
    }


    fun Input.combinedPosAndDepth() = this
        .fold(listOf(0, 0)) { (pos, depth), (dir, amount) ->
            when (dir) {
                "forward" -> listOf(pos + amount, depth)
                "down" -> listOf(pos, depth + amount)
                "up" -> listOf(pos, depth - amount)
                else -> listOf(pos, depth) // ignore unknown directions
            }
        }
        .let { (pos, depth) -> pos * depth }


    fun Input.combinedPosAndAim() = this
        .fold(listOf(0, 0, 0)) { (pos, depth, aim), (dir, amount) ->
            when (dir) {
                "forward" -> listOf(pos + amount, depth + (aim * amount), aim)
                "down" -> listOf(pos, depth, aim + amount)
                "up" -> listOf(pos, depth, aim - amount)
                else -> listOf(pos, depth, aim) // ignore unknown directions
            }
        }
        .let { (pos, depth) -> pos * depth }


    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.split(" ") }
        .map { (dir, amount) -> dir to amount.toInt() }

}
