package aoc2023

import AdventDay
import Point
import TestCase


class Day11 : AdventDay<List<Point>, Long, Long>(2023, 11) {
    override fun parseInput(stringInput: List<String>) = stringInput.flatMapIndexed { y, line ->
        line.mapIndexedNotNull { x, char -> if (char == '#') Point(x, y) else null }
    }

    private fun solve(input: List<Point>, emptySize: Int): Long {
        val galaxyX = input.map { it.x }.toSet()
        val galaxyY = input.map { it.y }.toSet()

        return sequence {
            for (i in 0..<input.lastIndex) {
                for (j in i + 1..input.lastIndex) {
                    yield(input[i] to input[j])
                }
            }
        }.sumOf { (start, end) ->
            val xRange = minOf(start.x, end.x)..maxOf(start.x, end.x)
            val yRange = minOf(start.y, end.y)..maxOf(start.y, end.y)
            (xRange.last - xRange.first) + (yRange.last - yRange.first) + (emptySize - 1).toLong() * (xRange.count { it !in galaxyX } + yRange.count { it !in galaxyY })
        }
    }

    override fun part1(input: List<Point>, testArg: Any?) = solve(input, 2)

    override fun part2(input: List<Point>, testArg: Any?) =
        solve(input, if (testArg != null) testArg as Int else 1000000)

    override fun testCases1() = listOf(TestCase(getTestInput(), 374L))

    override fun testCases2() = listOf(
        TestCase(getTestInput(), 1030L, 10),
        TestCase(getTestInput(), 8410L, 100),
    )
}

fun main() {
    Day11().main()
}
