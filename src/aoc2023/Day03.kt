package aoc2023

import AdventDay
import TestCase

class Day03 : AdventDay<Day03.Schematic, Int, Int>(2023, 3) {
    class Schematic(input: List<String>) {
        val numbers = mutableMapOf<Pair<Int, Int>, Int>()
        val symbols = mutableMapOf<Pair<Int, Int>, Char>()

        init {
            input.forEachIndexed { row, line ->
                var start = 0
                while (start < line.length) {
                    if (line[start] == '.') {
                        start++
                    } else if (line[start].isDigit()) {
                        var end = start + 1
                        while (end < line.length && line[end].isDigit()) {
                            end++
                        }
                        numbers[row to start] = line.substring(start, end).toInt()
                        start = end
                    } else {
                        symbols[row to start] = line[start++]
                    }
                }
            }
        }
    }

    override fun parseInput(stringInput: List<String>) = Schematic(stringInput)

    override fun part1(input: Schematic, testArg: Any?): Int {
        return input.numbers.filter { (pos, num) ->
            val (row, col) = pos
            (row - 1..row + 1).any { r ->
                (col - 1..col + num.toString().length).any { c ->
                    input.symbols.containsKey(r to c)
                }
            }
        }.values.sum()
    }

    override fun part2(input: Schematic, testArg: Any?): Int {
        return input.numbers.flatMap { (pos, num) ->
            val (row, col) = pos
            (row - 1..row + 1).flatMap { r ->
                (col - 1..col + num.toString().length).map { c -> r to c }.filter { input.symbols[it] == '*' }
            }.map { it to num }
        }.groupBy({ it.first }, { it.second }).filter { it.value.size == 2 }.values.sumOf { it.reduce(Int::times) }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 4361))

    override fun testCases2() = listOf(TestCase(getTestInput(), 467835))

}

fun main() {
    Day03().main()
}
