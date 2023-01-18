package aoc2016

import SingleStringDay
import TestCase

class Day09 : SingleStringDay<Int, Long>(2016, 9) {
    override fun part1(input: String, testArg: Any?) = buildString {
        var pos = 0
        while (pos <= input.lastIndex) {
            if (input[pos] == '(') {
                val endIndex = input.indexOf(')', pos)
                val (len, times) = input.substring(pos + 1, endIndex).split('x').map { it.toInt() }
                val toRepeat = input.substring(endIndex + 1, endIndex + 1 + len)
                repeat(times) {
                    append(toRepeat)
                }
                pos = endIndex + len
            } else {
                append(input[pos])
            }
            pos++
        }
    }.length

    override fun part2(input: String, testArg: Any?): Long {
        abstract class Node {
            abstract fun length(): Long
        }

        open class Group(val times: Int, val children: List<Node>) : Node() {
            override fun length(): Long = times * children.sumOf { it.length() }
        }

        class Literal(val s: String) : Node() {
            override fun length() = s.length.toLong()
        }

        fun parse(file: String): List<Node> {
            val list = mutableListOf<Node>()
            var pos = 0
            while (pos < file.length) {
                pos = if (file[pos] != '(') {
                    val endIndex = file.indexOf('(', pos).let { if (it > -1) it else file.length }
                    list.add(Literal(file.substring(pos, endIndex)))
                    endIndex
                } else {
                    val endIndex = file.indexOf(')', pos)
                    val (len, times) = file.substring(pos + 1, endIndex).split('x').map { it.toInt() }
                    val toRepeat = file.substring(endIndex + 1, endIndex + 1 + len)
                    list.add(Group(times, parse(toRepeat)))
                    endIndex + 1 + len
                }
            }
            return list
        }
        return parse(input).sumOf { it.length() }
    }

    override fun testCases1() = listOf(
        TestCase("ADVENT", 6),
        TestCase("A(1x5)BC", 7),
        TestCase("(3x3)XYZ", 9),
        TestCase("A(2x2)BCD(2x2)EFG", 11),
        TestCase("(6x1)(1x3)A", 6),
        TestCase("X(8x2)(3x3)ABCY", 18)
    )

    override fun testCases2() = listOf(
        TestCase("(3x3)XYZ", 9L),
        TestCase("(27x12)(20x12)(13x14)(7x10)(1x12)A", 241920L),
        TestCase("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN", 445L)
    )
}

fun main() {
    Day09().main()
}
