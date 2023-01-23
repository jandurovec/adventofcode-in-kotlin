package aoc2015

import IntListDay
import TestCase

class Day17 : IntListDay<Int, Int>(2015, 17) {
    private fun containerCombinations(
        containers: List<Int>,
        eggnog: Int,
        selected: List<Int> = listOf()
    ): Sequence<List<Int>> = sequence {
        if (eggnog == 0) {
            yield(selected)
        } else {
            val first = containers.indexOfFirst { it <= eggnog }
            if (first > -1) {
                for (i in first..containers.lastIndex) {
                    val item = containers[i]
                    val remainingContainers = containers.subList(i + 1, containers.size)
                    val eggnogLeft = eggnog - item
                    if (remainingContainers.sumOf { it } >= eggnogLeft) {
                        yieldAll(containerCombinations(remainingContainers, eggnogLeft, selected.plus(item)))
                    }
                }
            }
        }
    }

    override fun part1(input: List<Int>, testArg: Any?): Int =
        containerCombinations(input.sorted(), if (testArg is Int) testArg else 150).count()

    override fun part2(input: List<Int>, testArg: Any?): Int =
        containerCombinations(input.sorted(), 150).let { combinations ->
            val min = combinations.minOf { it.size }
            combinations.filter { it.size == min }.count()
        }

    override fun testCases1() = listOf(TestCase(listOf(20, 15, 10, 5, 5), 4, 25))
}

fun main() {
    Day17().main()
}
