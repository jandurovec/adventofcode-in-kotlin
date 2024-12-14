package aoc2015

import AdventDay
import Grid
import Point
import TestCase

class Day18 : AdventDay<Grid<Boolean>, Int, Int>(2015, 18) {

    private class TestArg(val size: Int, val steps: Int)

    override fun parseInput(stringInput: List<String>) = Grid<Boolean>().apply {
        stringInput.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c == '#') {
                    this[Point(x, y)] = true
                }
            }
        }
    }

    private fun conway(input: Grid<Boolean>, size: Int, steps: Int, fixed: Map<Point, Boolean> = emptyMap()): Int {
        var grid = Grid(input.plus(fixed))
        repeat(steps) {
            val newGrid = Grid(fixed)
            grid.keys.plus(grid.keys.flatMap { it.neighbors() }).toSet()
                .filter { it.x in 0 until size && it.y in 0 until size }
                .forEach { p ->
                    val litNeighbors = p.neighbors().count { grid.contains(it) }
                    if (grid.contains(p)) {
                        if (litNeighbors == 2 || litNeighbors == 3) {
                            newGrid[p] = true
                        }
                    } else if (litNeighbors == 3) {
                        newGrid[p] = true
                    }
                }
            grid = newGrid
        }
        return grid.size
    }

    override fun part1(input: Grid<Boolean>, testArg: Any?) =
        conway(input, if (testArg is TestArg) testArg.size else 100, if (testArg is TestArg) testArg.steps else 100)

    override fun part2(input: Grid<Boolean>, testArg: Any?) = (if (testArg is TestArg) testArg.size else 100).let {
        conway(
            input,
            it,
            if (testArg is TestArg) testArg.steps else 100,
            setOf(Point(0, 0), Point(0, it - 1), Point(it - 1, 0), Point(it - 1, it - 1)).associateWith { true }
        )
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 4, TestArg(6, 4)))
    override fun testCases2() = listOf(TestCase(getTestInput(), 17, TestArg(6, 5)))
}

fun main() {
    Day18().main()
}
