package aoc2016

import AdventDay
import Grid
import Point
import TestCase
import permutations

class Day24 : AdventDay<Day24.Hvac, Int, Int>(2016, 24) {
    companion object {
        const val START = '0'
    }

    inner class Hvac(private val plan: Grid<Char>) {
        val poi = plan.entries.filter { it.value.isDigit() }.associate { it.value to it.key }
        val distances = mutableMapOf<Char, MutableMap<Char, Int>>()

        init {
            poi.forEach { (from, start) ->
                poi.filter { it.key > from }.forEach { (to, dest) ->
                    val dist = findPath(plan, start, dest)
                    distances.computeIfAbsent(from) { mutableMapOf() }[to] = dist
                    distances.computeIfAbsent(to) { mutableMapOf() }[from] = dist
                }
            }

        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Point(x, y) to c }
    }.associate { it }.let { Hvac(Grid(it)) }

    private fun findPath(grid: Grid<Char>, start: Point, end: Point): Int {
        val toExplore = ArrayDeque<Pair<Point, Int>>().also { it.addLast(start to 0) }
        val visited = mutableSetOf(start)
        while (toExplore.isNotEmpty()) {
            val (cur, dist) = toExplore.removeFirst()
            cur.neighbors(false).filter { grid.containsKey(it) && !visited.contains(it) && grid.getValue(it) != '#' }
                .forEach { p ->
                    if (p == end) {
                        return dist + 1
                    } else {
                        visited.add(p)
                        toExplore.addLast(p to dist + 1)
                    }
                }
        }
        error("No path from $start to $end")
    }

    override fun part1(input: Hvac, testArg: Any?) = (input.poi.keys - START).permutations().minOf {
        (listOf(START) + it).windowed(2).sumOf { (from, to) -> input.distances[from]!![to]!! }
    }


    override fun part2(input: Hvac, testArg: Any?) = (input.poi.keys - START).permutations().minOf {
        (listOf(START) + it + START).windowed(2).sumOf { (from, to) -> input.distances[from]!![to]!! }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 14))
}

fun main() {
    Day24().main()
}
