package aoc2024

import AdventDay
import Grid
import Point
import TestCase


class Day10 : AdventDay<Grid<Int>, Int, Int>(2024, 10) {


    override fun parseInput(stringInput: List<String>) = Grid(buildMap {
        stringInput.forEachIndexed { row, line ->
            line.forEachIndexed { x, c ->
                this[Point(x, stringInput.lastIndex - row)] = c.digitToInt()
            }
        }
    })

    private fun trailEnds(grid: Grid<Int>, start: Point, aggregator: MutableCollection<Point> = mutableSetOf()): Int {
        val toExplore = ArrayDeque<Pair<Point, Int>>() // point to expected
        val ends = aggregator
        toExplore.add(start to 0)
        while (!toExplore.isEmpty()) {
            val (cur, expected) = toExplore.removeFirst()
            if (grid[cur] == expected) {
                if (expected == 9) {
                    ends.add(cur)
                } else {
                    cur.neighbors(false).forEach { neighbor ->
                        toExplore.add(neighbor to expected + 1)
                    }
                }
            }
        }
        return ends.size
    }

    override fun part1(input: Grid<Int>, testArg: Any?) =
        input.keys.sumOf { trailEnds(input, it) }

    override fun part2(input: Grid<Int>, testArg: Any?) =
        input.keys.sumOf { trailEnds(input, it, mutableListOf()) }


    override fun testCases1() = listOf(TestCase(getTestInput(), 36))

    override fun testCases2() = listOf(TestCase(getTestInput(), 81))

}

fun main() {
    Day10().main()
}
