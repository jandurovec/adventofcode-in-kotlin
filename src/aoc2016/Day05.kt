package aoc2016

import SingleStringDay
import TestCase
import md5

class Day05 : SingleStringDay<String, String>(2016, 5) {
    private fun hashes(key: String) = generateSequence(0) { it + 1 }
        .map { (key + it).md5() }
        .filter { it.length <= 27 }
        .map { it.padStart(27, '0') }

    override fun part1(input: String, testArg: Any?) = hashes(input).take(8).map { it[0] }.joinToString("")

    override fun part2(input: String, testArg: Any?): String {
        val pass = arrayOfNulls<Char>(8)
        val hashes = hashes(input).iterator()
        while (pass.any { it == null }) {
            val hash = hashes.next()
            if (hash[0] in '0'..'7') {
                val idx = hash[0] - '0'
                if (pass[idx] == null) {
                    pass[idx] = hash[1]
                }
            }
        }
        return pass.joinToString("")
    }


    override fun testCases1() = listOf(TestCase("abc", "18f47a30"))
    override fun testCases2() = listOf(TestCase("abc", "05ace8e3"))
}

fun main() {
    Day05().main()
}
