package aoc2015

import SingleStringDay
import TestCase

class Day11 : SingleStringDay<String, String>(2015, 11) {
    override fun part1(input: String, testArg: Any?): String {
        fun StringBuilder.increment() {
            var pos = 0
            var carry: Boolean
            do {
                carry = false
                if (pos == length) {
                    append('a')
                } else if (this[pos] == 'z') {
                    this[pos] = 'a'
                    carry = true
                    pos++
                } else {
                    this[pos]++
                }
            } while (carry)
            Regex("[iop]").findAll(this).lastOrNull()?.let {
                val p = it.range.first
                for (i in 0 until p) {
                    this[i] = 'a'
                }
                this[p]++
            }
        }

        fun StringBuilder.checkStraight() = windowedSequence(2).map { it[0] == it[1] + 1 }
            .windowed(2).map { it.reduce(Boolean::and) }.any { it }

        fun StringBuilder.checkPairs() =
            windowedSequence(2).mapNotNull { if (it[0] == it[1]) it[0] else null }.toSet().size > 1

        val password = StringBuilder(input.reversed())
        do {
            password.increment()
        } while (!password.checkStraight() || !password.checkPairs())
        return password.reversed().toString()
    }

    override fun part2(input: String, testArg: Any?) = part1(part1(input, null), null)

    override fun testCases1() = listOf(
        TestCase("abcdefgh", "abcdffaa"),
        TestCase("ghijklmn", "ghjaabcc")
    )
}

fun main() {
    Day11().main()
}
