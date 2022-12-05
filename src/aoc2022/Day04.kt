package aoc2022

import readInput

fun main() {
    fun checkInterval(input: List<String>, p: (List<List<Int>>) -> Boolean) =
        input.count { l ->
            l.split(',')
                .map { rng -> rng.split("-").map { it.toInt() } }
                .let(p)
        }

    fun part1(input: List<String>) = checkInterval(input) { (a, b) ->
        a[0] <= b[0] && a[1] >= b[1] || a[0] >= b[0] && a[1] <= b[1]
    }

    fun part2(input: List<String>) = checkInterval(input) { (a, b) ->
        b[1] >= a[0] && b[0] <= a[1]
    }

    val testInput = readInput(2022, 4, "test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput(2022, 4)
    println(part1(input))
    println(part2(input))
}
