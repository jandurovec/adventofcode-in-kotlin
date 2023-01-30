package aoc2017

import AdventDay
import TestCase

class Day13 : AdventDay<Map<Int, Int>, Int, Int>(2017, 13) {
    override fun parseInput(stringInput: List<String>) = buildMap {
        stringInput.forEach { line ->
            val (depth, range) = line.split(": ").map { it.toInt() }
            this[depth] = range
        }
    }

    private fun Map<Int, Int>.detections(entryTime: Int) = asSequence().mapNotNull { (depth, range) ->
        if ((entryTime + depth).mod(2 * range - 2) == 0) depth else null
    }

    override fun part1(input: Map<Int, Int>, testArg: Any?) =
        input.detections(0).sumOf { it * input[it]!! }

    override fun part2(input: Map<Int, Int>, testArg: Any?) =
        generateSequence(0) { it + 1 }.first {
            input.detections(it).none()
        }

    override fun testCases1() = listOf(TestCase(getTestInput(), 24))
    override fun testCases2() = listOf(TestCase(getTestInput(), 10))
}

fun main() {
    Day13().main()
}
