package aoc2025

import TestCase
import UnparsedDay


class Day06 : UnparsedDay<Long, Long>(2025, 6) {

    override fun part1(input: List<String>, testArg: Any?): Long {
        val parsedInput = input.map { it.trim().split(Regex("\\s+")) }
        return (0..parsedInput[parsedInput.lastIndex].lastIndex).sumOf { idx ->
            (0..<parsedInput.lastIndex).map { row -> parsedInput[row][idx].toLong() }.reduce(
                when (parsedInput[parsedInput.lastIndex][idx]) {
                    "+" -> Long::plus
                    "*" -> Long::times
                    else -> error("Unsupported operation ${parsedInput[parsedInput.lastIndex][idx]}")
                }
            )
        }
    }

    override fun part2(input: List<String>, testArg: Any?): Long {
        var sum = 0L
        val curList = mutableListOf<Long>()
        for (idx in input.maxOf { it.lastIndex } downTo 0) {
            val digits = (0..<input.lastIndex).map { if (input[it].length > idx) input[it][idx] else ' ' }
            if (digits.any { it != ' ' }) {
                curList += digits.joinToString("").trim().toLong()
            }
            if (input.last().length > idx) {
                val op = input.last()[idx]
                if (op != ' ') {
                    sum += curList.reduce(
                        when (op) {
                            '+' -> Long::plus
                            '*' -> Long::times
                            else -> error("Unsupported operation $op")
                        }
                    )
                    curList.clear()
                }
            }
        }
        return sum
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 4277556L))
    override fun testCases2() = listOf(TestCase(getTestInput(), 3263827L))
}

fun main() {
    Day06().main()
}
