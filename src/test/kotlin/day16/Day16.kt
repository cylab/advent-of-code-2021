package day16

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.Int.Companion.MAX_VALUE

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
        "38006F45291200".hexToBits().iterator().unpack()
        sample.iterator().sumVersions() shouldBe 31
        println("Day  16, Part 1: ${data.iterator().sumVersions()} version sum")
    }

    @Test
    fun part2() {
//        sample.versionSum() shouldBe 23
//        println("Day  16, Part 2: ${data.versionSum()} version sum")
    }

    fun BIter.sumVersions() = unpack().first().withSubPackets().sumOf { it.version }

    fun BIter.unpack(num: Int = 1): List<Packet> {
        val packets = mutableListOf<Packet>()
        for (c in 1..num) {
            val version = nextValue(3)
            val typeId = nextValue(3)
            if (typeId == 4) {
                packets.add(Packet(version, typeId, literal = nextLiteral()))
            } else {
                val lengthId = nextValue()
                val subPackets = when (lengthId) {
                    0 -> next(nextValue(15)).iterator().unpack(MAX_VALUE)
                    else -> unpack(nextValue(11))
                }
                packets.add(Packet(version, typeId, subPackets = subPackets))
            }
            if (!hasNext())
                break
        }
        return packets
    }

    fun BIter.next(num: Int) = (1..num).map { next() }.joinToString("")
    fun BIter.nextValue(num: Int = 1) = next(num).toInt(2)
    fun BIter.nextLiteral(): Long {
        var bits = ""
        do {
            val keepReading = nextValue() == 1
            bits += next(4)
        } while (keepReading)
        return bits.toLong(2)
    }

    fun Packet.withSubPackets(): List<Packet> = listOf(this) + subPackets.flatMap { it.withSubPackets() }

    fun String.hexToBits() = trim().flatMap {
        "%4s".format(it.toString().toInt(16).toString(2)).replace(' ', '0').toList()
    }

    fun parse(resource: String) = this.javaClass.getResource(resource).readText().hexToBits()
}

fun main() = Day16().run {
    part1()
    part2()
}
