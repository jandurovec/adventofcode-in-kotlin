package aoc2023

import AdventDay
import TestCase


class Day12 : AdventDay<List<Pair<String, List<Int>>>, Long, Long>(2023, 12) {
    override fun parseInput(stringInput: List<String>) = stringInput.map { line ->
        val (graphical, numList) = line.split(' ')
        graphical to numList.split(',').map { it.toInt() }
    }

    private fun solve(springs: Pair<String, List<Int>>): Long {
        val cache = mutableMapOf<Pair<String, List<Int>>, Long>()
        fun countArrangements(springs: Pair<String, List<Int>>): Long {
            if (cache.containsKey(springs)) {
                return cache[springs]!!
            }
            val (gr, num) = springs
            if (num.isEmpty()) {
                return (if (gr.contains('#')) 0L else 1L).also { cache[springs] = it }
            }
            if (gr.length < num.sum() + num.size - 1) {
                return 0L.also { cache[springs] = 0 }
            }
            if (gr.startsWith(".")) {
                return countArrangements(gr.dropWhile { it == '.' } to num).also { cache[springs] = it }
            }
            var arrangements = 0L
            if (gr.startsWith("?")) { // try first as blank
                arrangements += countArrangements(gr.drop(1) to num)
            }
            val segment = num.first()
            if (gr.take(segment).all { it != '.' } && (gr.length == segment || gr[segment] != '#')) {
                arrangements += countArrangements(gr.drop(segment + 1) to num.drop(1))
            }
            return arrangements.also { cache[springs] = it }
        }
        return countArrangements(springs)
    }

    override fun part1(input: List<Pair<String, List<Int>>>, testArg: Any?) = input.sumOf { solve(it) }

    override fun part2(input: List<Pair<String, List<Int>>>, testArg: Any?): Long {
        val factor = 5
        return input.sumOf { (gr, num) ->
            solve(List(factor) { gr }.joinToString("?") to List(factor) { num }.flatten())
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 21L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 525152L))
}

fun main() {
    Day12().main()
}
