package aoc2023

import AdventDay
import Point
import TestCase


class Day16 : AdventDay<Pair<Map<Point, Char>, Pair<Int, Int>>, Int, Int>(2023, 16) {
    override fun parseInput(stringInput: List<String>): Pair<Map<Point, Char>, Pair<Int, Int>> {
        val maxY = stringInput.lastIndex
        val maxX = stringInput.first().lastIndex
        val grid = stringInput.flatMapIndexed { row: Int, s: String ->
            s.mapIndexed { col, c ->
                Point(col, row) to c
            }
        }.associate { it }
        return grid to (maxX to maxY)
    }

    private fun explore(grid: Map<Point, Char>, start: Pair<Point, Point>): Int {
        val toExplore = ArrayDeque<Pair<Point, Point>>()
        val visited = mutableSetOf(start)
        toExplore.add(start)
        while (toExplore.isNotEmpty()) {
            val (cur, dir) = toExplore.removeFirst()
            val c = grid[cur]
            val next = when {
                c == '|' && (dir == Point.LEFT || dir == Point.RIGHT) ->
                    listOf(cur.up() to Point.UP, cur.down() to Point.DOWN)

                c == '-' && (dir == Point.UP || dir == Point.DOWN) ->
                    listOf(cur.left() to Point.LEFT, cur.right() to Point.RIGHT)

                c == '\\' -> when (dir) {
                    Point.UP -> listOf(cur.left() to Point.LEFT)
                    Point.DOWN -> listOf(cur.right() to Point.RIGHT)
                    Point.LEFT -> listOf(cur.up() to Point.UP)
                    else -> listOf(cur.down() to Point.DOWN)
                }

                c == '/' -> when (dir) {
                    Point.DOWN -> listOf(cur.left() to Point.LEFT)
                    Point.UP -> listOf(cur.right() to Point.RIGHT)
                    Point.RIGHT -> listOf(cur.up() to Point.UP)
                    else -> listOf(cur.down() to Point.DOWN)
                }

                else -> listOf(cur + dir to dir)
            }
            next.forEach {
                if (grid.containsKey(it.first) && visited.add(it)) {
                    toExplore.add(it)
                }
            }
        }
        return visited.map { it.first }.toSet().count()
    }

    override fun part1(input: Pair<Map<Point, Char>, Pair<Int, Int>>, testArg: Any?) =
        explore(input.first, Point(0, 0) to Point.RIGHT)

    override fun part2(input: Pair<Map<Point, Char>, Pair<Int, Int>>, testArg: Any?): Int {
        val (grid, dimensions) = input
        val (maxX, maxY) = dimensions
        val vertical = (0..maxX).flatMap { listOf(Point(it, 0) to Point.DOWN, Point(it, maxY) to Point.UP) }
        val horizontal = (0..maxY).flatMap { listOf(Point(0, it) to Point.RIGHT, Point(maxX, it) to Point.LEFT) }
        return (vertical + horizontal).maxOf { explore(grid, it) }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 46))

    override fun testCases2() = listOf(TestCase(getTestInput(), 51))
}

fun main() {
    Day16().main()
}
