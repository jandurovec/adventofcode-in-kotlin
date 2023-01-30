package aoc2017

import Point
import TestCase
import UnparsedDay

class Day19 : UnparsedDay<String, Int>(2017, 19) {
    companion object {
        val DIRECTIONS = Point(0, 0).neighbors(false).toSet()
    }

    private fun List<String>.crawl() = sequence {
        val data = toTypedArray()
        var pos = Point(data.first().indexOf('|'), 0)
        var direction = Point(0, 1)
        var steps = 1
        while (true) {
            pos += direction
            if (pos.y !in data.indices || pos.x !in data[pos.y].indices || data[pos.y][pos.x] == ' ') {
                break
            }
            steps++
            val c = data[pos.y][pos.x]
            if (c == '+') {
                direction = DIRECTIONS.minus(direction).minus(-direction)
                    .first { d -> (pos + d).let { it.y in data.indices && it.x in data[it.y].indices && data[it.y][it.x] != ' ' } }
            } else if (c.isLetter()) {
                yield(steps to c)
            }
        }
    }

    override fun part1(input: List<String>, testArg: Any?) = input.crawl().map { it.second }.joinToString("")
    override fun part2(input: List<String>, testArg: Any?) = input.crawl().last().first

    override fun testCases1() = listOf(TestCase(getTestInput(), "ABCDEF"))
    override fun testCases2() = listOf(TestCase(getTestInput(), 38))
}

fun main() {
    Day19().main()
}
