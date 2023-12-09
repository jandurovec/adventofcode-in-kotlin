package aoc2023

import AdventDay
import TestCase
import gcd


class Day08 : AdventDay<Day08.Wasteland, Int, Long>(2023, 8) {
    data class Wasteland(private val instr: String, val nodes: Map<String, Map<Char, String>>) {
        private val instructions = sequence {
            while (true) {
                yieldAll(instr.asSequence())
            }
        }

        fun path(start: String) = instructions.runningFold(start) { prev, cur -> nodes[prev]!![cur]!! }
    }

    override fun parseInput(stringInput: List<String>): Wasteland {
        val nodes = stringInput.drop(2).map { line ->
            val (key, values) = line.split(" = ")
            val (l, r) = values.trim('(', ')').split(", ")
            key to mapOf('L' to l, 'R' to r)
        }.associate { it }
        return Wasteland(stringInput[0], nodes)
    }

    override fun part1(input: Wasteland, testArg: Any?): Int {
        return input.path("AAA").takeWhile { it != "ZZZ" }.count()
    }


    override fun part2(input: Wasteland, testArg: Any?): Long {
        /* The inputs for this task are super-specific where the state ending with a Z occurs periodically after N
        steps (and nowhere else), therefore it's sufficient to calculate the LCM of the distances to the first such
        state on each path. A solution for a generic input would be much more complicated.
         */
        val start = input.nodes.keys.filter { it.endsWith('A') }
        return start.map { s -> input.path(s).takeWhile { !it.endsWith('Z') }.count() }
            .map { it.toLong() }.reduce { a, b -> a * b / gcd(a, b) }
    }

    override fun testCases1() = listOf(
        TestCase(getInput("test1"), 2),
        TestCase(getInput("test2"), 6)
    )

    override fun testCases2() = listOf(TestCase(getInput("test3"), 6L))
}

fun main() {
    Day08().main()
}
