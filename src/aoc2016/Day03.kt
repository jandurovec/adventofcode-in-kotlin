package aoc2016

import AdventDay
import TestCase

class Day03 : AdventDay<List<List<Int>>, Int, Int>(2016, 3) {
    override fun parseInput(stringInput: List<String>) = stringInput.map { s ->
        s.trim().split(Regex(" +")).map { it.toInt() }
    }

    private fun countTriangles(input: List<List<Int>>) = input.count {
        it[0] + it[1] > it[2] && it[0] + it[2] > it[1] && it[1] + it[2] > it[0]
    }

    override fun part1(input: List<List<Int>>, testArg: Any?) = countTriangles(input)

    override fun part2(input: List<List<Int>>, testArg: Any?) = countTriangles(input.chunked(3).flatMap {
        listOf(
            listOf(it[0][0], it[1][0], it[2][0]),
            listOf(it[0][1], it[1][1], it[2][1]),
            listOf(it[0][2], it[1][2], it[2][2]),
        )
    })

    override fun testCases1() = listOf(TestCase(listOf(listOf(5, 10, 25)), 0))
}

fun main() {
    Day03().main()
}
