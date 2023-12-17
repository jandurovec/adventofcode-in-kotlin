package aoc2023

import AdventDay
import Point
import TestCase
import java.util.PriorityQueue


class Day17 : AdventDay<Day17.City, Int, Int>(2023, 17) {
    data class Node(val pos: Point, val horizontal: Boolean)
    class City(val data: Map<Point, Int>, val maxX: Int, val maxY: Int) {
        fun neighbours(n: Node, range: IntRange) =
            (if (n.horizontal) listOf(Point.RIGHT, Point.LEFT) else listOf(Point.DOWN, Point.UP)).flatMap { direction ->
                (1..range.last).asSequence().runningFold(n.pos) { prev, _ -> prev + direction }.drop(1)
                    .filter(data::containsKey)
                    .runningFold(n to 0) { acc, next -> Node(next, !n.horizontal) to (acc.second + data[next]!!) }
                    .drop(range.first)
                    .toList()
            }
    }

    override fun parseInput(stringInput: List<String>): City {
        val maxY = stringInput.lastIndex
        val maxX = stringInput.first().lastIndex
        val grid = stringInput.flatMapIndexed { row: Int, s: String ->
            s.mapIndexed { col, c -> Point(col, maxY - row) to c.digitToInt() }
        }.associate { it }
        return City(grid, maxX, maxY)
    }


    private fun findPath(city: City, start: Point, end: Point, range: IntRange): Int {
        val distances = PriorityQueue<Pair<Node, Int>> { a, b -> a.second - b.second }
        val unexplored = listOf(true, false).flatMap { dir -> city.data.keys.map { Node(it, dir) } }.toMutableSet()
        distances.addAll(city.neighbours(Node(start, true), range))
        distances.addAll(city.neighbours(Node(start, false), range))
        while (unexplored.isNotEmpty()) {
            val (cur, dist) = distances.poll()
            if (cur.pos == end) {
                return dist
            }
            if (unexplored.remove(cur)) {
                distances.addAll(city.neighbours(cur, range).filter { unexplored.contains(it.first) }
                    .map { (node, d) -> node to dist + d })
            }
        }
        error("No path from $start to $end")
    }

    override fun part1(input: City, testArg: Any?) =
        findPath(input, Point(0, input.maxY), Point(input.maxX, 0), 1..3)

    override fun part2(input: City, testArg: Any?) =
        findPath(input, Point(0, input.maxY), Point(input.maxX, 0), 4..10)

    override fun testCases1() = listOf(TestCase(getTestInput(), 102))

    override fun testCases2() = listOf(TestCase(getTestInput(), 94))
}

fun main() {
    Day17().main()
}
