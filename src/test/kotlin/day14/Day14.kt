package day14

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Rule = List<Char>
typealias Prevalence = Map<Char, Long>

class Day14 {
    data class Input(val template: String, val rules: List<Rule>)

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
        countPrevalence(numSteps).values.sortedDescending().run { first() - last() }


    fun Input.countPrevalence(numSteps: Int) = template.windowed(2)
        .flatMap { pair -> prevalenceBetween(pair, numSteps).toList() }
        .plus(template.map { it to 1L })
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.sum() }

    fun Input.prevalenceBetween(pair: String, n: Int): Prevalence = cache("$pair$n") {
        rules.takeIf { n > 0 }
            ?.firstOrNull { (left, right) -> "$left$right" == pair }
            ?.let { (left, right, insert) ->
                val pLeft = prevalenceBetween("$left$insert", n - 1)
                val pRight = prevalenceBetween("$insert$right", n - 1)
                (pLeft.toList() + pRight.toList() + (insert to 1L))
                    .groupBy({ it.first }, { it.second })
                    .mapValues { it.value.sum() }
            }
            ?: emptyMap()
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
                rules.map { it.replace(Regex("\\W+"), "").toList() }
            )
        }
}

fun main() = Day14().run {
    part1()
    part2()
}
