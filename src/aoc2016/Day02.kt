package aoc2016

import Grid
import Point
import TestCase
import UnparsedDay

class Day02 : UnparsedDay<String, String>(2016, 2) {
    private fun parseGrid(def: List<String>): Pair<Grid<Char>, Point> {
        val grid = Grid<Char>()
        var start: Point? = null
        def.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c != ' ') {
                    grid[Point(x, y)] = c
                }
                if (c == '5') {
                    start = Point(x, y)
                }
            }
        }
        return grid to (start ?: error("Start not found"))
    }

    private fun findCode(def: List<String>, codeInstructions: List<String>) = buildString {
        var (grid, cur) = parseGrid(def)
        codeInstructions.forEach { s ->
            s.forEach {
                when (it) {
                    'R' -> if (grid.contains(cur.right())) cur = cur.right()
                    'L' -> if (grid.contains(cur.left())) cur = cur.left()
                    'U' -> if (grid.contains(cur.up())) cur = cur.up()
                    'D' -> if (grid.contains(cur.down())) cur = cur.down()
                    else -> error("Unknown direction $it")
                }
            }
            append(grid[cur])
        }
    }

    override fun part1(input: List<String>, testArg: Any?) = findCode(
        listOf(
            "123",
            "456",
            "789"
        ), input
    )

    override fun part2(input: List<String>, testArg: Any?) = findCode(
        listOf(
            "  1  ",
            " 234 ",
            "56789",
            " ABC ",
            "  D  ",
        ), input
    )

    private val testInput = getTestInput()
    override fun testCases1() = listOf(TestCase(testInput, "1985"))
    override fun testCases2() = listOf(TestCase(testInput, "5DB3"))
}

fun main() {
    Day02().main()
}
