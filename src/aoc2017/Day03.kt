package aoc2017

import AdventDay
import Grid
import Point
import TestCase

class Day03 : AdventDay<Int, Int, Int>(2017, 3) {
    override fun parseInput(stringInput: List<String>) = stringInput.first().toInt()


    override fun part1(input: Int, testArg: Any?): Int {
        data class SpiralLoop(val index: Int, val start: Int, val side: Int) {
            fun distances() = sequence {
                repeat(4) {
                    (side - 2 downTo index).forEach { yield(it) }
                    (index + 1 until side).forEach { yield(it) }
                }
            }
        }

        fun loopSequence() = generateSequence(SpiralLoop(1, 2, 3)) {
            SpiralLoop(it.index + 1, it.start + 4 * (it.side - 1), it.side + 2)
        }

        return if (input == 1) 0 else {
            val loop = loopSequence().takeWhile { it.start <= input }.last()
            val indexOnLoop = input - loop.start
            loop.distances().drop(indexOnLoop).first()
        }
    }

    override fun part2(input: Int, testArg: Any?): Int {
        val spiral = sequence {
            val grid = Grid<Int>()
            grid[Point(0, 0)] = 1
            grid[Point(1, 0)] = 1
            val directions = listOf(Point.UP, Point.LEFT, Point.DOWN, Point.RIGHT)
            var pos = Point(1, 0)
            var dirIndex = 0
            var checkIndex = 1
            while (true) {
                while (grid.containsKey(pos + directions[checkIndex])) {
                    pos += directions[dirIndex]
                    val newValue = pos.neighbors().filter { grid.containsKey(it) }.sumOf { grid[it]!! }
                    yield(newValue)
                    grid[pos] = newValue
                }
                dirIndex = (dirIndex + 1) % directions.size
                checkIndex = (checkIndex + 1) % directions.size
            }
        }
        return spiral.dropWhile { it <= input }.first()
    }

    override fun testCases1() = listOf(
        TestCase(1, 0),
        TestCase(12, 3),
        TestCase(23, 2),
        TestCase(1024, 31)
    )
}

fun main() {
    Day03().main()
}
