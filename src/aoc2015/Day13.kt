package aoc2015

import AdventDay
import TestCase

class Day13 : AdventDay<Map<String, Map<String, Int>>, Int, Int>(2015, 13) {
    override fun parseInput(stringInput: List<String>) = buildMap<String, MutableMap<String, Int>> {
        val regex = Regex("""(.*) would (.*) happiness units by sitting next to (.*)\.""")
        stringInput.forEach {
            val (who, delta, neighbor) = regex.matchEntire(it)!!.destructured
            val effects = this.computeIfAbsent(who) { mutableMapOf() }
            effects[neighbor] = delta.split(" ").let { (direction, value) ->
                (if (direction == "gain") 1 else -1) * value.toInt()
            }
        }
    }

    private fun optimalHappiness(data: Map<String, Map<String, Int>>, attendees: Set<String>): Int {
        fun Set<String>.arrangements(arranged: List<String> = listOf()): Sequence<List<String>> =
            sequence {
                if (isEmpty()) {
                    yield(arranged)
                } else when (arranged.size) {
                    0 -> { // select anyone as head because the table is round
                        val head = this@arrangements.first()
                        yieldAll(this@arrangements.minus(head).arrangements(listOf(head)))
                    }

                    1 -> { // skip the last one to eliminate duplicate symmetrical arrangements
                        this@arrangements.minus(this@arrangements.last()).forEach {
                            yieldAll(this@arrangements.minus(it).arrangements(arranged.plus(it)))
                        }
                    }

                    else -> this@arrangements.forEach {
                        yieldAll(this@arrangements.minus(it).arrangements(arranged.plus(it)))
                    }
                }
            }

        fun List<String>.happiness() = mapIndexed { idx, name ->
            listOf(-1, 1).map { (idx + it).mod(this.size) }.sumOf {
                data.getOrDefault(name, mapOf()).getOrDefault(this[it], 0)
            }
        }.sum()

        return attendees.arrangements().maxOf { it.happiness() }
    }

    override fun part1(input: Map<String, Map<String, Int>>, testArg: Any?) = optimalHappiness(input, input.keys)
    override fun part2(input: Map<String, Map<String, Int>>, testArg: Any?) =
        optimalHappiness(input, input.keys.plus("me"))

    override fun testCases1() = listOf(TestCase(getTestInput(), 330))
}

fun main() {
    Day13().main()
}
