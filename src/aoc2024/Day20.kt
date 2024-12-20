package aoc2024

import AdventDay
import Grid
import Point
import TestCase
import manhattanDist


class Day20 : AdventDay<Grid<Char>, Int, Int>(2024, 20) {

    companion object {
        const val WALL = '#'
        const val START = 'S'
        const val END = 'E'
    }

    override fun parseInput(stringInput: List<String>): Grid<Char> = stringInput.flatMapIndexed { y: Int, s: String ->
        s.mapIndexed { x: Int, c: Char -> Point(x, y) to c }
    }.toMap().toMutableMap()

    private fun Grid<Char>.distances(start: Point, end: Point): Map<Point, Int> {
        val toExplore = ArrayDeque<Pair<Point, Int>>()
        toExplore.add(start to 0)
        val distances = mutableMapOf(start to 0)
        while (toExplore.isNotEmpty()) {
            val (cur, path) = toExplore.removeFirst()
            cur.neighbors(false)
                .filter { it in this.keys && this[it] != WALL && it !in distances.keys }
                .forEach {
                    distances[it] = path + 1
                    if (it != end) {
                        toExplore.add(it to path + 1)
                    }
                }
        }
        return distances
    }

    private fun Grid<Char>.cheats(cheatStart: Point, cheatDuration: Int): Set<Point> = buildSet {
        val toExplore = ArrayDeque<Pair<Point, Int>>()
        toExplore.add(cheatStart to 0)
        val visited = mutableSetOf(cheatStart)
        while (toExplore.isNotEmpty()) {
            val (cur, time) = toExplore.removeFirst()
            if (time <= cheatDuration && this@cheats[cur] != WALL) {
                add(cur)
            }
            if (time < cheatDuration) {
                cur.neighbors(false).filter { it in this@cheats.keys && it !in visited }.forEach {
                    visited.add(it)
                    toExplore.add(it to time + 1)
                }
            }
        }
    }

    private fun Grid<Char>.countCheats(toSave: Int, cheatDuration: Int): Int {
        val start = entries.first { it.value == START }.key
        val end = entries.first { it.value == END }.key
        val distToEnd = distances(end, start)
        val bestPath = buildList {
            var cur = start
            while (distToEnd[cur] != 0) {
                add(cur)
                cur = cur.neighbors(false).first { distToEnd[it] == distToEnd[cur]!! - 1 }
            }
        }
        return bestPath.takeWhile { distToEnd[it]!! > toSave - 2 }.sumOf { cheatStart ->
            cheats(cheatStart, cheatDuration).filter { distToEnd.containsKey(it) }.count { cheatEnd ->
                val cheatLength = manhattanDist(cheatStart.x, cheatStart.y, cheatEnd.x, cheatEnd.y)
                distToEnd[cheatStart]!! - cheatLength - distToEnd[cheatEnd]!! >= toSave
            }
        }
    }

    override fun part1(input: Grid<Char>, testArg: Any?): Int {
        val toSave = if (testArg is Int) testArg else 100
        return input.countCheats(toSave, 2)
    }

    override fun part2(input: Grid<Char>, testArg: Any?): Int {
        val toSave = if (testArg is Int) testArg else 100
        return input.countCheats(toSave, 20)
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 1, 64))

    override fun testCases2() = listOf(TestCase(getTestInput(), 3, 76))

}

fun main() {
    Day20().main()
}
