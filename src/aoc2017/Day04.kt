package aoc2017

import TestCase
import UnparsedDay

class Day04 : UnparsedDay<Int, Int>(2017, 4) {

    private fun <T> String.isValidPassphrase(keySelector: (String) -> T) =
        split(" ").groupingBy(keySelector).eachCount().all { it.value == 1 }

    override fun part1(input: List<String>, testArg: Any?) = input.count { phrase -> phrase.isValidPassphrase { it } }

    override fun part2(input: List<String>, testArg: Any?) =
        input.count { phrase -> phrase.isValidPassphrase { w -> w.groupingBy { it }.eachCount() } }

    override fun testCases1() = listOf(
        TestCase(listOf("aa bb cc dd ee"), 1),
        TestCase(listOf("aa bb cc dd aa"), 0),
        TestCase(listOf("aa bb cc dd aaa"), 1)
    )

    override fun testCases2() = listOf(
        TestCase(listOf("abcde fghij"), 1),
        TestCase(listOf("abcde xyz ecdab"), 0),
        TestCase(listOf("a ab abc abd abf abj"), 1),
        TestCase(listOf("iiii oiii ooii oooi oooo"), 1),
        TestCase(listOf("oiii ioii iioi iiio"), 0)
    )
}

fun main() {
    Day04().main()
}
