package aoc2024

import AdventDay
import TestCase
import kotlin.math.absoluteValue

class Day01 : AdventDay<Pair<List<Int>, List<Int>>, Int, Int>(2024, 1) {

    override fun parseInput(stringInput: List<String>) = stringInput
        .fold(mutableListOf<Int>() to mutableListOf<Int>()) { acc, s ->
            val (l, r) = s.split(" +".toRegex()).map { it.toInt() }
            acc.first.add(l)
            acc.second.add(r)
            acc
        }

    override fun part1(input: Pair<List<Int>, List<Int>>, testArg: Any?): Int {
        val (left, right) = input.toList().map { it.sorted() }
        return left.zip(right).sumOf { (l, r) -> (l - r).absoluteValue }
    }

    override fun part2(input: Pair<List<Int>, List<Int>>, testArg: Any?): Int {
        val (left, right) = input.toList().map { lst -> lst.groupingBy { it }.eachCount() }
        return left.entries.sumOf { (n, count) ->
            n * count * right.getOrDefault(n, 0)
        }
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), 11))

    override fun testCases2() = listOf(TestCase(getTestInput(), 31))

}

fun main() {
    Day01().main()
}
