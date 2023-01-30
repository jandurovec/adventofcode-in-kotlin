package aoc2017

import IntListDay
import TestCase

class Day06 : IntListDay<Int, Int>(2017, 6) {

    override fun parseInput(stringInput: List<String>) =
        stringInput.first().split("""\s+""".toRegex()).map { it.toInt() }

    private fun bankSequence(initialState: List<Int>) = sequence {
        val banks = initialState.toIntArray()
        var state = banks.joinToString()
        val configurations = mutableSetOf<String>()
        do {
            configurations.add(state)
            var idx = banks.indices.maxBy { banks[it] }
            var toRedistribute = banks[idx]
            banks[idx] = 0
            idx = (idx + 1) % banks.size
            while (toRedistribute > 0) {
                banks[idx]++
                toRedistribute--
                idx = (idx + 1) % banks.size
            }
            yield(banks)
            state = banks.joinToString()
        } while (!configurations.contains(state))
    }

    override fun part1(input: List<Int>, testArg: Any?) = bankSequence(input).count()
    override fun part2(input: List<Int>, testArg: Any?) = bankSequence(bankSequence(input).last().toList()).count()


    override fun testCases1() = listOf(TestCase(listOf(0, 2, 7, 0), 5))
    override fun testCases2() = listOf(TestCase(listOf(0, 2, 7, 0), 4))
}

fun main() {
    Day06().main()
}
