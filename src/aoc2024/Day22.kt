package aoc2024

import IntListDay
import TestCase


class Day22 : IntListDay<Long, Int>(2024, 22) {

    private tailrec fun Int.secret(pos: Int = 1): Int {
        var next = ((this shl 6) xor this) and 0xffffff
        next = ((next shr 5) xor next) and 0xffffff
        next = ((next shl 11) xor next) and 0xffffff
        return if (pos == 1) next else next.secret(pos - 1)
    }

    @Suppress("SameParameterValue")
    private fun prizeSequences(seed: Int, priceChanges: Int): Map<List<Int>, Int> {
        val prices = generateSequence(seed) { it.secret() }.take(priceChanges + 1).map { it % 10 }
        val deltas = prices.windowed(2).map { it[1] - it[0] }
        return deltas.windowed(4).zip(prices.drop(4))
            .groupBy { it.first }.mapValues { (_, v) -> v.first().second }
    }

    override fun part1(input: List<Int>, testArg: Any?): Long {
        return input.sumOf { it.secret(2000).toLong() }
    }

    override fun part2(input: List<Int>, testArg: Any?): Int {
        val seqToMaxPrize = input.map { prizeSequences(it, 2000) }
        val merged = seqToMaxPrize.map { it.toMutableMap() }.reduce { acc, map ->
            map.forEach { (k, v) -> acc.merge(k, v, Int::plus) }
            acc
        }
        return merged.maxOf { it.value }
    }

    override fun testCases1() = listOf(TestCase(listOf(1, 10, 100, 2024), 37327623L))

    override fun testCases2() = listOf(TestCase(listOf(1, 2, 3, 2024), 23))
}

fun main() {
    Day22().main()
}
