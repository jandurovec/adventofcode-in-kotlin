package aoc2025

import TestCase
import UnparsedDay

class Day03 : UnparsedDay<Long, Long>(2025, 3) {

    fun String.joltage(batteryCount: Int): Long {
        var s = this
        return buildString {
            for (i in batteryCount - 1 downTo 0) {
                val digit = s.take(s.length - i).maxBy { it.digitToInt() }
                s = s.drop(s.indexOf(digit) + 1)
                append(digit)
            }

        }.toLong()
    }

    override fun part1(input: List<String>, testArg: Any?) = input.sumOf { it.joltage(2) }

    override fun part2(input: List<String>, testArg: Any?) = input.sumOf { it.joltage(12) }

    override fun testCases1() = listOf(TestCase(getTestInput(), 357L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 3121910778619))

}

fun main() {
    Day03().main()
}
