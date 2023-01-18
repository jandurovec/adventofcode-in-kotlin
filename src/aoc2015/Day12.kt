package aoc2015

import SingleStringDay
import TestCase

class Day12 : SingleStringDay<Int, Int>(2015, 12) {
    override fun part1(input: String, testArg: Any?) =
        Regex("-?[0-9]+").findAll(input).map { it.value.toInt() }.sum()

    override fun part2(input: String, testArg: Any?): Int {
        class JSON(private val s: String) {
            private var pos = 0
            private fun parseString(): Any =
                s.substring(pos + 1, s.indexOf('"', pos + 1)).also { pos += it.length + 2 }

            private fun parseNumber(): Any =
                s.drop(pos).takeWhile { it.isDigit() || it == '-' }.let {
                    pos += it.length
                    it.toInt()
                }

            private fun parseArray(): Any = generateSequence {
                if (s[pos++] != ']') parse() else null
            }.toList()

            private fun parseObject(): Any = generateSequence {
                if (s[pos++] != '}') parseString().also { pos++ } to parse() else null
            }.toMap()

            private fun parse() = when (s[pos]) {
                '"' -> parseString()
                '[' -> parseArray()
                '{' -> parseObject()
                else -> parseNumber()
            }

            val data = parse()

            private fun sumNumbers(it: Any?): Int = when (it) {
                is Int -> it
                is List<*> -> it.sumOf(::sumNumbers)
                is Map<*, *> -> if (it.values.contains("red")) 0 else it.values.sumOf(::sumNumbers)
                else -> 0
            }

            fun sumNumbers() = sumNumbers(data)
        }
        return JSON(input).sumNumbers()
    }

    override fun testCases1() = listOf(
        TestCase("""[1,2,3]""", 6),
        TestCase("""{"a":2,"b":4}""", 6),
        TestCase("""[[[3]]]""", 3),
        TestCase("""{"a":{"b":4},"c":-1}""", 3),
        TestCase("""{"a":[-1,1]}""", 0),
        TestCase("""[-1,{"a":1}]""", 0),
        TestCase("""[]""", 0),
        TestCase("""{}""", 0)
    )

    override fun testCases2() = listOf(
        TestCase("""[1,2,3]""", 6),
        TestCase("""[1,{"c":"red","b":2},3]""", 4),
        TestCase("""{"d":"red","e":[1,2,3,4],"f":5}""", 0),
        TestCase("""[1,"red",5]""", 6),
    )

}

fun main() {
    Day12().main()
}
