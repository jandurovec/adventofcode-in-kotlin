package aoc2017

import AdventDay
import TestCase

class Day12 : AdventDay<Map<Int, Set<Int>>, Int, Int>(2017, 12) {
    override fun parseInput(stringInput: List<String>) = buildMap<Int, MutableSet<Int>> {
        stringInput.forEach { line ->
            val (sFrom, sTo) = line.split(" <-> ")
            val from = sFrom.toInt()
            val toList = sTo.split(", ").map { it.toInt() }
            computeIfAbsent(from) { mutableSetOf() }.addAll(toList)
            toList.forEach { to ->
                computeIfAbsent(to) { mutableSetOf() }.add(from)
            }
        }
    }

    private fun Map<Int, Set<Int>>.group(start: Int) = buildSet {
        add(start)
        var toProcess = setOf(start)
        while (toProcess.isNotEmpty()) {
            val newlyAdded = mutableSetOf<Int>()
            toProcess.forEach { next ->
                this@group[next]?.forEach {
                    if (add(it)) {
                        newlyAdded += it
                    }
                }
            }
            toProcess = newlyAdded
        }
    }

    override fun part1(input: Map<Int, Set<Int>>, testArg: Any?) = input.group(0).size
    override fun part2(input: Map<Int, Set<Int>>, testArg: Any?): Int {
        val remaining = input.keys.toMutableSet()
        var groupCount = 0
        while (remaining.isNotEmpty()) {
            groupCount++
            remaining.removeAll(input.group(remaining.first()))
        }
        return groupCount
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 6))
    override fun testCases2() = listOf(TestCase(getTestInput(), 2))
}

fun main() {
    Day12().main()
}
