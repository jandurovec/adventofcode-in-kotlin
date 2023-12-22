package aoc2023

import AdventDay
import Grid
import Point
import TestCase

class Day21 : AdventDay<Grid<Char>, Int, Long>(2023, 21) {
    override fun parseInput(stringInput: List<String>) = stringInput.flatMapIndexed { y, line ->
        line.mapIndexed { x, c -> Point(x, y) to c }
    }.associate { it }.toMutableMap()

    private fun Grid<Char>.explore(start: Point, steps: Int, overflow: Boolean = false) = buildMap {
        val maxX = this@explore.keys.maxOf { it.x }
        val maxY = this@explore.keys.maxOf { it.y }
        val toExplore = ArrayDeque<Pair<Point, Int>>()
        toExplore.addLast(start to 0)
        while (toExplore.isNotEmpty()) {
            val (cur, dist) = toExplore.removeFirst()
            val nextDist = dist + 1
            cur.neighbors(false)
                .filter { overflow || this@explore.containsKey(it) }
                .filter {
                    val origPoint = Point(it.x.mod(maxX + 1), it.y.mod(maxY + 1))
                    !containsKey(it) && this@explore[origPoint] != '#'
                }.forEach {
                    put(it, nextDist)
                    if (nextDist < steps) {
                        toExplore.addLast(it to nextDist)
                    }
                }
        }
    }


    override fun part1(input: Grid<Char>, testArg: Any?): Int {
        val start = input.entries.first { it.value == 'S' }.key
        val steps = if (testArg is Int) testArg else 64
        val stepParity = steps % 2
        return input.explore(start, steps, false).count { it.value <= steps && it.value % 2 == stepParity }
    }


    override fun part2(input: Grid<Char>, testArg: Any?): Long {
        /*
            26501365 = 65 + 202300 * 131
            Since the input has clear straight path from the center where the elf starts and no long narrow
            corridors, the final pattern looks like this (example for N = 4, real N = 202300)
                  eCe
                 eE0Ee
                eE010Ee
               eE01010Ee
               C0101010C
               eE01010Ee
                eE010Ee
                 eE0Ee
                  eCe
            Where:
            C is a corner tile populated just to reach the middle of the side opposing the entrance point
            e is an edge tile explored to "halfSize" steps (65 in our case)
            E is an edge tile explored to "size + halfSize" steps (131 + 65 in our case)
            1 is a tile with valid "odd" steps
            0 is a tile with valid "even" steps
         */
        val steps = 26501365
        val maxX = input.keys.maxOf { it.x }
        val gardenSize = (maxX + 1).also { if (it % 2 == 0) error("Garden size must be odd") }
        val half = (maxX / 2)

        // let's pre-calculate tiles for N=2 and do a smoke test
        val parity = steps % 2
        val helper = input.explore(Point(half, half), half + 2 * gardenSize, true)
            .filter { it.value % 2 == parity }

        fun reachableInTile(tileX: Int, tileY: Int) = helper.entries.count { (p, _) ->
            p.x in tileX * gardenSize..<(tileX + 1) * gardenSize && p.y in tileY * gardenSize..<(tileY + 1) * gardenSize
        }

        val corners = listOf(0 to 2, 2 to 0, 0 to -2, -2 to 0).sumOf { (x, y) -> reachableInTile(x, y) }
        val oddTile = reachableInTile(0, 0)
        val evenTile = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0).map { (x, y) -> reachableInTile(x, y) }
            .reduce { prev, next -> if (prev != next) error("Even tiles don't have the same counts ($prev vs $next)") else prev }
        val bigEdges = listOf(1 to 1, 1 to -1, -1 to 1, -1 to -1).sumOf { (x, y) -> reachableInTile(x, y) }
        val smallEdges = listOf(1 to 2, -2 to 1, -1 to -2, 2 to -1).sumOf { (x, y) -> reachableInTile(x, y) }

        val n = (steps - half) / gardenSize.toLong()
        return n * n * evenTile + (n - 1) * (n - 1) * oddTile + (n - 1) * bigEdges + n * smallEdges + corners
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 16, 6))

    override fun testCases2() = emptyList<TestCase<Grid<Char>, Long>>()
}

fun main() {
    Day21().main()
}
