package aoc2017

import AdventDay
import TestCase

class Day17 : AdventDay<Int, Int, Int>(2017, 17) {
    override fun parseInput(stringInput: List<String>) = stringInput.first().toInt()

    override fun part1(input: Int, testArg: Any?): Int {
        var pos = 0
        val data = mutableListOf(0)

        repeat(2017) {
            val newPos = (pos + input + 1).mod(data.size)
            data.add(newPos, data.size)
            pos = newPos
        }
        return data[(pos + 1).mod(data.size)]
    }

    override fun part2(input: Int, testArg: Any?): Int {
        var pos = 0
        var next = 0

        (1 until 50_000_000).forEach {
            val newPos = (pos + input).mod(it)
            if (newPos == 0) {
                next = it
            }
            pos = newPos + 1
        }
        return next
    }

    override fun testCases1() = listOf(TestCase(3, 638))
}

fun main() {
    Day17().main()
}
