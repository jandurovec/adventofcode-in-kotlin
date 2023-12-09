package aoc2023

import AdventDay
import TestCase


class Day09 : AdventDay<List<List<Long>>, Long, Long>(2023, 9) {
    override fun parseInput(stringInput: List<String>) = stringInput.map { line ->
        line.split(" ").map { it.toLong() }
    }

    fun solve(input: List<List<Long>>, select: (List<Long>) -> Long, reduction: (Long, Long) -> Long) =
        input.sumOf { seq ->
            val numbersOfInterest = mutableListOf(select(seq))
            var cur = seq
            while (cur.any { it != 0L }) {
                cur = cur.windowed(2).map { (a, b) -> b - a }
                numbersOfInterest.add(select(cur))
            }
            numbersOfInterest.reduceRight(reduction)
        }

    override fun part1(input: List<List<Long>>, testArg: Any?) = solve(input, { it.last() }, Long::plus)

    override fun part2(input: List<List<Long>>, testArg: Any?) = solve(input, { it.first() }, Long::minus)

    override fun testCases1() = listOf(TestCase(getTestInput(), 114L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 2L))
}

fun main() {
    Day09().main()
}
