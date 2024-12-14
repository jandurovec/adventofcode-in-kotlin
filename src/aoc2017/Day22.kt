package aoc2017

import AdventDay
import Grid
import Point
import TestCase

class Day22 : AdventDay<Grid<Char>, Int, Int>(2017, 22) {
    companion object {
        const val CLEAN = '.'
        const val INFECTED = '#'
        const val WEAKENED = 'W'
        const val FLAGGED = 'F'
        val DIRECTIONS = listOf(Point.UP, Point.RIGHT, Point.DOWN, Point.LEFT)
    }

    override fun parseInput(stringInput: List<String>) = Grid(stringInput.flatMapIndexed { row, line ->
        line.mapIndexed { col, c -> Point(col, row) to c }
    }.associate { it })

    private fun Grid<Char>.simulate(steps: Int, stateTransitions: Map<Char, Char>, turn: (Char, Int) -> Int): Int {
        var pos = Point(keys.maxOf { it.x } / 2, keys.maxOf { it.y } / 2)
        var dirIndex = 0
        var newlyInfected = 0
        repeat(steps) {
            val cur = getOrDefault(pos, CLEAN)
            dirIndex = turn(cur, dirIndex)
            val newState = stateTransitions[cur]!!
            if (newState == INFECTED) {
                newlyInfected++
            }
            put(pos, newState)
            pos += DIRECTIONS[dirIndex]
        }
        return newlyInfected
    }

    override fun part1(input: Grid<Char>, testArg: Any?) =
        input.simulate(10000, mapOf(CLEAN to INFECTED, INFECTED to CLEAN)) { cell, dirIndex ->
            (dirIndex + if (cell == INFECTED) 1 else -1).mod(DIRECTIONS.size)
        }

    override fun part2(input: Grid<Char>, testArg: Any?) =
        input.simulate(
            10000000,
            mapOf(CLEAN to WEAKENED, WEAKENED to INFECTED, INFECTED to FLAGGED, FLAGGED to CLEAN)
        ) { cell, dirIndex ->
            (dirIndex + when (cell) {
                CLEAN -> -1
                WEAKENED -> 0
                INFECTED -> 1
                FLAGGED -> 2
                else -> error("Unknown state $cell")
            }).mod(DIRECTIONS.size)
        }

    override fun testCases1() = listOf(TestCase(getTestInput(), 5587))
    override fun testCases2() = listOf(TestCase(getTestInput(), 2511944))
}

fun main() {
    Day22().main()
}
