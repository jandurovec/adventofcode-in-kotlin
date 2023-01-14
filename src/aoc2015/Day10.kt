package aoc2015

import SingleStringDay
import TestCase

class Day10 : SingleStringDay<Int, Int>(2015, 10) {
    private fun String.lookAndSay() = buildString {
        var char = this@lookAndSay.first()
        var count = 1
        var pos = 1
        while (pos < this@lookAndSay.length) {
            if (this@lookAndSay[pos] == char) {
                count++
            } else {
                append(count)
                append(char)
                char = this@lookAndSay[pos]
                count = 1
            }
            pos++
        }
        append(count)
        append(char)
    }

    private fun game(start: String, iterations: Int) = (1..iterations).fold(start) { prev, _ -> prev.lookAndSay() }.length
    override fun part1(input: String, testArg: Any?) = game(input, if (testArg is Int) testArg else 40)
    override fun part2(input: String, testArg: Any?) = game(input, 50)

    override fun testCases1() = listOf(TestCase("1", 6, 5))
}

fun main() {
    Day10().main()
}
