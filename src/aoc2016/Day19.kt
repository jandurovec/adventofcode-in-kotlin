package aoc2016

import AdventDay
import TestCase

class Day19 : AdventDay<Int, Int, Int>(2016, 19) {
    override fun parseInput(stringInput: List<String>) = stringInput.first().toInt()

    override fun part1(input: Int, testArg: Any?): Int {
        var first = 1
        var step = 2
        var count = input
        while (count > 1) {
            if (count % 2 == 1) {
                first += step
            }
            count /= 2
            step *= 2
        }
        return first
    }

    override fun part2(input: Int, testArg: Any?): Int {
        var toEliminate = input / 2
        val eliminated = mutableSetOf<Int>()
        while (eliminated.size != input - 1) {
            eliminated.add(toEliminate)
            repeat(if (((input - eliminated.size) % 2) == 0) 2 else 1) {
                do {
                    toEliminate = (toEliminate + 1) % input
                } while (eliminated.contains(toEliminate))
            }
        }
        return toEliminate + 1
    }

    override fun testCases1() = listOf(TestCase(5, 3))
    override fun testCases2() = listOf(
        TestCase(5, 2),
        TestCase(20, 13),
    )
}

fun main() {
    Day19().main()
}
