package aoc2025

import TestCase
import UnparsedDay


class Day07 : UnparsedDay<Int, Long>(2025, 7) {

    override fun part1(input: List<String>, testArg: Any?): Int {
        var beams = setOf(input.first().indexOf('S'))
        var splits = 0
        input.drop(1).forEach { line ->
            beams = buildSet {
                beams.forEach { beam ->
                    if (line[beam] == '^') {
                        add(beam - 1)
                        add(beam + 1)
                        splits++
                    } else {
                        add(beam)
                    }
                }
            }
        }
        return splits
    }

    override fun part2(input: List<String>, testArg: Any?): Long {
        var beams = mapOf(input.first().indexOf('S') to 1L)
        input.drop(1).forEach { line ->
            beams = buildMap {
                beams.forEach { (beam, timeLines) ->
                    val add = { _: Int, v: Long? -> (v ?: 0) + timeLines }
                    if (line[beam] == '^') {
                        compute(beam - 1, add)
                        compute(beam + 1, add)
                    } else {
                        compute(beam, add)
                    }
                }
            }
        }
        return beams.values.sum()
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 21))
    override fun testCases2() = listOf(TestCase(getTestInput(), 40L))
}

fun main() {
    Day07().main()
}
