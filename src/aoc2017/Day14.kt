package aoc2017

import Point
import SingleStringDay
import TestCase

class Day14 : SingleStringDay<Int, Int>(2017, 14) {

    private fun String.knotHash() = Day10().part2(this)

    override fun part1(input: String, testArg: Any?) = (0..127).sumOf {
        "$input-$it".knotHash().chunked(8).sumOf { i -> i.toUInt(16).countOneBits() }
    }

    override fun part2(input: String, testArg: Any?): Int {
        val bits = (0..127).flatMap { row ->
            "$input-$row".knotHash().chunked(8).flatMap { i ->
                i.toUInt(16).toString(2)
                    .padStart(32, '0').map { it == '1' }
            }.mapIndexedNotNull { col, bit -> if (bit) Point(row, col) else null }
        }.toMutableSet()

        var components = 0
        while (bits.isNotEmpty()) {
            components++
            val toExplore = ArrayDeque<Point>()
            toExplore.addLast(bits.first())
            bits.remove(toExplore.last())
            while (toExplore.isNotEmpty()) {
                val cur = toExplore.removeFirst()
                cur.neighbors(false).forEach { neighbor ->
                    if (bits.remove(neighbor)) {
                        toExplore.addLast(neighbor)
                    }
                }
            }
        }
        return components
    }

    override fun testCases1() = listOf(TestCase("flqrgnkx", 8108))
    override fun testCases2() = listOf(TestCase("flqrgnkx", 1242))
}

fun main() {
    Day14().main()
}
