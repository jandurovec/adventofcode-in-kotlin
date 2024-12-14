package aoc2024

import AdventDay
import Point
import Point.Companion.DOWN
import Point.Companion.LEFT
import Point.Companion.RIGHT
import Point.Companion.UP
import TestCase


class Day06 : AdventDay<Day06.Lab, Int, Int>(2024, 6) {

    data class Lab(val maxX: Int, val maxY: Int, val obstructions: Set<Point>, val guardStart: Point) {
        data class Path(val steps: Set<Pair<Point, Point>>, val hasLoop: Boolean)

        fun findPath(): Path {
            var guard = guardStart
            var direction = UP
            val visited = mutableSetOf<Pair<Point, Point>>() // position to direction
            while (guard.x in 0..maxX && guard.y in 0..maxY) {
                if (!visited.add(guard to direction)) {
                    return Path(visited, true)
                }
                val next = guard + direction
                if (obstructions.contains(next)) {
                    direction = when (direction) {
                        UP -> RIGHT
                        RIGHT -> DOWN
                        DOWN -> LEFT
                        else -> UP
                    }
                } else {
                    guard = next
                }
            }
            return Path(visited, false)
        }
    }

    override fun parseInput(stringInput: List<String>): Lab {
        val maxX = stringInput[0].lastIndex
        val maxY = stringInput.lastIndex
        val obstructions = mutableSetOf<Point>()
        var guard: Point? = null
        stringInput.forEachIndexed { row, s ->
            s.forEachIndexed { col, chr ->
                when (chr) {
                    '#' -> obstructions.add(Point(col, row))
                    '^' -> guard = Point(col, row)
                }
            }
        }
        return Lab(maxX, maxY, obstructions, guard!!)
    }

    override fun part1(input: Lab, testArg: Any?) = input.findPath().steps.map { it.first }.toSet().size

    override fun part2(input: Lab, testArg: Any?): Int {
        return input.findPath().steps.map { it.first }.toSet().minus(input.guardStart)
            .count { newObst ->
                Lab(input.maxX, input.maxY, input.obstructions + newObst, input.guardStart).findPath().hasLoop
            }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 41))

    override fun testCases2() = listOf(TestCase(getTestInput(), 6))

}

fun main() {
    Day06().main()
}
