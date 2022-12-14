package aoc2022

import readInput
import kotlin.math.sign

fun main() {
    val directions = listOf(0 to 1, -1 to 1, 1 to 1)
    val rock = '#'
    val sand = 'o'
    val start = 500 to 0

    fun parseInput(input: List<String>): MutableMap<Pair<Int, Int>, Char> {
        val result = mutableMapOf<Pair<Int, Int>, Char>()
        input.forEach { line ->
            line.split(" -> ")
                .map { it.split(",").let { (l, r) -> l.toInt() to r.toInt() } }
                .windowed(2).forEach { (from, to) ->
                    val step = (to.first - from.first).sign to (to.second - from.second).sign
                    var cur = from
                    while (cur != to) {
                        result[cur] = rock
                        cur = cur.first + step.first to cur.second + step.second
                    }
                    result[to] = rock
                }
        }
        return result
    }

    fun part1(input: List<String>) = parseInput(input).also { cave ->
        val bottom = cave.keys.maxOf { it.second }
        var full = false
        while (!full) {
            var cur = start
            while (cur.second < bottom) {
                val step =
                    directions.firstOrNull { !cave.containsKey(cur.first + it.first to cur.second + it.second) }
                        ?: break
                cur = cur.first + step.first to cur.second + step.second
            }

            full = if (cur.second < bottom) {
                cave[cur] = sand
                false
            } else {
                true
            }
        }
    }.values.count { it == sand }

    fun part2(input: List<String>) = parseInput(input).also { cave ->
        val bottom = cave.keys.maxOf { it.second } + 1
        var full = false
        while (!full) {
            var cur = start
            while (cur.second < bottom) {
                val step =
                    directions.firstOrNull { !cave.containsKey(cur.first + it.first to cur.second + it.second) }
                        ?: break
                cur = cur.first + step.first to cur.second + step.second
            }

            cave[cur] = sand
            full = cave.containsKey(start)
        }
    }.values.count { it == sand }

    val testInput = readInput(2022, 14, "test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput(2022, 14)
    println(part1(input))
    println(part2(input))
}
