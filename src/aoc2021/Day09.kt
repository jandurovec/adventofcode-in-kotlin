package aoc2021

import readInput

fun main() {
    fun parseInput(name: String) = readInput(name).flatMapIndexed { row, r ->
        r.mapIndexed { col, c -> (row to col) to c.digitToInt() }
    }.toMap()

    fun neighbors(heightMap: Map<Pair<Int, Int>, Int>, pos: Pair<Int, Int>) =
        listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0).map { pos.first + it.first to pos.second + it.second }
            .filter { heightMap.containsKey(it) }

    fun part1(heightMap: Map<Pair<Int, Int>, Int>) = heightMap.filter { (pos, height) ->
        neighbors(heightMap, pos).all { heightMap[it]!! > height }
    }.values.sumOf { it + 1 }

    fun part2(heightMap: Map<Pair<Int, Int>, Int>): Int {
        val unexplored = heightMap.filterValues { it < 9 }.toMutableMap()
        val basinSizes = mutableListOf<Int>()
        while (unexplored.isNotEmpty()) {
            val toExplore = mutableListOf(unexplored.keys.first())
            val basin = mutableSetOf<Pair<Int, Int>>()
            while (toExplore.isNotEmpty()) {
                val at = toExplore.removeFirst()
                unexplored.remove(at)
                basin.add(at)
                toExplore.addAll(neighbors(unexplored, at))
            }
            basinSizes.add(basin.size)
        }
        return basinSizes.sortedDescending().take(3).reduce(Int::times)
    }

    val testInput = parseInput("aoc2021/Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = parseInput("aoc2021/Day09")
    println(part1(input))
    println(part2(input))
}
