package aoc2024

import AdventDay
import Grid
import Point
import TestCase


class Day12 : AdventDay<Grid<Char>, Int, Int>(2024, 12) {

    override fun parseInput(stringInput: List<String>) = Grid(buildMap {
        stringInput.forEachIndexed { row, line ->
            line.forEachIndexed { x, c ->
                this[Point(x, stringInput.lastIndex - row)] = c
            }
        }
    })

    private fun Grid<Char>.areas() = sequence {
        val remaining = keys.toMutableSet()
        while (remaining.isNotEmpty()) {
            val start = remaining.first()
            val plant = this@areas[start]!!
            val toExplore = ArrayDeque<Point>()
            toExplore.add(start)
            val area = mutableSetOf(start)
            while (toExplore.isNotEmpty()) {
                val cur = toExplore.removeFirst()
                cur.neighbors(false).filter {
                    !area.contains(it) && remaining.contains(it) && this@areas[it] == plant
                }.forEach {
                    area.add(it)
                    toExplore.add(it)
                }
            }
            remaining.removeAll(area)
            yield(area)
        }
    }

    override fun part1(input: Grid<Char>, testArg: Any?) = input.areas().sumOf { area ->
        area.size * area.sumOf { p -> p.neighbors(false).count { !area.contains(it) } }
    }

    override fun part2(input: Grid<Char>, testArg: Any?): Int {
        fun List<Int>.segments() = if (size < 2) size else (1 + sorted().windowed(2).count { (l, r) -> l + 1 != r })
        fun Set<Point>.sides(): Int {
            val vertical = groupBy { it.x }.entries.sumOf { (x, points) ->
                val yCoords = points.map { it.y }
                yCoords.filter { !contains(Point(x - 1, it)) }.segments() +
                        yCoords.filter { !contains(Point(x + 1, it)) }.segments()
            }
            val horizontal = groupBy { it.y }.entries.sumOf { (y, points) ->
                val xCoords = points.map { it.x }
                xCoords.filter { !contains(Point(it, y - 1)) }.segments() +
                        xCoords.filter { !contains(Point(it, y + 1)) }.segments()
            }
            return vertical + horizontal
        }
        return input.areas().sumOf {
            it.size * it.sides()
        }
    }

    override fun testCases1() = listOf(
        TestCase(getInput("test1"), 140),
        TestCase(getInput("test2"), 772),
        TestCase(getInput("test3"), 1930)
    )

    override fun testCases2() = listOf(
        TestCase(getInput("test1"), 80),
        TestCase(getInput("test2"), 436),
        TestCase(getInput("test3"), 1206)
    )

}

fun main() {
    Day12().main()
}
