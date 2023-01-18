package aoc2016

import TestCase
import UnparsedDay

class Day06 : UnparsedDay<String, String>(2016, 6) {
    private fun decode(input: List<String>, selector: (Map<Char, Int>) -> Map.Entry<Char, Int>) = input
        .fold(mutableListOf<StringBuilder>()) { acc, s ->
            for (i in 0..s.lastIndex) {
                if (i < acc.size) {
                    acc[i].append(s[i])
                } else {
                    acc.add(StringBuilder(s[i].toString()))
                }
            }
            acc
        }.map { chars -> chars.groupingBy { it }.eachCount() }.map { freq -> selector.invoke(freq).key }
        .joinToString("")

    override fun part1(input: List<String>, testArg: Any?) = decode(input) { m -> m.maxBy { it.value } }
    override fun part2(input: List<String>, testArg: Any?) = decode(input) { m -> m.minBy { it.value } }

    override fun testCases1() = listOf(TestCase(getTestInput(), "easter"))
    override fun testCases2() = listOf(TestCase(getTestInput(), "advent"))
}

fun main() {
    Day06().main()
}
