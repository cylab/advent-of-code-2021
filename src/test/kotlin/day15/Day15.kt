package day15

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.Integer.signum
import java.util.PriorityQueue
import kotlin.math.abs

// poor mans point type
typealias Point = Pair<Int, Int>

val Point.x get() = first
val Point.y get() = second
operator fun Point.plus(other: Point) = x + other.x to y + other.y

class Day15 {

    data class Input(val risks: List<List<Int>>, val xSize: Int, val ySize: Int, val tiles: Int = 1)

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    val NWSE = listOf(0 to -1, -1 to 0, 0 to 1, 1 to 0)

    @Test
    fun part1() {
        sample.debug()
        sample.totalRisk() shouldBe 40
        println("Day  15, Part 1: ${data.totalRisk()} risk")
    }

    @Test
    fun part2() {
        sample.copy(tiles = 5).debug()
        sample.copy(tiles = 5).totalRisk() shouldBe 315
        println("Day  15, Part 2: ${data.copy(tiles = 5).totalRisk()} risk")
    }

    fun Input.totalRisk() = findPathByAStar(points().first(), points().last()).sumOf { risk(it) }

    fun Input.points() = (0 until ySize * tiles).asSequence()
        .flatMap { y -> (0 until xSize * tiles).asSequence().map { x -> x to y } }

    fun Input.risk(p: Point) = when {
        p.x in 0 until xSize * tiles && p.y in 0 until ySize * tiles -> (risks[p.y % ySize][p.x % xSize] + (p.y / ySize) + (p.x / xSize) - 1) % 9 + 1
        else -> 10
    }

    // adapted from https://www.redblobgames.com/pathfinding/a-star/introduction.html
    fun Input.findPathByAStar(start: Point, goal: Point): List<Point> {
        val frontier = PriorityQueue<Pair<Int, Point>> { (prioA), (prioB) -> signum(prioA - prioB) }
        frontier.add(0 to start)
        val cameFrom = mutableMapOf(start to Point(-1, -1))
        val costSoFar = mutableMapOf(start to 0)
        while (frontier.isNotEmpty()) {
            val current = frontier.remove().second
            if (current == goal) {
                break
            }
            NWSE.map { current + it }
                .forEach { next ->
                    val newCost = costSoFar[current]!! + risk(next)
                    if (next !in costSoFar || newCost < costSoFar[next]!!) {
                        costSoFar[next] = newCost
                        // cost + Manhattan distance on a square grid
                        val priority = newCost + (abs(goal.x - next.x) + abs(goal.y - next.y))
                        frontier.add(priority to next)
                        cameFrom[next] = current
                    }
                }
        }
        // reconstruct path
        val path = mutableListOf<Point>()
        var visited = goal
        while (visited != start) {
            path.add(visited)
            visited = cameFrom[visited]!!
        }
        return path.reversed()
    }

    fun Input.debug() {
        val start = points().first()
        val goal = points().last()
        val path = findPathByAStar(start, goal)
        points().forEach { p ->
            print(
                when (p) {
                    start -> "*${risk(p)}*"
                    in path -> "(${risk(p)})"
                    else -> " ${risk(p)} "
                }
            )
            if (p.x % xSize == xSize - 1) print(" ")
            if (p.x == xSize * tiles - 1) println()
            if (p.y % ySize == ySize - 1 && p.x == xSize * tiles - 1) println()
        }
    }

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .lines()
        .filter { it.isNotBlank() }
        .map { line -> line.map { it.digitToInt() } }
        .let { Input(it, it.first().size, it.size) }
}

fun main() = Day15().run {
    part1()
    part2()
}
