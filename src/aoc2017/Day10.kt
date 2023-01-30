package aoc2017

import SingleStringDay
import TestCase

class Day10 : SingleStringDay<Int, String>(2017, 10) {

    class Knot(val size: Int) {
        val numbers = IntArray(size) { it }
        private var pos = 0
        private var skipSize = 0
        fun twist(length: Int) {
            val toTwist = ArrayDeque<Int>()
            var tempPos = pos
            repeat(length) {
                toTwist.addLast(numbers[tempPos])
                tempPos = (tempPos + 1) % size
            }
            while (toTwist.isNotEmpty()) {
                tempPos = (tempPos - 1).mod(size)
                numbers[tempPos] = toTwist.removeFirst()
            }
            pos = (pos + length + (skipSize++)) % size
        }
    }

    override fun part1(input: String, testArg: Any?): Int {
        val length = if (testArg is Int) testArg else 256
        val knot = Knot(length)
        input.split(',').map { it.toInt() }.forEach { knot.twist(it) }
        return knot.numbers[0] * knot.numbers[1]
    }

    override fun part2(input: String, testArg: Any?): String {
        val knot = Knot(256)
        val seq = input.map { it.code }.plus(listOf(17, 31, 73, 47, 23))
        repeat(64) {
            seq.forEach { knot.twist(it) }
        }
        return knot.numbers.asSequence().chunked(16)
            .joinToString("") { it.reduce(Int::xor).toString(16).padStart(2, '0') }
    }

    override fun testCases1() = listOf(
        TestCase("3,4,1,5", 12, 5)
    )

    override fun testCases2() = listOf(
        TestCase("", "a2582a3a0e66e6e86e3812dcb672a272"),
        TestCase("AoC 2017", "33efeb34ea91902bb2f59c9920caa6cd"),
        TestCase("1,2,3", "3efbe78a8d82f29979031a4aa0b16a9d"),
        TestCase("1,2,4", "63960835bcdc130f0b66d7ff4f6a5a8e")
    )
}

fun main() {
    Day10().main()
}
