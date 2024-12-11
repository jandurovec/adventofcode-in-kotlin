package aoc2024

import AdventDay
import TestCase


class Day11 : AdventDay<List<Long>, Long, Long>(2024, 11) {

    override fun parseInput(stringInput: List<String>) = stringInput.first().split(" ").map { it.toLong() }

    private fun List<Long>.step(): List<Long> = flatMap { stone ->
        if (stone == 0L) {
            listOf(1)
        } else {
            val s = stone.toString()
            if (s.length % 2 == 0) {
                s.chunked(s.length / 2).map { it.toLong() }
            } else {
                listOf(2024 * stone)
            }
        }
    }

    private val cache = mutableMapOf<Pair<List<Long>, Int>, Long>() // (list, in X steps) -> length
    private fun stoneCount(stones: List<Long>, steps: Int): Long {
        if (steps == 0) {
            return stones.size.toLong()
        }
        return cache.getOrPut(stones to steps) {
            stones.step().sumOf { stoneCount(listOf(it), steps - 1) }
        }
    }

    override fun part1(input: List<Long>, testArg: Any?) = stoneCount(input, 25)

    override fun part2(input: List<Long>, testArg: Any?) = stoneCount(input, 75)

    override fun testCases1() = listOf(TestCase(listOf(125L, 17L), 55312L))

    override fun testCases2() = emptyList<TestCase<List<Long>, Long>>()

}

fun main() {
    Day11().main()
}
