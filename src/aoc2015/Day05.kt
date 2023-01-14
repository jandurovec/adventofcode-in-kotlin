package aoc2015

import TestCase
import UnparsedDay

class Day05 : UnparsedDay<Int, Int>(2015, 5) {
    private fun String.check1a() = filter(setOf('a', 'e', 'i', 'o', 'u')::contains).length >= 3
    private fun String.check1b() = asSequence().windowed(2).any { (a, b) -> a == b }
    private fun String.check1c() = listOf("ab", "cd", "pq", "xy").none { contains(it) }

    override fun part1(input: List<String>, testArg: Any?) =
        input.count { it.check1a() && it.check1b() && it.check1c() }

    private fun String.check2a() = windowedSequence(2).withIndex().any { this.indexOf(it.value, it.index + 2) != -1 }
    private fun String.check2b() = windowedSequence(3).any { it.first() == it.last() }

    override fun part2(input: List<String>, testArg: Any?) = input.count { it.check2a() && it.check2b() }

    override fun testCases1() = listOf(
        TestCase(listOf("ugknbfddgicrmopn"), 1),
        TestCase(listOf("aaa"), 1),
        TestCase(listOf("jchzalrnumimnmhp"), 0),
        TestCase(listOf("haegwjzuvuyypxyu"), 0),
        TestCase(listOf("dvszwmarrgswjxmb"), 0)
    )

    override fun testCases2() = listOf(
        TestCase(listOf("xyxy"), 1),
        TestCase(listOf("qjhvhtzxzqqjkmpb"), 1),
        TestCase(listOf("xxyxx"), 1),
        TestCase(listOf("uurcxstgmygtbstg"), 0),
        TestCase(listOf("ieodomkazucvgmuy"), 0)
    )
}

fun main() {
    Day05().main()
}
