package aoc2016

import AdventDay
import Grid
import Point
import TestCase

class Day13 : AdventDay<Int, Int, Int>(2016, 13) {

    override fun parseInput(stringInput: List<String>) = stringInput.first().toInt()

    private fun generatePoints(favorite: Int) = sequence {
        val grid = Grid<Boolean>()
        val start = Point(1, 1)
        val toExplore = ArrayDeque<Pair<Point, Int>>()
        toExplore.addLast(start to 0)
        val visited = mutableSetOf(start)
        while (toExplore.isNotEmpty()) {
            val cur = toExplore.removeFirst()
            yield(cur)
            cur.first.neighbors(false).filter { it.x >= 0 && it.y >= 0 }.filter { !visited.contains(it) }.filter { p ->
                grid.computeIfAbsent(p) { (favorite + it.x * it.x + 3 * it.x + 2 * it.x * it.y + it.y + it.y * it.y).countOneBits() % 2 == 0 }
            }.forEach { p ->
                visited.add(p)
                toExplore.addLast(p to cur.second + 1)
            }
        }
    }

    override fun part1(input: Int, testArg: Any?) =
        generatePoints(input).first { it.first == if (testArg is Point) testArg else Point(31, 39) }.second

    override fun part2(input: Int, testArg: Any?) = generatePoints(input).takeWhile { it.second <= 50 }.count()

    override fun testCases1() = listOf(
        TestCase(10, 11, Point(7, 4))
    )
}

fun main() {
    Day13().main()
}
