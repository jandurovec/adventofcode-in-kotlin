package aoc2021

import readInput

fun main() {
    fun part1(input: List<Int>) = input.windowed(2, 1).sumOf { if (it[1] > it[0]) 1L else 0L }

    fun part2(input: List<Int>) = part1(input.windowed(3, 1).map { it.sum() })

    val testInput = readInput(2021, 1, "test").map(String::toInt)
    check(part1(testInput) == 7L)
    check(part2(testInput) == 5L)

    val input = readInput(2021, 1).map(String::toInt)
    println(part1(input))
    println(part2(input))
}
