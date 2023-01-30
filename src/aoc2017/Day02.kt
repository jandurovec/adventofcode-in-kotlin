package aoc2017

import AdventDay
import TestCase

class Day02 : AdventDay<List<List<Int>>, Int, Int>(2017, 2) {
    override fun parseInput(stringInput: List<String>) = stringInput.map { l ->
        l.split("""\s+""".toRegex()).map { it.toInt() }
    }

    override fun part1(input: List<List<Int>>, testArg: Any?) = input.sumOf { l ->
        l.max() - l.min()
    }

    override fun part2(input: List<List<Int>>, testArg: Any?) = input.sumOf(fun(line: List<Int>): Int {
        for (i in 0..line.lastIndex) {
            for (j in i + 1..line.lastIndex) {
                if (line[i] % line[j] == 0) {
                    return line[i] / line[j]
                } else if (line[j] % line[i] == 0) {
                    return line[j] / line[i]
                }
            }
        }
        return 0
    })

    override fun testCases1() = listOf(
        TestCase(getInput("test1"), 18)
    )

    override fun testCases2() = listOf(
        TestCase(getInput("test2"), 9)
    )
}

fun main() {
    Day02().main()
}
