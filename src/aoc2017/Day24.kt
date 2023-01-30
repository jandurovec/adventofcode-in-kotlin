package aoc2017

import AdventDay
import TestCase

class Day24 : AdventDay<List<Pair<Int, Int>>, Int, Int>(2017, 24) {
    override fun parseInput(stringInput: List<String>) =
        stringInput.map { s -> s.split("/").map { it.toInt() }.let { (a, b) -> a to b } }

    private fun Set<Pair<Int, Int>>.bridges(
        connector: Int = 0,
        prefix: List<Pair<Int, Int>> = emptyList()
    ): Sequence<List<Pair<Int, Int>>> =
        sequence {
            if (prefix.isNotEmpty()) {
                yield(prefix)
            }
            filter { it.first == connector || it.second == connector }.forEach { next ->
                yieldAll(
                    minus(next).bridges(
                        if (next.first == connector) next.second else next.first,
                        prefix + next
                    )
                )
            }
        }

    private fun List<Pair<Int, Int>>.strength() = sumOf { it.first + it.second }

    override fun part1(input: List<Pair<Int, Int>>, testArg: Any?) =
        input.toSet().bridges().maxOf { it.strength() }

    override fun part2(input: List<Pair<Int, Int>>, testArg: Any?) =
        input.toSet().bridges()
            .maxWith { b1, b2 -> (b1.size - b2.size).let { if (it == 0) b1.strength() - b2.strength() else it } }
            .strength()

    override fun testCases1() = listOf(TestCase(getTestInput(), 31))
    override fun testCases2() = listOf(TestCase(getTestInput(), 19))
}

fun main() {
    Day24().main()
}
