package day16

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.Int.Companion.MAX_VALUE as ALL_CONTAINED

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
        "38006F45291200".hexToBits().iterator().findPackets()
        sample.iterator().sumVersions() shouldBe 31
        println("Day  16, Part 1: ${data.iterator().sumVersions()} version sum")
    }

    @Test
    fun part2() {
//        sample.versionSum() shouldBe 23
//        println("Day  16, Part 2: ${data.versionSum()} version sum")
    }

    fun BIter.sumVersions() = nextPacket().plusSubPackets().sumOf { it.version }

    private fun BIter.nextPacket(): Packet {
        val version = nextValue(3)
        return when (val typeId = nextValue(3)) {
            4 -> Packet(version, typeId, literal = nextLiteral())
            else -> Packet(version, typeId, subPackets = nextSubPakets())
        }
    }

    private fun BIter.nextSubPakets() = when {
        nextValue() == 0 -> next(nextValue(15)).iterator().findPackets(ALL_CONTAINED)
        else -> findPackets(nextValue(11))
    }

    fun BIter.findPackets(num: Int = 1): List<Packet> {
        val packets = mutableListOf<Packet>()
        for (c in 1..num) {
            packets.add(nextPacket())
            if (!hasNext())
                break
        }
        return packets
    }

    fun Packet.plusSubPackets(): List<Packet> = listOf(this) + subPackets.flatMap { it.plusSubPackets() }

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

    fun String.hexToBits() = trim().flatMap {
        "%4s".format(it.toString().toInt(16).toString(2)).replace(' ', '0').toList()
    }

    fun parse(resource: String) = this.javaClass.getResource(resource).readText().hexToBits()
}

fun main() = Day16().run {
    part1()
    part2()
}
