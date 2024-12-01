package aoc2021.day12

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

typealias Node = Map.Entry<String, List<String>>
typealias Graph = Map<String, Node>
typealias Path = List<String>

class Day12 {
    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.findPaths(smallTwice = 0).count() shouldBe 226
        println("Day  2, Part 1: ${data.findPaths(smallTwice = 0).count()} paths")
    }

    @Test
    fun part2() {
        sample.findPaths(smallTwice = 1).count() shouldBe 3509
        println("Day  2, Part 2: ${data.findPaths(smallTwice = 1).count()} paths")
    }


    fun Graph.findPaths(name: String = "start", path: Path = listOf(), smallTwice: Int): List<Path> = when {
        name == "end" -> listOf(path + name)
        path.noRevisit(name, smallTwice) -> emptyList()
        else -> targets(name).flatMap {
            findPaths(it, path + name, smallTwice)
        }
    }

    fun Path.noRevisit(name: String, smallTwice: Int) = contains(name) && name.isSmall() &&
        groupingBy { it }.eachCount().count { it.key.isSmall() && it.value >= 2 } == smallTwice


    fun Graph.targets(name: String) = get(name)!!.value

    fun String.isSmall() = this[0].isLowerCase()


    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.trim().split(Regex("\\W+")) }
        .flatMap { listOf(it, it.reversed()) } // duplicate outgoing edges as incoming
        .filterNot { it[1] == "start" } // we don't care about paths that lead to "start"
        .groupBy({ it[0] }, { it[1] })
        .mapValues { it }

}

fun main() = Day12().run {
    part1()
    part2()
}
