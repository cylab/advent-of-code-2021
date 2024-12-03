package aoc2024.day3

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day3 {
    val sample1 = parse("sample1.txt")
    val sample2 = parse("sample2.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample1.sumOfMuls() shouldBe 161
        println("Day  3, Part 1: sum of multiplications is ${data.sumOfMuls()}")
    }

    @Test
    fun part2() {
        sample2.sumOfConditionalMuls() shouldBe 48
        println("Day  3, Part 2: sum of do multiplications is ${data.sumOfConditionalMuls()}")
    }

    fun String.sumOfMuls() = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        .findAll(this)
        .map { it.groupValues.drop(1) }
        .map { (left, right) -> left.toInt() * right.toInt() }
        .sum()

    // split the string with a lookahead and lookbehind to keep the delimiters for further processing
    // so we have consecutive pairs of "do()" and command-strings as well as "don't()" and commands-strings
    // which we can group to select only the commands-strings in the do()-group and pass the combined string
    // to the sumOfMuls function
    val delimiter = Regex("""(?<=do\(\))|(?<=don't\(\))|(?<!^)(?=do\(\))|(?=don't\(\))""")
    fun String.sumOfConditionalMuls() = prefixedWithDo()
        .split(delimiter)
        .windowed(2, 2)
        .groupBy { it.first() }
        .filter { it.key == "do()" }
        .flatMap { it.value }
        .joinToString()
        .sumOfMuls()

    // helper to make sure the string starts with "do()" to avoid missing the first commands
    fun String.prefixedWithDo() = if (startsWith("do()")) this else "do()$this"

    fun parse(resource: String): String = javaClass.getResource(resource)!!
        .readText()
        .lines()
        .joinToString()

}

fun main() = Day3().run {
    part1()
    part2()
}
