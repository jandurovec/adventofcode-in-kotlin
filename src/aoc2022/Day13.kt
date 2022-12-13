package aoc2022

import readInput

fun main() {
    fun parseList(input: String): Pair<List<Any>, Int> {
        val result = mutableListOf<Any>()
        var pos = 1
        var intValue = 0
        var intInProgress = false
        while (input[pos] != ']') {
            if (input[pos].isDigit()) {
                intInProgress = true
                intValue = 10 * intValue + input[pos].digitToInt()
            } else if (input[pos] == ',') {
                if (intInProgress) {
                    intInProgress = false
                    result.add(intValue)
                    intValue = 0
                }
            } else {
                val (nested, len) = parseList(input.substring(pos))
                result.add(nested)
                pos += len
            }
            pos++
        }
        if (intInProgress) {
            result.add(intValue)
        }
        return result to pos
    }

    fun parseInput(input: List<String>) = input.asSequence().filter { it != "" }.map { parseList(it).first }

    operator fun List<*>.compareTo(other: List<*>): Int {
        for (i in this.indices) {
            if (i > other.lastIndex) {
                return 1
            } else {
                val a = this[i]
                val b = other[i]
                if (a is Int && b is Int) {
                    val intComparison = a.compareTo(b)
                    if (intComparison != 0) {
                        return intComparison
                    }
                } else if (a is List<*> && b is List<*>) {
                    val listComparison = a.compareTo(b)
                    if (listComparison != 0) {
                        return listComparison
                    }
                } else if (a is Int && b is List<*>) {
                    val listComparison = listOf(a).compareTo(b)
                    if (listComparison != 0) {
                        return listComparison
                    }
                } else if (a is List<*> && b is Int) {
                    val listComparison = a.compareTo(listOf(b))
                    if (listComparison != 0) {
                        return listComparison
                    }
                }
            }
        }
        return if (this.size < other.size) -1 else 0
    }

    fun part1(input: List<String>) = parseInput(input).chunked(2)
        .foldIndexed(0) { i, acc, (l1, l2) -> if (l1 <= l2) acc + i + 1 else acc }

    fun part2(input: List<String>): Int {
        /*
            Sorting is shorter code but O(n*log(n)), however, in order to find indices we just need to
            count items smaller than divider packets
         */
        val divider1 = listOf(listOf(2))
        val divider2 = listOf(listOf(6))
        var before = 0
        var between = 0
        parseInput(input).forEach {
            if (it < divider1) {
                before++
            } else if (it < divider2) {
                between++
            }
        }
        return (before + 1) * (before + between + 2)
    }

    val testInput = readInput(2022, 13, "test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput(2022, 13)
    println(part1(input))
    println(part2(input))
}
