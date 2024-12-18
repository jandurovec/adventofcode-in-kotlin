package aoc2024

import AdventDay
import Point
import TestCase

class Day18 : AdventDay<List<Point>, Int, String>(2024, 18) {

    data class ExtraSpec(val toTake: Int, val maxCoord: Int)

    override fun parseInput(stringInput: List<String>) =
        stringInput.map { l -> l.split(",").map { it.toInt() } }.map { (x, y) -> Point(x, y) }

    private fun findPath(corrupted: Set<Point>, max: Int): Set<Point> {
        val start = Point(0, 0)
        val end = Point(max, max)
        val toExplore = ArrayDeque<Pair<Point, Set<Point>>>()
        toExplore.add(start to setOf(start))
        val visited = mutableSetOf<Point>()
        while (toExplore.isNotEmpty()) {
            val (pos, path) = toExplore.removeFirst()
            pos.neighbors(false)
                .filter { it.x in 0..max && it.y in 0..max && it !in visited && it !in corrupted }
                .forEach {
                    if (it == end) {
                        return path + it
                    }
                    visited.add(it)
                    toExplore.add(it to path + it)
                }
        }
        return emptySet()
    }

    override fun part1(input: List<Point>, testArg: Any?): Int {
        val corrupted = input.take(if (testArg is ExtraSpec) testArg.toTake else 1024).toSet()
        val max = if (testArg is ExtraSpec) testArg.maxCoord else 70
        return findPath(corrupted, max).size - 1
    }

    override fun part2(input: List<Point>, testArg: Any?): String {
        // we know that in part1 the path still exists
        val takeMin = if (testArg is ExtraSpec) testArg.toTake else 1024
        val maxCoord = if (testArg is ExtraSpec) testArg.maxCoord else 70
        val corrupted = input.take(takeMin).toMutableSet()
        var bestPath = findPath(corrupted, maxCoord)
        for (i in takeMin..input.lastIndex) {
            val nextBit = input[i]
            corrupted += nextBit
            if (bestPath.contains(nextBit)) {
                bestPath = findPath(corrupted, maxCoord)
                if (bestPath.isEmpty()) {
                    return nextBit.let { "${it.x},${it.y}" }
                }
            }
        }
        error("No byte blocks path to (${maxCoord},${maxCoord})")
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), 22, ExtraSpec(12, 6)))
    override fun testCases2() = listOf(TestCase(getTestInput(), "6,1", ExtraSpec(12, 6)))

}

fun main() {
    Day18().main()
}
