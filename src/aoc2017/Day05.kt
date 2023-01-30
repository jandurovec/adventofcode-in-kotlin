package aoc2017

import IntListDay
import TestCase

class Day05 : IntListDay<Int, Int>(2017, 5) {
    private fun steps(jumps: List<Int>, offsetIncrement: (Int) -> Int): Int {
        val instructions = jumps.toTypedArray()
        var ip = 0
        var steps = 0
        while (ip in instructions.indices) {
            val offset = instructions[ip]
            instructions[ip] += offsetIncrement(offset)
            ip += offset
            steps++
        }
        return steps
    }

    override fun part1(input: List<Int>, testArg: Any?) = steps(input) { 1 }
    override fun part2(input: List<Int>, testArg: Any?) = steps(input) { if (it >= 3) -1 else 1 }

    override fun testCases1() = listOf(TestCase(getTestInput(), 5))
    override fun testCases2() = listOf(TestCase(getTestInput(), 10))

}

fun main() {
    Day05().main()
}
