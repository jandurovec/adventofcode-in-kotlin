package aoc2017

import AdventDay
import TestCase

class Day16 : AdventDay<List<String>, String, String>(2017, 16) {
    override fun parseInput(stringInput: List<String>) = stringInput.first().split(",")

    private fun dance(size: Int, moves: List<String>) = sequence {
        var data = CharArray(size) { 'a' + it }
        fun CharArray.swap(p1: Int, p2: Int) {
            val x = this[p1]
            this[p1] = this[p2]
            this[p2] = x
        }
        yield(data.joinToString(""))
        while (true) {
            moves.forEach { move ->
                when (move[0]) {
                    's' -> {
                        val spinSize = move.substring(1).toInt()
                        val remaining = data.size - spinSize
                        val newData = CharArray(size)
                        data.copyInto(newData, 0, remaining)
                        data.copyInto(newData, spinSize, 0, remaining)
                        data = newData
                    }

                    'x' -> {
                        val (a, b) = move.substring(1).split("/").map { it.toInt() }
                        data.swap(a, b)
                    }

                    'p' -> {
                        val (a, b) = move.substring(1).split("/").map { it[0] }
                        data.swap(data.indexOf(a), data.indexOf(b))
                    }

                    else -> error("Unknown move $move")
                }
            }
            yield(data.joinToString(""))
        }
    }

    override fun part1(input: List<String>, testArg: Any?) =
        dance(if (testArg is Int) testArg else 16, input).drop(1).first()

    override fun part2(input: List<String>, testArg: Any?): String {
        val seen = mutableMapOf<String, Int>()
        val steps = 1_000_000_000
        for ((i, s) in dance(16, input).mapIndexed { i, s -> i to s }) {
            if (seen.containsKey(s)) {
                val loopStart = seen[s]!!
                val loopSize = i - loopStart
                val remaining = (steps - i) % loopSize
                return seen.entries.first { it.value == loopStart + remaining }.key
            } else if (i == steps) {
                return s
            } else {
                seen[s] = i
            }
        }
        error("Sequence too short")
    }

    override fun testCases1() = listOf(TestCase(listOf("s1", "x3/4", "pe/b"), "baedc", 5))
}

fun main() {
    Day16().main()
}
