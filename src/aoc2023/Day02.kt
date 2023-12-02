package aoc2023

import AdventDay
import TestCase
import kotlin.math.max

class Day02 : AdventDay<List<Day02.Game>, Int, Int>(2023, 2) {
    class Game(description: String) {
        val id: Int
        val rounds: List<Map<String, Int>>

        init {
            val (header, roundsStr) = description.split(": ")
            id = header.split(" ")[1].toInt()
            rounds = roundsStr.split(";").map { it.trim() }
                .map { round ->
                    round.split(",").map { it.trim() }.associate {
                        val splitRound = it.split(" ")
                        splitRound[1] to splitRound[0].toInt()
                    }
                }
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { Game(it) }

    override fun part1(input: List<Game>, testArg: Any?): Int {
        return input.filter { g ->
            g.rounds.all {
                it.getOrDefault("red", 0) <= 12 &&
                        it.getOrDefault("green", 0) <= 13 &&
                        it.getOrDefault("blue", 0) <= 14
            }
        }.sumOf { it.id }
    }

    override fun part2(input: List<Game>, testArg: Any?): Int {
        return input.sumOf { g ->
            val required = g.rounds.fold(mutableMapOf<String, Int>()) { prev, next ->
                next.forEach { (color, count) -> prev.merge(color, count, ::max) }
                prev
            }
            required.getOrDefault("red", 0) *
                    required.getOrDefault("green", 0) *
                    required.getOrDefault("blue", 0)
        }
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), 8))

    override fun testCases2() = listOf(TestCase(getTestInput(), 2286))

}

fun main() {
    Day02().main()
}
