package day14

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
        countPrevalence(numSteps = numSteps).map { it.second }.sortedDescending().run { first() - last() }


    fun Input.countPrevalence(sequence: String = template, added: String = template, numSteps: Int) =
        sequence.windowed(2)
            .flatMap { prevalenceBetween(it, numSteps) }
            .plus(added.map { it to 1L })
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }
            .toList()

    fun Input.prevalenceBetween(pair: String, n: Int): Prevalence = cache("$pair$n") {
        rules.takeIf { n > 0 }
            ?.firstOrNull { rule -> "${rule.first()}${rule.last()}" == pair }
            ?.let { rule -> countPrevalence(rule, rule.slice(1..1), n - 1) }
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
                rules.map { "${it[0]}${it.last()}${it[1]}" }
            )
        }
}

fun main() = Day14().run {
    part1()
    part2()
}
