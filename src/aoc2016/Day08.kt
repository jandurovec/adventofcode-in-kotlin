package aoc2016

import Grid
import Point
import TestCase
import UnparsedDay

class Day08 : UnparsedDay<Int, String>(2016, 8) {
    companion object {
        val SIZE = 50 to 6
        const val LIT = '#'
    }

    private fun process(instructions: List<String>, gridSize: Pair<Int, Int>): Grid<Char> {
        var grid = Grid<Char>()
        instructions.forEach { s ->
            if (s.startsWith("rect")) {
                val (width, height) = "rect (.*)x(.*)".toRegex().matchEntire(s)!!.groupValues.drop(1).map { it.toInt() }
                (0 until width).forEach { x ->
                    (0 until height).forEach { y ->
                        grid[Point(x, y)] = LIT
                    }
                }
            } else if (s.startsWith("rotate row")) {
                val (row, offset) = "rotate row y=(.*) by (.*)".toRegex().matchEntire(s)!!.groupValues.drop(1)
                    .map { it.toInt() }
                grid = grid.keys.map { p ->
                    if (p.y == row) Point((p.x + offset).mod(gridSize.first), p.y) else p
                }.associateWith { LIT }.toMutableMap()
            } else if (s.startsWith("rotate column")) {
                val (col, offset) = "rotate column x=(.*) by (.*)".toRegex().matchEntire(s)!!.groupValues.drop(1)
                    .map { it.toInt() }
                grid = grid.keys.map { p ->
                    if (p.x == col) Point(p.x, (p.y + offset).mod(gridSize.second)) else p
                }.associateWith { LIT }.toMutableMap()
            }
        }
        return grid
    }

    @Suppress("UNCHECKED_CAST")
    override fun part1(input: List<String>, testArg: Any?) =
        process(input, if (testArg is Pair<*, *>) testArg as Pair<Int, Int> else SIZE).size

    override fun part2(input: List<String>, testArg: Any?) = buildString {
        process(input, SIZE).let { grid ->
            (0 until SIZE.second).forEach { row ->
                (0 until SIZE.first).forEach { col ->
                    append(grid.getOrDefault(Point(col, row), ' '))
                }
                appendLine()
            }
        }
    }

    override fun testCases1() = listOf(
        TestCase(getTestInput(), 6, 7 to 3)
    )
}

fun main() {
    Day08().main()
}
