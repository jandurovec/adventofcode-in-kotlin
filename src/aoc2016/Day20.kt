package aoc2016

import AdventDay
import TestCase

class Day20 : AdventDay<List<UIntRange>, UInt, UInt>(2016, 20) {
    override fun parseInput(stringInput: List<String>) = stringInput.map { s ->
        s.split('-').map { it.toUInt() }.let { (from, to) -> from..to }
    }

    private fun UIntRange.overlaps(other: UIntRange) = this.last >= other.first && this.first <= other.last
    private fun Iterable<UIntRange>.subtractRange(range: UIntRange) = this.flatMap {
        if (it.overlaps(range))
            listOf(it.first until range.first).let { rng ->
                if (range.last < UInt.MAX_VALUE) rng.plusElement(range.last + 1u..it.last) else rng
            }
        else
            listOf(it)
    }.filter { !it.isEmpty() }

    private fun getAllowed(range: UIntRange, blacklist: List<UIntRange>): List<UIntRange> {
        var valid = listOf(range)
        for (restricted in blacklist) {
            valid = valid.subtractRange(restricted)
        }
        return valid
    }

    override fun part1(input: List<UIntRange>, testArg: Any?): UInt {
        val max = if (testArg is UInt) testArg else UInt.MAX_VALUE
        return getAllowed(0u..max, input).first().first
    }

    override fun part2(input: List<UIntRange>, testArg: Any?): UInt {
        val max = if (testArg is UInt) testArg else UInt.MAX_VALUE
        return getAllowed(0u..max, input).sumOf { it.last - it.first + 1u }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 3u, 9u))
    override fun testCases2() = listOf(TestCase(getTestInput(), 2u, 9u))
}

fun main() {
    Day20().main()
}
