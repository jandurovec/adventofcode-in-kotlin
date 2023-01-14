package aoc2015

import AdventDay
import TestCase

class Day09 : AdventDay<Map<Day09.Way, Int>, Int, Int>(2015, 9) {
    data class Way(val from: String, val to: String)

    override fun parseInput(stringInput: List<String>) = stringInput.flatMap {
        val (from, to, dist) = it.split("( to )|( = )".toRegex())
        val d = dist.toInt()
        listOf(Way(from, to) to d, Way(to, from) to d)
    }.associate { it }

    private fun solve(input: Map<Way, Int>, selector: Sequence<Int>.() -> Int): Int {
        val destinations = input.keys.flatMap { listOf(it.from, it.to) }.toSet()
        fun find(distances: Map<Way, Int>, at: String, toVisit: Set<String>, travelled: Int = 0): Int =
            if (toVisit.isEmpty()) travelled else toVisit.asSequence().map { next ->
                find(distances, next, toVisit.minus(next), travelled + distances[Way(at, next)]!!)
            }.run(selector)
        return destinations.asSequence().map { start -> find(input, start, destinations.minus(start)) }.run(selector)
    }

    override fun part1(input: Map<Way, Int>, testArg: Any?) = solve(input, Sequence<Int>::min)
    override fun part2(input: Map<Way, Int>, testArg: Any?) = solve(input, Sequence<Int>::max)

    override fun testCases1() = listOf(TestCase(getInput("test"), 605))
    override fun testCases2() = listOf(TestCase(getInput("test"), 982))
}

fun main() {
    Day09().main()
}
