package aoc2022

import overlaps
import readInput

fun main() {
    fun checkInterval(input: List<String>, p: (List<IntRange>) -> Boolean) =
        input.count { l ->
            l.split(',')
                .map { rng -> rng.split("-").map { it.toInt() } }
                .map { (l,r) -> l..r }
                .let(p)
        }

    fun part1(input: List<String>) = checkInterval(input) { (a, b) ->
        a.first <= b.first && a.last >= b.last || a.first >= b.first && a.last <= b.last
    }

    fun part2(input: List<String>) = checkInterval(input) { (a, b) ->
        a.overlaps(b)
    }

    val testInput = readInput(2022, 4, "test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput(2022, 4)
    println(part1(input))
    println(part2(input))
}
