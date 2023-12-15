package aoc2023

import AdventDay
import TestCase


class Day15 : AdventDay<List<String>, Int, Int>(2023, 15) {
    override fun parseInput(stringInput: List<String>) = stringInput.first().split(",")

    private fun hash(s: String) = s.fold(0) { hash, next -> ((hash + next.code) * 17) % 256 }

    override fun part1(input: List<String>, testArg: Any?) = input.sumOf(::hash)

    override fun part2(input: List<String>, testArg: Any?): Int {
        val boxes = Array(256) { linkedMapOf<String, String>() }
        input.map { it.split(Regex("[-=]")) }.forEach { (label, power) ->
            if (power.isNotBlank()) {
                boxes[hash(label)][label] = power
            } else {
                boxes[hash(label)].remove(label)
            }
        }
        return boxes.mapIndexed { boxIndex, box ->
            box.entries.mapIndexed { slot, entry -> (boxIndex + 1) * (slot + 1) * entry.value.toInt() }.sum()
        }.sum()
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 1320))

    override fun testCases2() = listOf(TestCase(getTestInput(), 145))
}

fun main() {
    Day15().main()
}
