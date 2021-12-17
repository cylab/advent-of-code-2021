package day16

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.Int.Companion.MAX_VALUE as ALL

typealias Input = String

typealias BIter = Iterator<Char>

class Day16 {

    data class Packet(
        val version: Int,
        val typeId: Int,
        val literal: Long? = null,
        val subPackets: List<Packet> = emptyList()
    )

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        sample.iterator().sumVersions() shouldBe 31
        println("Day  16, Part 1: ${data.iterator().sumVersions()} version sum")
    }

    @Test
    fun part2() {
        listOf(
            "C200B40A82" to 3, // sum
            "04005AC33890" to 54, // mul
            "880086C3E88112" to 7, // min
            "CE00C43D881120" to 9, // max
            "D8005AC2A8F0" to 1, // less than
            "F600BC2D8F" to 0, // greater than
            "9C005AC2F8F0" to 0, // equal to
            "9C0141080250320F1802104A08" to 1, // sum equal to sum
        ).forEach { (input, expected) ->
            input.asClue {
                println(input)
                input.hexToBits().iterator().nextPacket().eval(debug = true) shouldBe expected
            }
        }
        sample.iterator().evalOperations() shouldBe 54
        println("Day  16, Part 2: ${data.iterator().evalOperations()} evaluation result")
    }


    fun BIter.sumVersions() = nextPacket().plusSubPackets().sumOf { it.version }

    fun BIter.evalOperations() = nextPacket().eval()


    fun BIter.nextPacket(): Packet {
        val version = nextValue(3)
        return when (val typeId = nextValue(3)) {
            4 -> Packet(version, typeId, literal = nextLiteral())
            else -> Packet(version, typeId, subPackets = nextSubPakets())
        }
    }

    fun BIter.nextLiteral(): Long {
        var bits = ""
        do {
            val keepReading = nextValue() == 1
            bits += nextChunk(4)
        } while (keepReading)
        return bits.toLong(2)
    }

    fun BIter.nextSubPakets() = when (nextValue()) {
        1 -> readPackets(nextValue(11))
        else -> nextChunk(nextValue(15)).iterator().readPackets(ALL)
    }

    fun BIter.readPackets(num: Int = 1): List<Packet> {
        val packets = mutableListOf<Packet>()
        for (c in 1..num) {
            packets.add(nextPacket())
            if (!hasNext())
                break
        }
        return packets
    }

    fun BIter.nextChunk(num: Int) = (1..num).map { next() }.joinToString("")
    fun BIter.nextValue(num: Int = 1) = nextChunk(num).toInt(2)

    fun Packet.eval(debug: Boolean = false, indent: String = ""): Long {
        val operands = subPackets.map { it.eval(debug, "$indent  ") }
        val (operation, result) = when (typeId) {
            0 -> "sum" to operands.sum()
            1 -> "mul" to operands.fold(1L) { acc, operand -> acc * operand }
            2 -> "min" to operands.minOf { it }
            3 -> "max" to operands.maxOf { it }
            4 -> "lit" to literal!!
            5 -> "gt" to (if (operands[0] > operands[1]) 1L else 0L)
            6 -> "lt" to (if (operands[0] < operands[1]) 1L else 0L)
            7 -> "eq" to (if (operands[0] == operands[1]) 1L else 0L)
            else -> throw IllegalArgumentException("Unknown typeId $typeId")
        }
        if (debug) {
            println("$indent$operation: $result")
        }
        return result
    }

    fun Packet.plusSubPackets(): List<Packet> = listOf(this) + subPackets.flatMap { it.plusSubPackets() }

    fun String.hexToBits() = trim().flatMap {
        "%4s".format(it.toString().toInt(16).toString(2)).replace(' ', '0').toList()
    }

    fun parse(resource: String) = this.javaClass.getResource(resource).readText().hexToBits()
}

fun main() = Day16().run {
    part1()
    part2()
}
