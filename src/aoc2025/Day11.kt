package aoc2025

import AdventDay
import TestCase


class Day11 : AdventDay<Map<String, List<String>>, Long, Long>(2025, 11) {
    override fun parseInput(stringInput: List<String>) = buildMap {
        stringInput.forEach { line ->
            val split = line.split(" ")
            val key = split.first().replace(":", "")
            put(key, split.drop(1))
        }
    }

    override fun part1(input: Map<String, List<String>>, testArg: Any?): Long {
        fun paths(start: String): Long {
            if (start == "out") {
                return 1
            }
            val next = input[start]
            if (next != null) {
                return next.sumOf { paths(it) }
            }
            return 0
        }
        return paths("you")
    }


    override fun part2(input: Map<String, List<String>>, testArg: Any?): Long {
        val cache = mutableMapOf<Pair<String, Set<String>>, Long>()
        fun paths(start: String, required: Set<String>): Long {
            val cacheKey = start to required
            if (cache.contains(cacheKey)) {
                return cache[cacheKey]!!
            }
            if (start == "out") {
                return (if (required.isEmpty()) 1L else 0L).also { cache[start to required] = it }
            }
            val next = input[start]
            if (next != null) {
                return next.sumOf { paths(it, required - it) }.also { cache[start to required] = it }
            }
            return 0L.also { cache[start to required] = it }
        }
        return paths("svr", setOf("dac", "fft"))
    }

    override fun testCases1() = listOf(TestCase(getInput("test1"), 5L))
    override fun testCases2() = listOf(TestCase(getInput("test2"), 2L))
}

fun main() {
    Day11().main()
}
