package aoc2023

import AdventDay
import TestCase


class Day13 : AdventDay<List<List<String>>, Int, Int>(2023, 13) {
    override fun parseInput(stringInput: List<String>) =
        stringInput.fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(line)
            }
            acc
        }

    private fun transpose(pattern: List<String>) = pattern[0].indices.map { col ->
        pattern.map { line -> line[col] }.joinToString("")
    }

    fun solve(input: List<List<String>>, scoreSymmetry: (List<String>) -> Int?) = input.sumOf { pattern ->
        scoreSymmetry(pattern)?.let { 100 * it } ?: scoreSymmetry(transpose(pattern)) ?: 0
    }

    override fun part1(input: List<List<String>>, testArg: Any?) = solve(input) { pattern ->
        (1..pattern.lastIndex).firstOrNull { above ->
            (0..minOf(above - 1, pattern.lastIndex - above)).all {
                pattern[above - 1 - it] == pattern[above + it]
            }
        }
    }

    override fun part2(input: List<List<String>>, testArg: Any?) = solve(input) { pattern ->
        (1..pattern.lastIndex).firstOrNull { above ->
            (0..minOf(above - 1, pattern.lastIndex - above)).sumOf { pos ->
                val l1 = pattern[above - 1 - pos]
                val l2 = pattern[above + pos]
                l1.indices.count { l1[it] != l2[it] }
            } == 1
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 405))

    override fun testCases2() = listOf(TestCase(getTestInput(), 400))
}

fun main() {
    Day13().main()
}
