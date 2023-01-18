package aoc2016

import SingleStringDay
import TestCase

class Day18 : SingleStringDay<Int, Int>(2016, 18) {
    private fun nextRow(row: String) = buildString {
        row.indices.map { i ->
            val left = if (i > 0) row[i - 1] == '^' else false
            val center = row[i] == '^'
            val right = if (i < row.lastIndex) row[i + 1] == '^' else false
            (left && center && !right) || (center && right && !left) || (left && !center && !right) || (!left && !center && right)
        }.forEach { append(if (it) '^' else '.') }
    }

    private fun safeTiles(firstRow: String, rows: Int) =
        generateSequence(firstRow) { nextRow(it) }.take(rows).sumOf { row ->
            row.count { it == '.' }
        }

    override fun part1(input: String, testArg: Any?) = safeTiles(input, if (testArg is Int) testArg else 40)
    override fun part2(input: String, testArg: Any?) = safeTiles(input, 40000)

    override fun testCases1() = listOf(
        TestCase(".^^.^.^^^^", 38, 10)
    )
}

fun main() {
    Day18().main()
}
