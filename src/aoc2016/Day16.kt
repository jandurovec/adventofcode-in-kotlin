package aoc2016

import SingleStringDay
import TestCase


class Day16 : SingleStringDay<String, String>(2016, 16) {
    private fun String.checksum() = chunked(2).map { if (it[0] == it[1]) '1' else '0' }.joinToString("")

    override fun part1(input: String, testArg: Any?): String {
        val diskSize = if (testArg is Int) testArg else 272
        var data = input
        while (data.length < diskSize) {
            data = buildString {
                append(data)
                append('0')
                data.reversed().forEach { append(if (it == '0') '1' else '0') }
            }
        }
        var checksum = data.substring(0, diskSize).checksum()
        while (checksum.length % 2 == 0) {
            checksum = checksum.checksum()
        }
        return checksum
    }

    override fun part2(input: String, testArg: Any?) = part1(input, 35651584)

    override fun testCases1() = listOf(TestCase("10000", "01100", 20))
}

fun main() {
    Day16().main()
}
