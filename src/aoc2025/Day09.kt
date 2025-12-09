package aoc2025

import AdventDay
import Point
import TestCase
import kotlin.math.absoluteValue
import kotlin.math.min


class Day09 : AdventDay<List<Point>, Long, Long>(2025, 9) {
    override fun parseInput(stringInput: List<String>) =
        stringInput.map { s -> s.split(",").map { it.toInt() }.let { (x, y) -> Point(x, y) } }

    private fun List<Point>.largestArea(extraCheck: (Point, Point) -> Boolean): Long {
        var max = 0L
        for (i in 0..<lastIndex) {
            for (j in i + 1..lastIndex) {
                val a = this[i]
                val b = this[j]
                if (extraCheck(a, b)) {
                    max = maxOf(max, ((a.x - b.x).absoluteValue + 1).toLong() * ((a.y - b.y).absoluteValue + 1))
                }
            }
        }
        return max
    }

    override fun part1(input: List<Point>, testArg: Any?) = input.largestArea { _, _ -> true }

    override fun part2(input: List<Point>, testArg: Any?): Long {
        val segments = (input + input.first()).windowed(2)
        return input.largestArea { a, b ->
            val minX = minOf(a.x, b.x)
            val maxX = maxOf(a.x, b.x)
            val minY = minOf(a.y, b.y)
            val maxY = maxOf(a.y, b.y)
            segments.none { (c, d) ->
                if (c.x == d.x) {
                    c.x in minX + 1..<maxX && min(c.y, d.y) < maxY && maxOf(c.y, d.y) > minY
                } else {
                    c.y in minY + 1..<maxY && min(c.x, d.x) < maxX && maxOf(c.x, d.x) > minX
                }
            }
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 50L))
    override fun testCases2() = listOf(TestCase(getTestInput(), 24L))
}

fun main() {
    Day09().main()
}
