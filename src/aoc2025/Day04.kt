package aoc2025

import AdventDay
import Point
import TestCase

class Day04 : AdventDay<Set<Point>, Int, Int>(2025, 4) {
    override fun parseInput(stringInput: List<String>) = buildSet {
        stringInput.forEachIndexed { y, line ->
            line.forEachIndexed { x, ch ->
                if (ch == '@') {
                    add(Point(x, y))
                }
            }
        }
    }

    override fun part1(input: Set<Point>, testArg: Any?): Int {
        return input.count { pos -> pos.neighbors().count { input.contains(it) } < 4 }
    }

    override fun part2(input: Set<Point>, testArg: Any?): Int {
        var somethingRemoved = true
        val data = input.toMutableSet()
        while (somethingRemoved) {
            val toRemove = mutableSetOf<Point>()
            data.forEach { pos ->
                if (pos.neighbors().count { data.contains(it) } < 4) {
                    toRemove.add(pos)
                }
            }
            somethingRemoved = toRemove.isNotEmpty()
            data.removeAll(toRemove)
        }
        return input.size - data.size
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 13))
    override fun testCases2() = listOf(TestCase(getTestInput(), 43))
}

fun main() {
    Day04().main()
}
