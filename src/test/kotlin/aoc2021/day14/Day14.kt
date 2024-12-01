package aoc2021.day14

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Prevalence = List<Pair<Char, Long>>

class Day14 {
    data class Input(val template: String, val rules: List<String>)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    val cache: MutableMap<String, Prevalence> = mutableMapOf()

    @Test
    fun part1() {
        sample.deltaCommon(10) shouldBe 1588
        println("Day 14, Part 1: ${data.deltaCommon(10)} delta common")
    }

    @Test
    fun part2() {
        sample.deltaCommon(40) shouldBe 2188189693529L
        println("Day 14, Part 2: ${data.deltaCommon(40)} delta common")
    }


    fun Input.deltaCommon(numSteps: Int) =
        countPrevalence(numSteps).map { it.second }.sortedDescending().run { first() - last() }


    fun Input.countPrevalence(n: Int, sequence: String = template, added: String = template) =
        sequence.windowed(2)
            .flatMap { pair -> prevalenceBetween(n, pair) }
            .plus(added.map { it to 1L })
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }
            .toList()

    fun Input.prevalenceBetween(n: Int, pair: String): Prevalence = cache("$pair$n") {
        rules.takeIf { n > 0 }
            ?.firstOrNull { rule -> "${rule.first()}${rule.last()}" == pair }
            ?.let { rule -> countPrevalence(n - 1, rule, rule.slice(1..1)) }
            ?: emptyList()
    }

    fun cache(key: String, supplier: () -> Prevalence) = cache[key] ?: supplier().also { cache[key] = it }

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .partition { !it.contains("->") }
        .let { (templates, rules) ->
            Input(
                templates[0],
                // "inline" the inserted symbol, so we can treat a rule like a template during recursive evaluation
                rules.map { "${it[0]}${it.last()}${it[1]}" }
            )
        }
}

fun main() = Day14().run {
    part1()
    part2()
}
