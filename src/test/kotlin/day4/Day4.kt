package day4

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day4 {

    data class Input(val numbers: List<Int>, val boards: List<Board>)
    data class Board(
        val rows: List<List<Int>>,
        val cols: List<List<Int>> = rows.first().indices.map { i -> rows.map { it[i] } }
    )

    data class Win(val board: Board, val drawn: List<Int> = emptyList())

    val sample = parse("sample.txt")
    val input = parse("input.txt")

    @Test
    fun puzzle1() {
        sample.wins().first().score() shouldBe 4512
        println("Day  4, Puzzle 1: ${input.wins().first().score()} score")
    }

    @Test
    fun puzzle2() {
        sample.wins().last().score() shouldBe 1924
        println("Day  4, Puzzle 2: ${input.wins().last().score()} score")
    }

    fun Input.wins() = boards.mapNotNull { it.winOrNull(numbers) }.sortedBy { it.drawn.size }

    fun Win.score() = board.rows.flatten().filterNot { it in drawn }.sum().let { it * drawn.last() }


    fun Board.winOrNull(numbers: List<Int>) = numbers.indices.asSequence()
        .map { numbers.slice(0..it) }
        .firstOrNull { drawn -> rows.any { drawn.containsAll(it) } || cols.any { drawn.containsAll(it) } }
        ?.let { Win(this, it) }


    fun List<String>.createInput() = Input(first().extractInts(), drop(1).map { it.createBoard() })

    fun String.extractInts() = trim().split(Regex("[,\\s]\\s*")).map { it.trim().toInt() }

    fun String.createBoard() = Board(lines().map { it.extractInts() })

    fun parse(resource: String) = this.javaClass
        .getResource(resource)
        .readText()
        .trim()
        .split(Regex("(?m)[\n\r]*^\\s*$[\n\r]+"))
        .createInput()
}
