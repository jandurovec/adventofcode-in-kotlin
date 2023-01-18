package aoc2016

import TestCase
import UnparsedDay

class Day04 : UnparsedDay<Int, Int>(2016, 4) {

    class Room(def: String) {
        val name: String
        val sectorId: Int
        private val checksum: String
        init {
            val (n,s,c) = """([a-z-]+)-([0-9]+)\[([a-z]*)\]""".toRegex().matchEntire(def)!!.destructured
            name = n
            sectorId = s.toInt()
            checksum = c
        }

        fun isReal() = name.filter { it != '-' }.groupingBy { it }
            .eachCount().entries.sortedWith { (c1, count1), (c2, count2) -> (count2 - count1).let { if (it == 0) c1 - c2 else it } }
            .take(5).map { it.key }.joinToString("") == checksum

        fun decryptedName(): String {
            val key = sectorId
            return name.map {
                when (it) {
                    '-' -> ' '
                    else -> 'a' + ((it - 'a' + key) % ('z' - 'a' + 1))
                }
            }.joinToString("")
        }
    }

    override fun part1(input: List<String>, testArg: Any?) =
        input.map { Room(it) }.filter { it.isReal() }.sumOf { it.sectorId }

    override fun part2(input: List<String>, testArg: Any?) =
        input.map { Room(it) }.single { it.decryptedName().contains(if (testArg is String) testArg else "north") }.sectorId

    override fun testCases1() = listOf(
        TestCase(
            listOf(
                "aaaaa-bbb-z-y-x-123[abxyz]",
                "a-b-c-d-e-f-g-h-987[abcde]",
                "not-a-real-room-404[oarel]",
                "totally-real-room-200[decoy]"
            ), 1514
        )
    )

    override fun testCases2() =
        listOf(TestCase(listOf("qzmt-zixmtkozy-ivhz-343[]"), 343, "very encrypted"))
}

fun main() {
    Day04().main()
}
