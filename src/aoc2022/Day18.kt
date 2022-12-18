package aoc2022

import readInput

fun main() {

    val directions = listOf(
        Triple(0, 0, -1), Triple(0, 0, 1),
        Triple(0, -1, 0), Triple(0, 1, 0),
        Triple(-1, 0, 0), Triple(1, 0, 0)
    )

    fun parseInput(input: List<String>) =
        input.map { l -> l.split(',').map { it.toInt() } }.map { (x, y, z) -> Triple(x, y, z) }.toSet()

    fun Set<Triple<Int, Int, Int>>.adjacentFree(pos: Triple<Int, Int, Int>) = directions.mapNotNull { step ->
        val candidate = Triple(
            pos.first + step.first,
            pos.second + step.second,
            pos.third + step.third
        )
        if (this.contains(candidate)) null else candidate
    }

    fun part1(input: List<String>) = parseInput(input).let { lava ->
        lava.sumOf { cube -> lava.adjacentFree(cube).size }
    }

    fun part2(input: List<String>) = parseInput(input).let { lava ->
        val xRange = lava.minOf { it.first - 1 }..lava.maxOf { it.first + 1 }
        val yRange = lava.minOf { it.second - 1 }..lava.maxOf { it.second + 1 }
        val zRange = lava.minOf { it.third - 1 }..lava.maxOf { it.third + 1 }

        val toExplore = mutableListOf(Triple(xRange.first, yRange.first, zRange.first))
        val visited = mutableSetOf(toExplore.first())
        var lavaSurface = 0
        while (toExplore.isNotEmpty()) {
            val current = toExplore.removeFirst()
            val next = lava.adjacentFree(current)
            lavaSurface += 6 - next.size
            next.filter {
                xRange.contains(it.first)
                        && yRange.contains(it.second)
                        && zRange.contains(it.third)
                        && !visited.contains(it)
            }.forEach {
                visited += it
                toExplore += it
            }
        }
        lavaSurface
    }

    val testInput = readInput(2022, 18, "test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput(2022, 18)
    println(part1(input))
    println(part2(input))
}
