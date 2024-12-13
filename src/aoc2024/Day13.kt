package aoc2024

import AdventDay
import TestCase
import split


class Day13 : AdventDay<List<Day13.ClawMachine>, Long, Long>(2024, 13) {

    data class ClawMachine(val ax: Long, val ay: Long, val bx: Long, val by: Long, val px: Long, val py: Long) {
        fun tokensToWin(): Long {
            val b = (ax * py - ay * px) / (ax * by - bx * ay)
            val a = (px - bx * b) / ax
            return if (a * ax + b * bx == px && a * ay + b * by == py) 3 * a + b else 0
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.split()
        .map { lines ->
            lines.map { line ->
                """.*: X[+=](\d+), Y[+=](\d+)""".toRegex().matchEntire(line)!!.groupValues.drop(1)
                    .map { it.toLong() }.let { (x, y) -> x to y }
            }.let { (a, b, p) -> ClawMachine(a.first, a.second, b.first, b.second, p.first, p.second) }
        }

    override fun part1(input: List<ClawMachine>, testArg: Any?) = input.sumOf { it.tokensToWin() }

    override fun part2(input: List<ClawMachine>, testArg: Any?) =
        input.map { ClawMachine(it.ax, it.ay, it.bx, it.by, it.px + 10000000000000, it.py + 10000000000000) }
            .sumOf { it.tokensToWin() }

    override fun testCases1() = listOf(TestCase(getTestInput(), 480L))
}

fun main() {
    Day13().main()
}
