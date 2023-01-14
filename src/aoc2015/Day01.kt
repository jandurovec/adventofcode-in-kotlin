package aoc2015

import SingleStringDay
import TestCase

class Day01 : SingleStringDay<Int, Int>(2015, 1) {
    private fun preProcess(input: String) = input.map { if (it == '(') 1 else -1 }
    override fun part1(input: String, testArg: Any?) = preProcess(input).sum()
    override fun part2(input: String, testArg: Any?) =
        preProcess(input).asSequence().runningFold(0, Int::plus).indexOfFirst { it == -1 }

    override fun testCases1() = listOf(
        TestCase("(())", 0),
        TestCase("()()", 0),
        TestCase("(((", 3),
        TestCase("(()(()(", 3),
        TestCase(")())())", -3)
    )

    override fun testCases2() = listOf(TestCase("()())", 5))
}

fun main() {
    Day01().main()
}
