package aoc2015

import TestCase
import UnparsedDay

class Day08 : UnparsedDay<Int, Int>(2015, 8) {

    override fun part1(input: List<String>, testArg: Any?) = input.sumOf {
        var escapes = 2
        var i = 1
        while (i < it.lastIndex) {
            if (it[i] == '\\') {
                i++
                escapes++
                if (it[i] == 'x') {
                    escapes += 2
                    i += 2
                }
            }
            i++
        }
        escapes
    }

    override fun part2(input: List<String>, testArg: Any?) = input.sumOf { s ->
        2 + s.count { it == '\\' || it == '"' }
    }

    override fun testCases1() = listOf(TestCase(getInput("test"), 12))
    override fun testCases2() = listOf(TestCase(getInput("test"), 19))
}

fun main() {
    Day08().main()
}
