package aoc2024

import AdventDay
import TestCase


class Day07 : AdventDay<List<Pair<Long, List<Long>>>, Long, Long>(2024, 7) {

    override fun parseInput(stringInput: List<String>) = stringInput.map { s ->
        val segments = s.split(":? ".toRegex()).map { it.toLong() }
        segments.first() to segments.drop(1)
    }

    private fun checkOperators(expected: Long, operands: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {
        return if (operands.size == 1) {
            operands.first() == expected
        } else {
            val (a, b) = operands
            val rest = operands.drop(2)
            operators.any {
                checkOperators(expected, listOf(it(a, b)) + rest, operators)
            }
        }
    }

    private fun solve(input: List<Pair<Long, List<Long>>>, operators: List<(Long, Long) -> Long>) =
        input.filter { checkOperators(it.first, it.second, operators) }.sumOf { it.first }

    override fun part1(input: List<Pair<Long, List<Long>>>, testArg: Any?) =
        solve(input, listOf(Long::plus, Long::times))


    override fun part2(input: List<Pair<Long, List<Long>>>, testArg: Any?) =
        solve(input, listOf(Long::plus, Long::times, { a, b -> "$a$b".toLong() }))


    override fun testCases1() = listOf(TestCase(getTestInput(), 3749L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 11387L))

}

fun main() {
    Day07().main()
}
