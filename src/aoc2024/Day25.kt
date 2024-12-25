package aoc2024

import AdventDay
import TestCase
import split


class Day25 : AdventDay<Day25.Input, Int, String>(2024, 25) {
    companion object {
        const val SCHEMATIC_HEIGHT = 7
    }

    data class Input(val locks: List<List<Int>>, val keys: List<List<Int>>)

    override fun parseInput(stringInput: List<String>): Input {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        stringInput.split().forEach { schematics ->
            if (schematics[0].all { it == '#' }) {
                locks += schematics[0].indices.map { i ->
                    schematics.indices.first { schematics[it][i] == '.' } - 1
                }
            } else {
                keys += schematics[0].indices.map { i ->
                    schematics.lastIndex - schematics.indices.first { schematics[it][i] == '#' }
                }
            }
        }
        return Input(locks, keys)
    }

    override fun part1(input: Input, testArg: Any?): Int {
        return input.keys.sumOf { key ->
            input.locks.count { lock ->
                key.zip(lock).all { (k, l) -> k + l <= SCHEMATIC_HEIGHT - 2 }
            }
        }
    }

    override fun part2(input: Input, testArg: Any?) = ""
    override fun testCases1() = listOf(TestCase(getTestInput(), 3))

}

fun main() {
    Day25().main()
}
