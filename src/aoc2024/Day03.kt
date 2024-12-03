package aoc2024

import TestCase
import UnparsedDay


class Day03 : UnparsedDay<Int, Int>(2024, 3) {

    override fun part1(input: List<String>, testArg: Any?) =
        """mul\((\d+),(\d+)\)""".toRegex().findAll(input.joinToString(""))
            .map { it.groupValues }.sumOf { (_, a, b) -> a.toInt() * b.toInt() }

    override fun part2(input: List<String>, testArg: Any?): Int {
        var result = 0
        var enabled = true
        """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex().findAll(input.joinToString("")).forEach {
            val whole = it.value
            if (whole == "do()") {
                enabled = true
            } else if (whole == "don't()") {
                enabled = false
            } else if (enabled) {
                val (_, a, b) = it.groupValues
                result += a.toInt() * b.toInt()
            }
        }
        return result
    }

    override fun testCases1() =
        listOf(TestCase(listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"), 161))

    override fun testCases2() =
        listOf(TestCase(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"), 48))

}

fun main() {
    Day03().main()
}
