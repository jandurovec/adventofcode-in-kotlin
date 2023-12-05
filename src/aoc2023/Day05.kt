package aoc2023

import AdventDay
import TestCase
import overlaps
import subtractRange
import union

class Day05 : AdventDay<Day05.Almanac, Long, Long>(2023, 5) {


    data class Almanac(val seeds: List<Long>, val mapping: List<List<Pair<LongRange, Long>>>)

    override fun parseInput(stringInput: List<String>): Almanac {
        val seeds = mutableListOf<Long>()
        val mapping = mutableListOf<MutableList<Pair<LongRange, Long>>>()
        stringInput.forEach { line ->
            when {
                line.startsWith("seeds: ") -> {
                    seeds.addAll(line.split(": ")[1].split(" ").map { it.toLong() })
                }

                line.endsWith("map:") -> {
                    mapping.add(mutableListOf())
                }

                line.isEmpty() -> {}
                else -> {
                    val (dest, src, size) = line.split(" ").map { it.toLong() }
                    mapping.last().add(src..src + size to dest - src)
                }
            }
        }
        return Almanac(seeds, mapping)
    }

    override fun part1(input: Almanac, testArg: Any?) = input.seeds.minOf { seed ->
        input.mapping.fold(seed) { cur, map ->
            val mapping = map.firstOrNull { it.first.contains(cur) }
            if (mapping == null) cur else cur + mapping.second
        }
    }

    override fun part2(input: Almanac, testArg: Any?) = input.seeds.chunked(2)
        .map { (from, length) -> from..from + length }
        .let { seedRanges ->
            input.mapping.fold(seedRanges) { curRanges, curMap ->
                val newUnmodified =
                    curMap.map { it.first }.fold(curRanges) { acc, mappedRange -> acc.subtractRange(mappedRange) }
                val newModified = curRanges.flatMap { range ->
                    curMap.mapNotNull { (mappedRange, offset) ->
                        if (mappedRange.overlaps(range)) {
                            val start = maxOf(range.first, mappedRange.first)
                            val end = minOf(range.last, mappedRange.last)
                            start + offset..end + offset
                        } else {
                            null
                        }
                    }
                }
                (newUnmodified + newModified).union()
            }.first().first
        }

    override fun testCases1() = listOf(TestCase(getTestInput(), 35L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 46L))
}

fun main() {
    Day05().main()
}
