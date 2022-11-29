package aoc2021

import readInputAsIntList

fun main() {
    fun part1(input: List<Int>) = input.windowed(2, 1).sumOf { if (it[1] > it[0]) 1L else 0L }

    fun part2(input: List<Int>) = part1(input.windowed(3, 1).map { it.sum() })

    val testInput = readInputAsIntList("aoc2021/Day01_test")
    check(part1(testInput) == 7L)
    check(part2(testInput) == 5L)

    val input = readInputAsIntList("aoc2021/Day01")
    println(part1(input))
    println(part2(input))
}
