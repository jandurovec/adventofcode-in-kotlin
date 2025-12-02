package aoc2025

import AdventDay
import TestCase

class Day02 : AdventDay<List<LongRange>, Long, Long>(2025, 2) {
    override fun parseInput(stringInput: List<String>): List<LongRange> {
        return stringInput[0].split(",").map { part ->
            val bounds = part.split("-").map { it.toLong() }
            bounds[0]..bounds[1]
        }
    }

    override fun part1(input: List<LongRange>, testArg: Any?): Long {
        return input.sumOf { range ->
            sequence {
                val firstStr = range.first.toString()
                var halfStr = if (firstStr.length % 2 == 0) {
                    firstStr.take(firstStr.length / 2)
                } else {
                    "1".padEnd(firstStr.length / 2, '0')
                }
                var candidate = (halfStr + halfStr).toLong()
                while (candidate <= range.last) {
                    if (candidate in range) {
                        yield(candidate)
                    }
                    halfStr = (halfStr.toLong() + 1).toString()
                    candidate = (halfStr + halfStr).toLong()
                }
            }.sum()
        }
    }

    override fun part2(input: List<LongRange>, testArg: Any?): Long {
        return input.sumOf { range ->
            val lastStr = range.last.toString()
            val maxSegment = if (lastStr.length % 2 == 0) {
                lastStr.take(lastStr.length / 2)
            } else {
                "".padEnd(lastStr.length / 2, '9')
            }.toLong()
            var segment = 1L
            val invalidIds = mutableSetOf<Long>()
            val minLen = range.first.toString().length
            val maxLen = lastStr.length
            while (segment <= maxSegment) {
                val segmentStr = segment.toString()
                var candidateStr = buildString {
                    // the sequence needs to repeat at least twice
                    repeat(2) { append(segmentStr) }
                    while (length < minLen) {
                        append(segmentStr)
                    }
                }
                while (candidateStr.length <= maxLen) {
                    val candidate = candidateStr.toLong()
                    if (candidate in range) {
                        invalidIds.add(candidate)
                    }
                    candidateStr += segmentStr
                }
                segment++
            }
            invalidIds.sum()
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 1227775554L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 4174379265L))

}

fun main() {
    Day02().main()
}
