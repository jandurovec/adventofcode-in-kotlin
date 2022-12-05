package aoc2022

import readInput

fun main() {
    fun findCommon(parts: List<String>) =
        parts.map { it.toSet() }
            .reduce { acc, e -> acc.intersect(e) }
            .single()
            .let {
                if (it.isLowerCase()) {
                    it - 'a' + 1
                } else {
                    it - 'A' + 27
                }
            }

    fun part1(input: List<String>): Int = input.sumOf {
        it.chunked(it.length / 2).run(::findCommon)
    }

    fun part2(input: List<String>) = input.chunked(3).sumOf(::findCommon)

    val testInput = readInput(2022, 3, "test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput(2022, 3)
    println(part1(input))
    println(part2(input))
}
