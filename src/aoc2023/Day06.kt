package aoc2023

import AdventDay
import TestCase
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt


class Day06 : AdventDay<List<String>, Int, Int>(2023, 6) {

    override fun parseInput(stringInput: List<String>) = stringInput.map { l -> l.split(Regex(": +"))[1] }

    private fun countPossibilities(t: Long, d: Long): Int {
        // we're solving x (t - x) > d, i.e. x^2 - tx + d < 0
        val discr = t.toDouble().pow(2) - 4 * d
        return if (discr < 0) 0
        else {
            val sd = sqrt(discr)
            // x1 = (t - sd)/2; x2 = (t + sd)/2
            val x1 = floor((t - sd) / 2 + 1)
            val x2 = ceil((t + sd) / 2 - 1)
            (1 + x2 - x1).toInt()
        }
    }

    override fun part1(input: List<String>, testArg: Any?) = input
        .map { l -> l.split(Regex(" +")).map { it.toLong() } }
        .let { (t, d) -> t.zip(d) }
        .map { (t, d) -> countPossibilities(t, d) }
        .reduce(Int::times)

    override fun part2(input: List<String>, testArg: Any?) = input
        .map { l -> l.replace(" ", "").toLong() }
        .let { (t, d) -> countPossibilities(t, d) }

    override fun testCases1() = listOf(TestCase(getTestInput(), 288))

    override fun testCases2() = listOf(TestCase(getTestInput(), 71503))
}

fun main() {
    Day06().main()
}
