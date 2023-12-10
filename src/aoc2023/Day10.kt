package aoc2023

import AdventDay
import Grid
import Point
import TestCase


class Day10 : AdventDay<Grid<Char>, Int, Int>(2023, 10) {
    private val validUp = setOf('|', 'F', '7')
    private val validDown = setOf('|', 'L', 'J')
    private val validLeft = setOf('-', 'F', 'L')
    private val validRight = setOf('-', 'J', '7')

    private fun Grid<Char>.pipeNeighbors(pos: Point) = when (this[pos]) {
        'L' -> listOf(validUp to pos.up(), validRight to pos.right())
        'J' -> listOf(validUp to pos.up(), validLeft to pos.left())
        '7' -> listOf(validDown to pos.down(), validLeft to pos.left())
        'F' -> listOf(validDown to pos.down(), validRight to pos.right())
        '|' -> listOf(validUp to pos.up(), validDown to pos.down())
        '-' -> listOf(validLeft to pos.left(), validRight to pos.right())
        'S' -> listOf(validUp to pos.up(), validDown to pos.down(), validLeft to pos.left(), validRight to pos.right())
        else -> emptyList()
    }.filter { (valid, cur) -> valid.contains(this[cur]) }
        .map { it.second }
        .toSet()

    override fun parseInput(stringInput: List<String>) = stringInput.flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Point(x, stringInput.lastIndex - y) to char
        }
    }.associate { it }.toMutableMap()

    private fun pipe(input: Grid<Char>, start: Point): Map<Point, Int> {
        val toExplore = ArrayDeque<Pair<Point, Int>>()
        val pipeSegments = mutableMapOf(start to 0)
        toExplore.addLast(start to 0)
        while (!toExplore.isEmpty()) {
            val (cur, dist) = toExplore.removeFirst()
            input.pipeNeighbors(cur).forEach { pos ->
                if (!pipeSegments.containsKey(pos)) {
                    pipeSegments[pos] = dist + 1
                    toExplore.addLast(pos to dist + 1)
                }
            }
        }
        return pipeSegments
    }

    override fun part1(input: Grid<Char>, testArg: Any?) = pipe(input, input.entries.first { it.value == 'S' }.key)
        .maxOf { it.value }

    override fun part2(input: Grid<Char>, testArg: Any?): Int {
        val start = input.entries.first { it.value == 'S' }.key
        val pipe = pipe(input, start)
        val realStart = when (input.pipeNeighbors(start)) {
            setOf(start.up(), start.right()) -> 'L'
            setOf(start.right(), start.down()) -> 'F'
            setOf(start.down(), start.left()) -> '7'
            setOf(start.left(), start.up()) -> 'J'
            setOf(start.left(), start.right()) -> '-'
            else -> '|'
        }
        val expandedPipe = pipe.keys
            .map { it to if (it == start) realStart else input[it]!! }
            .flatMap { (p, c) ->
                val extPoint = Point(2 * p.x, 2 * p.y)
                when (c) {
                    '-' -> listOf(extPoint, extPoint.right())
                    '|' -> listOf(extPoint, extPoint.up())
                    'L' -> listOf(extPoint, extPoint.up(), extPoint.right())
                    'J' -> listOf(extPoint, extPoint.up())
                    'F' -> listOf(extPoint, extPoint.right())
                    else -> listOf(extPoint)
                }
            }.toSet()

        val (maxX, maxY) = expandedPipe.reduce { p1, p2 -> Point(maxOf(p1.x, p2.x), maxOf(p1.y, p2.y)) }
        val xRange = 0..maxX
        val yRange = 0..maxY
        val surfacePoi = expandedPipe.flatMap { it.neighbors(false) }
            .filter { !expandedPipe.contains(it) }.toMutableSet()
        var enclosed = 0
        areaExploration@ while (surfacePoi.isNotEmpty()) {
            val areaStart = surfacePoi.first().also { surfacePoi.remove(it) }
            val toExploreEnclosed = ArrayDeque<Point>()
            toExploreEnclosed.addLast(areaStart)
            val area = mutableSetOf(areaStart)
            while (toExploreEnclosed.isNotEmpty()) {
                val cur = toExploreEnclosed.removeFirst()
                val neighbors = cur.neighbors(false)
                for (n in neighbors.filter { !area.contains(it) && !expandedPipe.contains(it) }) {
                    if (n.x in xRange && n.y in yRange) {
                        toExploreEnclosed.addLast(n)
                        area.add(n)
                        surfacePoi.remove(n)
                    } else {
                        continue@areaExploration
                    }
                }
            }
            enclosed += area.count { it.x % 2 == 0 && it.y % 2 == 0 }
        }
        return enclosed
    }

    override fun testCases1() = listOf(
        TestCase(getInput("test1a"), 4),
        TestCase(getInput("test1b"), 8)
    )

    override fun testCases2() = listOf(
        TestCase(getInput("test2a"), 4),
        TestCase(getInput("test2b"), 8),
        TestCase(getInput("test2c"), 10),
    )
}

fun main() {
    Day10().main()
}
