package day16

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.Int.Companion.MAX_VALUE

typealias Input = String

typealias Biter = Iterator<Char>

class Day16 {

    data class Packet(
        val version: Int,
        val typeId: Int,
        val literal: Long? = null,
        val packets: List<Packet> = emptyList()
    )

    val sample = parse("sample.txt")
    val data = parse("data.txt")

    @Test
    fun part1() {
        unpack("38006F45291200".hexToBits().iterator())
        sample.iterator().sumVersions() shouldBe 31
        println("Day  16, Part 1: ${data.iterator().sumVersions()} version sum")
    }

    @Test
    fun part2() {
//        sample.versionSum() shouldBe 23
//        println("Day  16, Part 2: ${data.versionSum()} version sum")
    }

    fun Biter.sumVersions() = unpack(this).flatMap { it.flattened() }.sumOf { it.version }

    fun Packet.flattened(): List<Packet> = listOf(this) + packets.flatMap { it.flattened() }

    fun unpack(bits: Biter, num: Int = 1): List<Packet> {
        val packets = mutableListOf<Packet>()
        for (c in 1..num) {
            val version = bits.nextValue(3)
            val typeId = bits.nextValue(3)
            if (typeId == 4) {
                packets.add(Packet(version, typeId, literal = bits.nextLiteral()))
            } else {
                val lengthId = bits.nextValue()
                val length = when (lengthId) {
                    0 -> bits.nextValue(15)
                    else -> bits.nextValue(11)
                }
                val packet = when (lengthId) {
                    0 -> {
                        val subPackets = bits.next(length)
                        Packet(version, typeId, packets = unpack(subPackets.iterator(), MAX_VALUE))
                    }
                    else -> Packet(version, typeId, packets = unpack(bits, length))
                }
                packets.add(packet)
            }
            if (!bits.hasNext())
                break
        }
        return packets
    }

    fun Biter.next(num: Int) = (1..num).map { next() }.joinToString("")
    fun Biter.nextValue(num: Int = 1) = next(num).toInt(2)
    fun Biter.nextLiteral(): Long {
        var bits = ""
        do {
            val keepReading = next() == '1'
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
