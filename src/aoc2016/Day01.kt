package aoc2016

import Point
import SingleStringDay
import TestCase
import manhattanDist
import times

class Day01 : SingleStringDay<Int, Int>(2016, 1) {
    companion object {
        val DIRECTIONS = listOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))
        val ORIGIN = Point(0, 0)
    }

    fun steps(input: String) = input.split(Regex(", ")).runningFold(ORIGIN to 0) { (pos, dir), instr ->
        val newDir = (dir + if (instr[0] == 'R') 1 else -1).mod(DIRECTIONS.size)
        val dist = instr.substring(1).toInt()
        pos + dist * DIRECTIONS[newDir] to newDir
    }

    override fun part1(input: String, testArg: Any?) =
        steps(input).last().let { (pos, _) -> manhattanDist(ORIGIN.x, ORIGIN.y, pos.x, pos.y) }

    override fun part2(input: String, testArg: Any?): Int {
        val visited = mutableSetOf<Point>()
        return sequence {
            yield(ORIGIN)
            steps(input).windowed(2).forEach { (from, to) ->
                val step = DIRECTIONS[to.second]
                var cur = from.first
                while (cur != to.first) {
                    cur += step
                    yield(cur)
                }
            }
        }.first { !visited.add(it) }.let { manhattanDist(ORIGIN.x, ORIGIN.y, it.x, it.y) }
    }

    override fun testCases1() = listOf(
        TestCase("R2, L3", 5),
        TestCase("R2, R2, R2", 2),
        TestCase("R5, L5, R5, R3", 12)
    )

    override fun testCases2() = listOf(TestCase("R8, R4, R4, R8", 4))
}

fun main() {
    Day01().main()
}
