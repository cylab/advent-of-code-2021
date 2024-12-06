package aoc2024.day5

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Rule = Pair<Int, Int>
typealias Update = List<Int>
typealias Updates = List<Update>
typealias Input = Pair<Rules, Updates>

val Input.rules: Rules get() = first
val Input.updates: Updates get() = second

class Rules(given: List<Rule>) : List<Rule> by given {
    val pageOrder = groupBy({ it.first }, { it.second })
}

class Day5 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.validUpdatesSum() shouldBe 143
        println("Day  5, Part 1: valid updates middle sum is ${data.validUpdatesSum()}")
    }

    @Test
    fun part2() {
        sample.fixedUpdatesSum() shouldBe 123
        println("Day  5, Part 2: valid updates middle sum is ${data.fixedUpdatesSum()}")
    }


    fun Input.validUpdatesSum() = updates.filter { it.check(rules) }
            .sumOf { it.middle() }

    fun Input.fixedUpdatesSum() = updates.filterNot { it.check(rules) }
            .map { rules.sortUpdate(it) }
            .sumOf { it.middle() }


    fun Update.check(rules: Rules) = rules
        .map { (left, right) -> indexOf(left) to indexOf(right) }
        .filter { (left, right) -> left != -1 && right != -1 }
        .all { (left, right) -> left < right }

    fun Update.middle() = get(size / 2)


    fun Rules.sortUpdate(update: Update) = update.sortedWith { page1, page2 ->
        when {
            pageOrder[page1] == null -> 0
            page2 in pageOrder[page1]!! -> -1
            else -> 1
        }
    }


    fun parse(resource: String): Input = javaClass.getResource(resource)!!
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .partition { it.contains('|') }
        .let { (rules, updates) -> Pair(rules.toRules(), updates.toUpdates()) }

    fun List<String>.toRules() =
        Rules(map { line -> line.split('|').let { (left, right) -> Pair(left.toInt(), right.toInt()) } })

    fun List<String>.toUpdates() = map { line -> line.split(',').map { it.toInt() } }
}

fun main() = Day5().run {
    part1()
    part2()
}
