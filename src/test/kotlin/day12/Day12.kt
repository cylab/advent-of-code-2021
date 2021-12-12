package day12

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
        sample.findPaths(allowedTwice = 0).count() shouldBe 226
        println("Day  2, Part 1: ${data.findPaths(allowedTwice = 0).count()} paths")
    }

    @Test
    fun part2() {
        sample.findPaths(allowedTwice = 1).count() shouldBe 3509
        println("Day  2, Part 2: ${data.findPaths(allowedTwice = 1).count()} paths")
    }

    fun Graph.findPaths(name: String = "start", path: Path = listOf(), allowedTwice: Int = 0): List<Path> {
        val visitedTwice = path.filter { it.isSmall() }
            .groupingBy { it }.eachCount().filter { it.value >= 2 }

        val noRevisit = name == "start" ||
            name.isSmall() && visitedTwice.count() == allowedTwice

        return when {
            name == "end" -> listOf(path + name)
            noRevisit && path.contains(name) -> emptyList()
            else -> get(name)!!.value.flatMap { findPaths(it, path + name, allowedTwice) }
        }
    }

    fun String.isSmall() = this[0].isLowerCase()

    fun parse(resource: String) = this.javaClass.getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { it.trim().split(Regex("\\W+")) }
        .flatMap { listOf(it, it.reversed()) } // duplicate outgoing edges as incoming
        .groupBy({ it[0] }, { it[1] })
        .mapValues { it }

}

fun main() = Day12().run {
    part1()
    part2()
}
