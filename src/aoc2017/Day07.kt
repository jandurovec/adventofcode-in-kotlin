package aoc2017

import AdventDay
import TestCase

class Day07 : AdventDay<Day07.Program, String, Int>(2017, 7) {

    data class Program(val name: String, val weight: Int, val above: List<Program>) {
        val totalWeight: Int = weight + above.sumOf { it.totalWeight }
    }

    override fun parseInput(stringInput: List<String>): Program {
        val weights = mutableMapOf<String, Int>()
        val above = mutableMapOf<String, List<String>>()
        stringInput.forEach { l ->
            val parts = """(.*) \((.*)\)(?: -> (.*))?""".toRegex().matchEntire(l)!!.groupValues.drop(1)
            val name = parts[0]
            weights[name] = parts[1].toInt()
            if (parts[2].isNotBlank()) {
                val aboveNames = parts[2].split(", ")
                above[name] = aboveNames
            }
        }
        fun createProgram(name: String): Program =
            Program(name, weights[name]!!,
                above[name].let { p ->
                    p?.map { createProgram(it) } ?: emptyList()
                })

        return createProgram(weights.keys.first { above.values.none { p -> p.contains(it) } })
    }

    override fun part1(input: Program, testArg: Any?) = input.name
    override fun part2(input: Program, testArg: Any?): Int {
        fun findUnbalanced(prg: Program, delta: Int = 0): Int {
            val weightDist = prg.above.map { it.totalWeight }.groupingBy { it }.eachCount()
            val unbalancedWeight = weightDist.entries.firstOrNull { it.value == 1 }?.key
            return if (unbalancedWeight == null) {
                prg.weight + delta
            } else {
                val balancedWeight = prg.above.map { it.totalWeight }.first { it != unbalancedWeight }
                findUnbalanced(
                    prg.above.first { it.totalWeight == unbalancedWeight },
                    balancedWeight - unbalancedWeight
                )
            }
        }
        return findUnbalanced(input)
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), "tknk"))
    override fun testCases2() = listOf(TestCase(getTestInput(), 60))
}

fun main() {
    Day07().main()
}
