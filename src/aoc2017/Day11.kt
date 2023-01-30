package aoc2017

import AdventDay
import TestCase
import kotlin.math.absoluteValue

class Day11 : AdventDay<List<String>, Int, Int>(2017, 11) {

    /**
     * Hex grid with cube coordinates implemented as per https://www.redblobgames.com/grids/hexagons/
     * <pre>
     *        +--+
     *       /  0 \
     *   +--+      +--+
     *  / -1 \+1-1/ +1 \
     * +      +--+      +
     *  \+1 0/ q  \0 -1/
     *   *--+      +--+
     *  / -1 \s  r/ +1 \
     * +      +--+      +
     *  \0 +1/  0 \-1 0/
     *   +--+      +--+
     *       \-1+1/
     *        +--+
     * </pre>
     */
    data class Hex(private val q: Int = 0, private val r: Int = 0, private val s: Int = 0) {
        val north: Hex; get() = Hex(q, r - 1, s + 1)
        val northEast: Hex; get() = Hex(q + 1, r - 1, s)
        val southEast: Hex; get() = Hex(q + 1, r, s - 1)
        val south: Hex; get() = Hex(q, r + 1, s - 1)
        val southWest: Hex; get() = Hex(q - 1, r + 1, s)
        val northWest: Hex; get() = Hex(q - 1, r, s + 1)

        fun dist(other: Hex) =
            ((q - other.q).absoluteValue + (r - other.r).absoluteValue + (s - other.s).absoluteValue) / 2
    }

    override fun parseInput(stringInput: List<String>) = stringInput.first().split(',')

    private fun List<String>.hexSteps(start: Hex) = sequence {
        var pos = start
        forEach {
            pos = when (it) {
                "n" -> pos.north
                "ne" -> pos.northEast
                "se" -> pos.southEast
                "s" -> pos.south
                "sw" -> pos.southWest
                "nw" -> pos.northWest
                else -> error("Unknown direction $it")
            }
            yield(pos)
        }
    }

    override fun part1(input: List<String>, testArg: Any?): Int {
        val start = Hex()
        return start.dist(input.hexSteps(start).last())
    }

    override fun part2(input: List<String>, testArg: Any?): Int {
        val start = Hex()
        return input.hexSteps(start).maxOf { start.dist(it) }
    }

    override fun testCases1() = listOf(
        TestCase(listOf("ne", "ne", "ne"), 3),
        TestCase(listOf("ne", "ne", "sw", "sw"), 0),
        TestCase(listOf("ne", "ne", "s", "s"), 2),
        TestCase(listOf("se", "sw", "se", "sw", "sw"), 3)
    )
}

fun main() {
    Day11().main()
}
