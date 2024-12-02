package aoc2024

import AdventDay
import TestCase
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day02 : AdventDay<List<List<Int>>, Int, Int>(2024, 2) {

    override fun parseInput(stringInput: List<String>) = stringInput.map { s ->
        s.split(" ").map { it.toInt() }
    }

    private fun List<Int>.safe(): Boolean = asSequence().windowed(2)
        .map { (a, b) -> if ((a - b).absoluteValue < 4) (a - b).sign else 0 }
        .windowed(2).all { (a, b) -> a != 0 && a == b }

    override fun part1(input: List<List<Int>>, testArg: Any?): Int {
        return input.count { it.safe() }
    }

    override fun part2(input: List<List<Int>>, testArg: Any?): Int {
        return input.count { level ->
            level.safe() || level.indices.any { i -> level.filterIndexed { idx, _ -> idx != i }.safe() }
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 2))

    override fun testCases2() = listOf(TestCase(getTestInput(), 4))

}

fun main() {
    Day02().main()
}
