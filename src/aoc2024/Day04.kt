package aoc2024

import TestCase
import UnparsedDay


class Day04 : UnparsedDay<Int, Int>(2024, 4) {

    private fun List<String>.check(needle: String, row: Int, col: Int, dRow: Int, dCol: Int) = needle
        .mapIndexed { offset, expected -> row + offset * dRow to col + offset * dCol to expected }
        .all { (pos, expected) ->
            val (r, c) = pos
            r >= 0 && c >= 0 && r < size && c < this[r].length && this[r][c] == expected
        }

    private fun List<String>.xmas(row: Int, col: Int, dRow: Int, dCol: Int) = check("XMAS", row, col, dRow, dCol)
    private fun List<String>.mas(row: Int, col: Int, dRow: Int, dCol: Int) = check("MAS", row, col, dRow, dCol)

    override fun part1(input: List<String>, testArg: Any?) = input.mapIndexed { row, s -> row to s }.sumOf { (row, s) ->
        s.indices.sumOf { col ->
            listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
                .count { input.xmas(row, col, it.first, it.second) }
        }
    }

    override fun part2(input: List<String>, testArg: Any?) = input.mapIndexed { r, row -> r to row }.sumOf { (r, row) ->
        row.indices.count { c ->
            (input.mas(r - 1, c - 1, 1, 1) || input.mas(r + 1, c + 1, -1, -1)) &&
                    (input.mas(r - 1, c + 1, 1, -1) || input.mas(r + 1, c - 1, -1, 1))
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 18))

    override fun testCases2() = listOf(TestCase(getTestInput(), 9))

}

fun main() {
    Day04().main()
}
