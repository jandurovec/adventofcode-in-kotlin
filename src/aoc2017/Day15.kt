package aoc2017

import AdventDay
import TestCase

class Day15 : AdventDay<Pair<Int, Int>, Int, Int>(2017, 15) {
    override fun parseInput(stringInput: List<String>) =
        stringInput.map { it.substringAfterLast(' ').toInt() }.let { (a, b) -> a to b }

    private fun generator(seed: Int, factor: Int): Sequence<Int> = generateSequence(seed) {
        (it.toLong() * factor).mod(2147483647)
    }.drop(1)

    private fun generatorA(seed: Int) = generator(seed, 16807)
    private fun generatorB(seed: Int) = generator(seed, 48271)

    private fun judge(seq: Sequence<Pair<Int, Int>>) = seq.count { (a, b) -> a and 0xffff == b and 0xffff }

    override fun part1(input: Pair<Int, Int>, testArg: Any?) =
        generatorA(input.first).zip(generatorB(input.second)).take(40_000_000)
            .let { judge(it) }

    override fun part2(input: Pair<Int, Int>, testArg: Any?) =
        generatorA(input.first).filter { it.mod(4) == 0 }.zip(generatorB(input.second).filter { it.mod(8) == 0 })
            .take(5_000_000).let { judge(it) }

    override fun testCases1() = listOf(TestCase(65 to 8921, 588))
    override fun testCases2() = listOf(TestCase(65 to 8921, 309))
}

fun main() {
    Day15().main()
}
