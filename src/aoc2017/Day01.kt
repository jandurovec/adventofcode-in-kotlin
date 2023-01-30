package aoc2017

import SingleStringDay
import TestCase

class Day01 : SingleStringDay<Int, Int>(2017, 1) {
    private fun solve(captcha: String, offset: Int) =
        captcha.mapIndexed { i, c -> if (captcha[(i + offset) % captcha.length] == c) c.digitToInt() else 0 }.sum()

    override fun part1(input: String, testArg: Any?) = solve(input, 1)

    override fun part2(input: String, testArg: Any?) = solve(input, input.length / 2)

    override fun testCases1() = listOf(
        TestCase("1122", 3),
        TestCase("1111", 4),
        TestCase("1234", 0),
        TestCase("91212129", 9)
    )

    override fun testCases2() = listOf(
        TestCase("1212", 6),
        TestCase("1221", 0),
        TestCase("123425", 4),
        TestCase("123123", 12),
        TestCase("12131415", 4)
    )

}

fun main() {
    Day01().main()
}
