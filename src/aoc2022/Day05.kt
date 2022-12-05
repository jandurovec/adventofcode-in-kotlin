package aoc2022

import readInput

fun main() {
    fun rearrange(input: List<String>, oneByOne: Boolean = true): String {
        val cmdRegex = """move (\d+) from (\d+) to (\d+)""".toRegex()
        val stacks = ArrayList<String>()
        for (line in input) {
            with(line) {
                when {
                    contains("[") -> {
                        line.substring(1)
                            .chunked(4)
                            .map { it.substring(0, 1).trim() }
                            .forEachIndexed { i, crate ->
                                while (stacks.size <= i) {
                                    stacks.add("")
                                }
                                stacks[i] = stacks[i] + crate
                            }
                    }

                    matches(cmdRegex) -> {
                        val (count: Int, from: Int, to: Int) = cmdRegex.matchEntire(line)!!.groupValues.subList(1, 4)
                            .map { it.toInt() }
                        val fromIndex = from - 1
                        val toIndex = to - 1
                        val toMove = stacks[fromIndex].substring(0, count)
                        stacks[fromIndex] = stacks[fromIndex].substring(count)
                        stacks[toIndex] = (if (oneByOne) toMove.reversed() else toMove) + stacks[toIndex]
                    }

                    else -> {}
                }
            }
        }
        return stacks.map { it[0] }.joinToString("")
    }

    fun part1(input: List<String>) = rearrange(input)

    fun part2(input: List<String>) = rearrange(input, false)

    val testInput = readInput(2022, 5, "test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput(2022, 5)
    println(part1(input))
    println(part2(input))
}
