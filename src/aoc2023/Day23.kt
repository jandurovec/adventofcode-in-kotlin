package aoc2023

import AdventDay
import Point
import TestCase

class Day23 : AdventDay<Day23.Trails, Int, Int>(2023, 23) {
    class Trails(val map: Map<Point, Char>, val start: Point, val end: Point) {
        private val crossroads: List<Point> =
            map.keys.filter { p -> p.neighbors(false).count { map.containsKey(it) && map[it] != '#' } > 2 }

        fun longestPath(stepsFrom: (Point, Char) -> List<Point>): Int {
            val poi = (crossroads + start + end).toSet()
            val distances = buildMap {
                poi.forEach { from ->
                    this[from] = buildMap {
                        val toExplore = ArrayDeque<Pair<Point, Int>>()
                        val visited = mutableSetOf(from)
                        toExplore.addLast(from to 0)
                        while (toExplore.isNotEmpty()) {
                            val (cur, dist) = toExplore.removeFirst()
                            stepsFrom(cur, map[cur]!!).filter { map.containsKey(it) && !visited.contains(it) }.forEach {
                                visited.add(it)
                                if (poi.contains(it)) {
                                    this[it] = dist + 1
                                } else {
                                    toExplore.addLast(it to dist + 1)
                                }
                            }
                        }
                    }
                }
            }

            fun recSearch(from: Point, visited: Set<Point>): Int? = if (from == end) {
                0
            } else {
                distances[from]!!.filter { (p, _) -> !visited.contains(p) }
                    .mapNotNull { (next, d) -> recSearch(next, visited + next)?.let { d + it } }
                    .maxOfOrNull { it }
            }
            return recSearch(start, setOf(start)) ?: error("No path from $start to $end")
        }
    }

    override fun parseInput(stringInput: List<String>): Trails {
        var start: Point? = null
        var end: Point? = null
        val map = buildMap {
            stringInput.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c != '#') {
                        val p = Point(x, stringInput.lastIndex - y).also { put(it, c) }
                        if (y == 0) {
                            start = p
                        } else if (y == stringInput.lastIndex) {
                            end = p
                        }
                    }
                }
            }
        }
        return Trails(map, start!!, end!!)
    }

    override fun part1(input: Trails, testArg: Any?) = input.longestPath { pos, c ->
        when (c) {
            '>' -> listOf(pos.right())
            '<' -> listOf(pos.left())
            '^' -> listOf(pos.up())
            'v' -> listOf(pos.down())
            else -> pos.neighbors(false)
        }
    }

    override fun part2(input: Trails, testArg: Any?) = input.longestPath { pos, _ -> pos.neighbors(false) }

    override fun testCases1() = listOf(TestCase(getTestInput(), 94))

    override fun testCases2() = listOf(TestCase(getTestInput(), 154))
}

fun main() {
    Day23().main()
}
