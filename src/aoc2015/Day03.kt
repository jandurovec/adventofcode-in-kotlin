package aoc2015

import Point
import SingleStringDay
import TestCase

class Day03 : SingleStringDay<Int, Int>(2015, 3) {
    private fun MutableSet<Point>.addStops(path: List<Char>): MutableSet<Point> {
        path.runningFold(Point(0, 0)) { prev, d ->
            Point(
                prev.x + when (d) {
                    '<' -> -1
                    '>' -> 1
                    else -> 0
                }, prev.y + when (d) {
                    '^' -> -1
                    'v' -> 1
                    else -> 0
                }
            )
        }.forEach { add(it) }
        return this
    }

    override fun part1(input: String, testArg: Any?) = mutableSetOf<Point>().addStops(input.toList()).size

    override fun part2(input: String, testArg: Any?) = mutableSetOf<Point>().let { dst ->
        val chunks = input.chunked(2)
        dst.addStops(chunks.map { it[0] }).addStops(chunks.map { it[1] })
    }.size

    override fun testCases1() = listOf(TestCase("^>v<", 4), TestCase("^v^v^v^v^v", 2))
    override fun testCases2() = listOf(TestCase("^>v<", 3), TestCase("^v^v^v^v^v", 11))
}

fun main() {
    Day03().main()
}
