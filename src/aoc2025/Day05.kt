package aoc2025

import AdventDay
import TestCase
import split
import union


class Day05 : AdventDay<Pair<List<LongRange>, List<Long>>, Int, Long>(2025, 5) {
    override fun parseInput(stringInput: List<String>) = stringInput.split().let { (ranges, ids) ->
        ranges.map { s ->
            s.split("-").map { it.toLong() }.let { it[0]..it[1] }
        } to ids.map { it.toLong() }
    }

    override fun part1(input: Pair<List<LongRange>, List<Long>>, testArg: Any?) =
        input.let { (ranges, ids) -> ids.count { id -> ranges.any { id in it } } }

    override fun part2(input: Pair<List<LongRange>, List<Long>>, testArg: Any?) =
        input.first.union().sumOf { it.last - it.first + 1 }


    override fun testCases1() = listOf(TestCase(getTestInput(), 3))
    override fun testCases2() = listOf(TestCase(getTestInput(), 14L))
}

fun main() {
    Day05().main()
}
