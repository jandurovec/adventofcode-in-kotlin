package aoc2021

import readInput
import toIntList
import kotlin.math.absoluteValue

fun main() {
    fun parseInput(input: List<String>) = input.first().toIntList()

    fun solve(crabs: List<Int>, fuel: (Int) -> Int) =
        (crabs.minOf { it }..crabs.maxOf { it }).minOf { pos -> crabs.sumOf { fuel((it - pos).absoluteValue) } }

    fun part1(crabs: List<Int>) = solve(crabs) { it }

    fun part2(crabs: List<Int>) = solve(crabs) { it * (it + 1) / 2 }

    val testInput = parseInput(readInput(2021, 7, "test"))
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = parseInput(readInput(2021, 7))
    println(part1(input))
    println(part2(input))
}
