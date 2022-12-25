package aoc2022

import readInput

fun main() {
    fun String.parseSnafu(): Long {
        val mapping = mapOf('=' to -2, '-' to -1, '0' to 0, '1' to 1, '2' to 2)
        return this.fold(0L) { acc, c -> acc * 5 + mapping[c]!! }
    }

    fun Long.toSnafu(): String {
        var remaining = this
        return buildString {
            while (remaining != 0L) {
                val d = remaining.mod(5)
                append("012=-"[d])
                remaining = remaining / 5 + if (d > 2) 1 else 0
            }
        }.reversed()
    }

    fun part1(input: List<String>): String {
        return input.sumOf { it.parseSnafu() }.toSnafu()
    }

    val testInput = readInput(2022, 25, "test")
    check(part1((testInput)) == "2=-1=0")

    val input = readInput(2022, 25)
    println(part1(input))
}
