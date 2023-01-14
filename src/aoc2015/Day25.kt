package aoc2015

import AdventDay
import TestCase
import kotlin.math.sign

class Day25 : AdventDay<Pair<Int, Int>, Int, Unit>(2015, 25) {

    override fun parseInput(stringInput: List<String>) = stringInput
        .map { Regex(".* row (.*), column (.*).").matchEntire(it)!!.groupValues.drop(1) }
        .first()
        .map { it.toInt() }
        .let { (row, col) -> row to col }

    // (row,col) is on "row+col-1"-th diagonal (which starts at (n,1) and finishes at (1,n).
    // Size of n-th diagonal n.
    // The position of (row,col) in the sequence is the sum of the first n-1 diagonals plus "col"
    // (where n is the number of diagonal that (row,col) belong to)
    override fun part1(input: Pair<Int, Int>, testArg: Any?): Int {
        val (row, col) = input
        val diagonal = row + col - 1
        val pos = (diagonal * (diagonal - 1)) / 2 + col

        var code = 20151125
        repeat(pos - 1) {
            code = (code.toLong() * 252533).mod(33554393)
        }
        return code
    }

    override fun part2(input: Pair<Int, Int>, testArg: Any?) {}

    override fun testCases1() = listOf(TestCase(6 to 1, 33071741))
}

fun main() {
    Day25().main()
}
