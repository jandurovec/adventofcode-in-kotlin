package aoc2017

import AdventDay
import Point
import TestCase
import kotlin.math.sqrt

class Day21 : AdventDay<Map<String, String>, Int, Int>(2017, 21) {
    override fun parseInput(stringInput: List<String>) = stringInput.flatMap { line ->
        fun rotateCw(s: String, size: Int) = buildString {
            for (row in 0 until size) {
                for (col in 0 until size) {
                    append(s[s.length - (col + 1) * size + row])
                }
            }
        }

        fun rotations(s: String, size: Int) = listOf(
            s,
            rotateCw(s, size),
            s.reversed(),
            rotateCw(s.reversed(), size)
        )

        fun flipAndRotate(s: String): List<String> {
            val size = sqrt(s.length.toDouble()).toInt()
            val transposed = buildString {
                for (row in 0 until size) {
                    for (col in 0 until size) {
                        append(s[col * size + row])
                    }
                }
            }
            return rotations(s, size).plus(rotations(transposed, size))
        }
        val (k, v) = line.split(" => ")
        flipAndRotate(k.replace("/", "")).map { it to v.replace("/", "") }
    }.associate { it }

    private fun simulate(rules: Map<String, String>, iterations: Int): Int {
        var lit = listOf(".#.", "..#", "###").flatMapIndexed { rowIdx, row ->
            row.mapIndexedNotNull { colIdx, c -> if (c == '#') Point(colIdx, rowIdx) else null }
        }.toSet()
        var size = 3
        repeat(iterations) {
            val partSize = if (size % 2 == 0) 2 else 3
            val newPartSize = partSize + 1
            val parts = size / partSize
            val newLit = mutableSetOf<Point>()
            for (px in 0 until parts) {
                for (py in 0 until parts) {
                    val part = buildString {
                        for (y in 0 until partSize) {
                            for (x in 0 until partSize) {
                                append(if (lit.contains(Point(partSize * px + x, partSize * py + y))) '#' else '.')
                            }
                        }
                    }
                    val newPart = rules[part]!!
                    for (x in 0 until newPartSize) {
                        for (y in 0 until newPartSize) {
                            if (newPart[x + newPartSize * y] == '#') {
                                newLit.add(Point(px * newPartSize + x, py * newPartSize + y))
                            }
                        }
                    }
                }
            }
            lit = newLit
            size += parts
        }
        return lit.size
    }

    override fun part1(input: Map<String, String>, testArg: Any?) = simulate(input, if (testArg is Int) testArg else 5)
    override fun part2(input: Map<String, String>, testArg: Any?) = simulate(input, 18)

    override fun testCases1() = listOf(TestCase(getTestInput(), 12, 2))
}

fun main() {
    Day21().main()
}
