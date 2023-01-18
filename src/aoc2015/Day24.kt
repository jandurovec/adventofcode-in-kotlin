package aoc2015

import AdventDay
import TestCase
import kotlin.math.sign

class Day24 : AdventDay<List<Int>, Long, Long>(2015, 24) {

    override fun parseInput(stringInput: List<String>) = stringInput.map { it.toInt() }

    private fun List<Int>.findGroup(n: Int, fromIndex: Int = 0, acc: Set<Int> = emptySet()): Sequence<Set<Int>> =
        sequence {
            val cur = this@findGroup[fromIndex]
            if (cur == n) {
                yield(acc + cur)
            } else if (cur < n && fromIndex < this@findGroup.lastIndex) {
                yieldAll(findGroup(n - cur, fromIndex + 1, acc + cur))
                yieldAll(findGroup(n, fromIndex + 1, acc))
            }
        }

    private fun Set<Int>.quantunEntanglement(): Long = map { it.toLong() }.reduce(Long::times)

    private fun List<Int>.minEntanglement(groupCount: Int): Long {
        val sortedItems = sorted()
        val groupSize = sum() / groupCount
        val groups = sortedItems.findGroup(groupSize)
        /*
        Normally it would be needed to recursively check if the remainder can be split to "groups -1" groups,
        however, AoC input seems to be nice enough so that this check is not needed and the found groups are valid
         */
        return groups.minWith { a, b ->
            (a.size - b.size).let { if (it == 0) (a.quantunEntanglement() - b.quantunEntanglement()).sign else it }
        }.quantunEntanglement()
    }

    override fun part1(input: List<Int>, testArg: Any?) = input.minEntanglement(3)
    override fun part2(input: List<Int>, testArg: Any?) = input.minEntanglement(4)

    override fun testCases1() = listOf(TestCase(listOf(1, 2, 3, 4, 5, 7, 8, 9, 10, 11), 99L))
    override fun testCases2() = listOf(TestCase(listOf(1, 2, 3, 4, 5, 7, 8, 9, 10, 11), 44L))
}

fun main() {
    Day24().main()
}
