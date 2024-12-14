package aoc2024

import Point
import TestCase
import UnparsedDay


class Day08 : UnparsedDay<Int, Int>(2024, 8) {

    private fun countAntinodes(input: List<String>, indexFilter: (Int) -> Boolean): Int {
        val maxX = input[0].lastIndex
        val maxY = input.lastIndex
        val antennas = buildMap<Char, MutableList<Point>> {
            input.forEachIndexed { row, line ->
                line.forEachIndexed { col, c ->
                    if (c != '.') {
                        val list = getOrPut(c) { mutableListOf() }
                        list.add(Point(col, row))
                    }
                }
            }
        }.values
        return buildSet {
            antennas.forEach { points ->
                for (i in 0..<points.lastIndex) {
                    for (j in i + 1..points.lastIndex) {
                        val p1 = points[i]
                        val p2 = points[j]
                        listOf(p1 to p1 - p2, p2 to p2 - p1).forEach { (start, step) ->
                            generateSequence(start) { it + step }
                                .takeWhile { it.x in 0..maxX && it.y in 0..maxY }
                                .filterIndexed { index, _ -> indexFilter(index) }
                                .forEach { add(it) }
                        }
                    }
                }
            }
        }.size
    }

    override fun part1(input: List<String>, testArg: Any?) = countAntinodes(input) { it == 1 }

    override fun part2(input: List<String>, testArg: Any?) = countAntinodes(input) { true }

    override fun testCases1() = listOf(TestCase(getTestInput(), 14))

    override fun testCases2() = listOf(TestCase(getTestInput(), 34))

}

fun main() {
    Day08().main()
}
