package aoc2016

import AdventDay
import Grid
import Point

class Day22 : AdventDay<List<Day22.Node>, Int, Int>(2016, 22) {
    data class Node(val x: Int, val y: Int, val size: Int, val used: Int, val available: Int)

    override fun parseInput(stringInput: List<String>) = stringInput.filter { it.contains("/dev/grid") }.map { l ->
        """/dev/grid/node-x([0-9]+)-y([0-9]+) +([0-9]+)T +([0-9]+)T +([0-9]+)T +[0-9]+%""".toRegex()
            .matchEntire(l)!!.groupValues.drop(1).map { it.toInt() }.let { (x, y, size, used, available) ->
                Node(x, y, size, used, available)
            }
    }

    override fun part1(input: List<Node>, testArg: Any?) =
        input.sumOf { node ->
            if (node.used == 0) 0 else input.count { node != it && it.available >= node.used }
        }

    private fun findPath(grid: Grid<Boolean>, start: Point, end: Point): Int {
        val toExplore = ArrayDeque<Pair<Point, Int>>().also { it.addLast(start to 0) }
        val visited = mutableSetOf(start)
        while (toExplore.isNotEmpty()) {
            val (cur, dist) = toExplore.removeFirst()
            cur.neighbors(false).filter { grid.containsKey(it) && !visited.contains(it) }.forEach { p ->
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

    override fun part2(input: List<Node>, testArg: Any?): Int {
        val minSize = input.minOf { it.size }
        val free = input.first { it.used == 0 }
        val maxX = input.maxOf { it.x }
        // check if first 2 rows are free, and we are free to do final rotations there
        if (input.any { it.used > minSize && it.y < 2 }) {
            error("Different algorithm needed")
        }
        // move free space left of target node
        val grid = Grid(input.filter { it.used <= minSize }.map { Point(it.x, it.y) }.associateWith { true })
        val steps = findPath(grid, Point(free.x, free.y), Point(maxX - 1, 0))
        // then it takes 1 step to move target data to the left (now free)
        // and 5 steps to move the data each other step along the way to (0,0)
        return steps + 1 + 5 * (maxX - 1)
    }
}

fun main() {
    Day22().main()
}
