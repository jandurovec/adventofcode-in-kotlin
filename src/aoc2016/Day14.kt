package aoc2016

import SingleStringDay
import TestCase
import md5

class Day14 : SingleStringDay<Int, Int>(2016, 14) {
    private fun hashes(salt: String, hashCount: Int) = generateSequence(0) { it + 1 }
        .map { salt + it }
        .map { s -> (1..hashCount).fold(s) { hash, _ -> hash.md5() } }

    private fun solve(salt: String, hashCount: Int = 1) =
        hashes(salt, hashCount).windowed(1001).mapIndexed { i, h -> i to h }.filter { (_, h) ->
            val chr = h.first().windowed(3).map { it.toCharArray().toSet() }.firstOrNull { it.size == 1 }?.first()
            chr != null && h.drop(1).any { it.contains(chr.toString().repeat(5)) }
        }.drop(63).first().first

    override fun part1(input: String, testArg: Any?) = solve(input)
    override fun part2(input: String, testArg: Any?) = solve(input, 2017)

    override fun testCases1() = listOf(TestCase("abc", 22728))
    override fun testCases2() = listOf(TestCase("abc", 22551))
}

fun main() {
    Day14().main()
}
