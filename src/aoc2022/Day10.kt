package aoc2022

import readInput
import kotlin.math.absoluteValue

fun main() {
    fun registerValues(input: List<String>) = sequence {
        var x = 1
        for (instr in input) {
            if (instr == "noop") {
                yield(x)
            } else {
                repeat(2) { yield(x) }
                x += instr.split(" ")[1].toInt()
            }
        }
    }

    fun part1(input: List<String>) = registerValues(input).mapIndexed { i, n -> i to n }
        .filter { (it.first - 19) % 40 == 0 }.sumOf { (it.first + 1) * it.second }

    fun part2(input: List<String>) = registerValues(input).mapIndexed { i, n ->
        if ((n - (i % 40)).absoluteValue < 2) '#' else '.'
    }.chunked(40).map { it.joinToString("") }

    val testInput = readInput(2022, 10, "test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput).toList() == listOf(
            "##..##..##..##..##..##..##..##..##..##..",
            "###...###...###...###...###...###...###.",
            "####....####....####....####....####....",
            "#####.....#####.....#####.....#####.....",
            "######......######......######......####",
            "#######.......#######.......#######....."
        )
    )

    val input = readInput(2022, 10)
    println(part1(input))
    println(part2(input).joinToString(System.lineSeparator()))
}
