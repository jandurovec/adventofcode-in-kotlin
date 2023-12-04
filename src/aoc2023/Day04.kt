package aoc2023

import AdventDay
import TestCase

class Day04 : AdventDay<List<Int>, Int, Int>(2023, 4) {

    override fun parseInput(stringInput: List<String>): List<Int> = stringInput.map { line ->
        val (win, rest) = line.split(Regex(": +"))[1]
            .split(Regex(" \\| +")).map { s -> s.split(Regex(" +")).map { it.toInt() }.toSet() }
        rest.filter { win.contains(it) }.size
    }

    override fun part1(input: List<Int>, testArg: Any?) = input.sumOf {
        if (it == 0) 0 else 1 shl (it - 1)
    }

    override fun part2(input: List<Int>, testArg: Any?): Int {
        val wins = input.toIntArray()
        val toExplore = ArrayDeque<Int>()
        toExplore.addAll(wins.indices)
        var totalPads = wins.size
        while (toExplore.isNotEmpty()) {
            val i = toExplore.removeFirst()
            (i + 1..minOf(i + wins[i], wins.lastIndex)).forEach {
                toExplore.addLast(it)
                totalPads++
            }
        }
        return totalPads
    }

    override fun testCases1() = listOf(
        TestCase(getTestInput(), 13),
    )

    override fun testCases2() = listOf(
        TestCase(getTestInput(), 30),
    )

}

fun main() {
    Day04().main()
}
