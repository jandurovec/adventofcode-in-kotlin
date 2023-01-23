package aoc2015

import SingleStringDay
import TestCase
import md5

class Day04 : SingleStringDay<Int, Int>(2015, 4) {
    private fun mine(secret: String, zeroes: Int) = "0".repeat(zeroes).let { prefix ->
        generateSequence(1) { it + 1 }.first {
            (secret + it.toString()).md5().startsWith(prefix)
        }
    }

    override fun part1(input: String, testArg: Any?) = mine(input, 5)
    override fun part2(input: String, testArg: Any?) = mine(input, 6)

    override fun testCases1() = listOf(TestCase("abcdef", 609043), TestCase("pqrstuv", 1048970))
}

fun main() {
    Day04().main()
}
