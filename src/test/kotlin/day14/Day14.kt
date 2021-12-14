package day14

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day14 {
    data class Input(
        val template: String,
        val rules: List<String>,
        val cache: MutableMap<String, Map<Char, Long>> = mutableMapOf()
    )

    val sample = parse("sample.txt")
    val data = parse("data.txt")

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


    fun Input.deltaCommon(numSteps: Int): Long {
        val prevalence = countPrevalence(numSteps).values.sortedDescending()
        return with(prevalence) { first() - last() }
    }

    fun Input.countPrevalence(numSteps: Int): Map<Char, Long> {
        return template.windowed(2)
            .flatMap { pair -> recursePair(pair, numSteps).toList() }
            .plus(template.map { it to 1L })
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sum() }
    }


    fun Input.recursePair(pair: String, numSteps: Int): Map<Char, Long> {
        val cacheKey = "$pair|$numSteps"
        cache[cacheKey]?.let { return it }
        if (numSteps == 0) return emptyMap()
        val result = (rules.firstOrNull { rule -> pair.first() == rule.first() && pair.getOrNull(1) == rule.last() }
            ?.let { rule ->
                val left = recursePair(rule.slice(0..1), numSteps - 1).toList()
                val right = recursePair(rule.slice(1..2), numSteps - 1).toList()
                (left + right + Pair(rule[1], 1L))
                    .groupBy({ it.first }, { it.second })
                    .mapValues { it.value.sum() }
            }
            ?: emptyMap())
        cache[cacheKey] = result
        return result
    }


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
