package aoc2021

import readInput
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    data class Coords(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    fun parseInput(name: String): List<Coords> {
        val lineRegex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
        return readInput(name).map { line ->
            val (x1, y1, x2, y2) = lineRegex.find(line)!!.groupValues.subList(1, 5).map { it.toInt() }
            Coords(x1, y1, x2, y2)
        }
    }

    fun overlaps(input: List<Coords>): Int {
        val counts = mutableMapOf<Pair<Int, Int>, Int>()
        input.forEach { (x1, y1, x2, y2) ->
            val stepX = (x2 - x1).sign
            val stepY = (y2 - y1).sign
            for (i in 0..abs(x2 - x1).coerceAtLeast(abs(y2 - y1))) {
                counts.merge(x1 + i * stepX to y1 + i * stepY, 1, Int::plus)
            }
        }
        return counts.values.count { it > 1 }
    }

    fun part1(input: List<Coords>) = overlaps(input.filter { (x1, y1, x2, y2) -> x1 == x2 || y1 == y2 })
    fun part2(input: List<Coords>) = overlaps(input)

    val testInput = parseInput("aoc2021/Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = parseInput("aoc2021/Day05")
    println(part1(input))
    println(part2(input))
}
