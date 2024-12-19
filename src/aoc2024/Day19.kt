package aoc2024

import AdventDay
import TestCase
import split


class Day19 : AdventDay<Day19.Onsen, Int, Long>(2024, 19) {

    data class Onsen(val towels: List<String>, val designs: List<String>)

    override fun parseInput(stringInput: List<String>): Onsen {
        val (towels, patterns) = stringInput.split()
        return Onsen(towels.first().split(", "), patterns)
    }

    override fun part1(input: Onsen, testArg: Any?): Int {
        val designCache = mutableMapOf("" to true)
        fun String.isPossible(): Boolean = designCache.getOrPut(this) {
            input.towels.any { startsWith(it) && substring(it.length).isPossible() }
        }
        return input.designs.count { it.isPossible() }
    }

    override fun part2(input: Onsen, testArg: Any?): Long {
        val designCache = mutableMapOf("" to 1L)
        fun String.possibilities(): Long = designCache.getOrPut(this) {
            input.towels.filter { startsWith(it) }.sumOf {
                substring(it.length).possibilities()
            }
        }
        return input.designs.sumOf { it.possibilities() }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 6))

    override fun testCases2() = listOf(TestCase(getTestInput(), 16L))

}

fun main() {
    Day19().main()
}
