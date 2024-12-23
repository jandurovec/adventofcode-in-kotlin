package aoc2024

import AdventDay
import TestCase


class Day23 : AdventDay<Day23.LanParty, Int, String>(2024, 23) {

    data class LanParty(val computers: Set<String>, val connections: Map<String, Set<String>>) {
        private val cliqueCache = mutableMapOf<Int, Set<Set<String>>>()
        fun cliques(size: Int): Set<Set<String>> = cliqueCache.getOrPut(size) {
            if (size == 1) {
                computers.map { setOf(it) }.toSet()
            } else {
                cliques(size - 1).flatMap { clq ->
                    computers.filter { last ->
                        val con = connections.getOrDefault(last, emptySet())
                        last !in clq && clq.all { con.contains(it) }
                    }.map { clq + it }
                }.toSet()
            }
        }
    }

    override fun parseInput(stringInput: List<String>): LanParty {
        val connections = buildMap {
            stringInput.forEach { line ->
                val (from, to) = line.split("-")
                sequenceOf(from to to, to to from).forEach { (src, dst) ->
                    merge(src, setOf(dst)) { a: Set<String>, b: Set<String> -> a + b }
                }
            }
        }
        return LanParty(connections.keys, connections)
    }

    override fun part1(input: LanParty, testArg: Any?): Int {
        return input.cliques(3).filter { clq -> clq.any { it.startsWith("t") } }.size
    }

    override fun part2(input: LanParty, testArg: Any?): String {
        return generateSequence(1) { it + 1 }.map { input.cliques(it) }.takeWhile { it.isNotEmpty() }
            .last().joinToString("\n") {
                it.sorted().joinToString(",")
            }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 7))
    override fun testCases2() = listOf(TestCase(getTestInput(), "co,de,ka,ta"))

}

fun main() {
    Day23().main()
}
