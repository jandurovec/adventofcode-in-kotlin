package aoc2023

import TestCase
import UnparsedDay


class Day07 : UnparsedDay<Int, Int>(2023, 7) {
    data class Hand(val bid: Int, private val typeRank : String, private val cardRank : Int) : Comparable<Hand> {
        override fun compareTo(other: Hand) = compareValuesBy(this, other, { it.typeRank }, { it.cardRank })
    }

    private fun solve(input: List<String>, cardOrder: String, typeRank: (String) -> String) : Int {
        val cardRank = cardOrder.mapIndexed { i, c -> c to i}.associate { it }
        return input.map { line ->
            val (cards, bidStr) = line.split(" ")
            Hand(bidStr.toInt(), typeRank(cards), cards.map { cardRank[it]!! }.reduce { acc, next -> acc * cardOrder.length + next})
        }.sorted().mapIndexed { index, hand -> (index + 1) * hand.bid }.reduce(Int::plus)
    }

    override fun part1(input: List<String>, testArg: Any?) = solve(input, "23456789TJQKA") { cards ->
        cards.groupingBy { it }.eachCount().values.sortedDescending().joinToString("") { it.toString() }
    }

    override fun part2(input: List<String>, testArg: Any?)= solve(input, "J23456789TQKA") { cards ->
        val counts = cards.groupingBy { it }.eachCount()
        val jokers= counts.getOrDefault('J', 0)
        val rankList = counts.filter { it.key != 'J' }.values.sortedDescending().toMutableList()
        if (rankList.isEmpty()) {
            rankList.add(jokers) // we have only jokers
        } else {
            rankList[0] += jokers
        }
         rankList.joinToString("") { it.toString() }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 6440))
    override fun testCases2() = listOf(TestCase(getTestInput(), 5905))
}

fun main() {
    Day07().main()
}
