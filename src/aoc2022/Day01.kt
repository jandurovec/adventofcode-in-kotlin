package aoc2022

import readInput

fun main() {
    fun parseInput(input: List<String>): List<List<Int>> {
        val result = mutableListOf<List<Int>>()
        var cur = mutableListOf<Int>()
        result.add(cur)
        input.map { it.toIntOrNull() }.forEach {
            if (it == null) {
                cur = mutableListOf()
                result.add(cur)
            } else {
                cur.add(it)
            }
        }
        return result
    }

    fun part1(input: List<List<Int>>) = input.maxOf { it.sum() }
    fun part2(input: List<List<Int>>) = input.map { it.sum() }.sortedDescending().take(3).sum()

    val testInput = parseInput(readInput(2022, 1, "test"))
    check(part1(testInput) == 24_000)
    check(part2(testInput) == 45_000)

    val input = parseInput(readInput(2022, 1))
    println(part1(input))
    println(part2(input))
}
