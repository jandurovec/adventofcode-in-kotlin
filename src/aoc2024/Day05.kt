package aoc2024

import TestCase
import UnparsedDay
import split


class Day05 : UnparsedDay<Int, Int>(2024, 5) {

    override fun part1(input: List<String>, testArg: Any?): Int {
        val (order, updates) = input.split()
        return updates.map { u ->
            val pages = u.split(",").map { i -> i.toInt() }
            val indices = pages.mapIndexed { i, p -> p to i }.associate { it }
            pages to indices
        }.filter { (_, indices) ->
            order.all { o ->
                val (lo, hi) = o.split("|").map { it.toInt() }
                !indices.containsKey(lo) || !indices.containsKey(hi) || indices[lo]!! < indices[hi]!!
            }
        }.sumOf { (pages, _) -> pages[(pages.size - 1) / 2] }
    }

    override fun part2(input: List<String>, testArg: Any?): Int {
        val (order, updates) = input.split()
        val ordPairs = order.map { o -> o.split("|").map { it.toInt() } }.map { (a, b) -> a to b }
        return updates.map { it.split(",").map { i -> i.toInt() } }
            .sumOf { pages ->
                val pageSet = pages.toSet()
                val validRules = ordPairs.filter { pageSet.contains(it.first) && pageSet.contains(it.second) }
                val indices = pages.mapIndexed { i, p -> p to i }.associate { it }
                if (validRules.any { (lo, hi) -> indices[lo]!! > indices[hi]!! }) {
                    val higher = pages.associateWith { mutableSetOf<Int>() }
                    validRules.forEach { (lo, hi) ->
                        val cur = higher[lo]!!
                        cur.add(hi)
                        cur.addAll(higher[hi]!!)
                        higher.values.forEach {
                            if (it.contains(lo)) {
                                it.add(hi)
                            }
                        }
                    }
                    pages.sortedWith { a, b ->
                        if (a == b) 0 else if (higher[a]!!.contains(b)) -1 else 1
                    }[(pages.size - 1) / 2]
                } else {
                    0
                }
            }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 143))

    override fun testCases2() = listOf(TestCase(getTestInput(), 123))

}

fun main() {
    Day05().main()
}
