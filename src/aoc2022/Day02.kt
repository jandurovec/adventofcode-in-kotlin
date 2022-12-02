package aoc2022

import readInput

fun main() {
    val winPoints = 6
    val drawPoints = 3

    fun parseInput(name: String): List<Pair<Int, Int>> {
        return readInput(name)
            .map { it.split(" ") }
            .map { it[0][0].code - 'A'.code + 1 to it[1][0].code - 'X'.code + 1 }
    }

    fun part1(input: List<Pair<Int, Int>>) = input.sumOf {
        it.second + when {
            it.first == it.second -> drawPoints
            1 + (it.first % 3) == it.second -> winPoints
            else -> 0
        }
    }

    fun part2(input: List<Pair<Int, Int>>) = input.sumOf {
        when (it.second) {
            1 -> ((it.first + 1) % 3) + 1
            2 -> it.first + drawPoints
            else -> (it.first % 3) + 1 + winPoints
        }
    }

    val testInput = parseInput("aoc2022/Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = parseInput("aoc2022/Day02")
    println(part1(input))
    println(part2(input))
}
