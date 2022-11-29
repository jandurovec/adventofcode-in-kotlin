package aoc2021

import readInput

fun main() {
    /**
     * Returns set of dots (coordinates) and list of transformation/folding functions
     */
    fun parseInput(input: List<String>) =
        input.fold(mutableSetOf<Pair<Int, Int>>() to mutableListOf<(Pair<Int, Int>) -> Pair<Int, Int>?>()) { data, line ->
            when {
                line.contains(',') -> {
                    val dot = line.split(',').map { it.toInt() }
                    data.first.add(dot[0] to dot[1])
                }
                line.startsWith("fold along x=") -> {
                    val fold = line.substringAfterLast('=').toInt()
                    data.second.add {
                        when {
                            it.first < fold -> it
                            it.first > fold -> 2 * fold - it.first to it.second
                            else -> null
                        }
                    }
                }
                line.startsWith("fold along y=") -> {
                    val fold = line.substringAfterLast('=').toInt()
                    data.second.add {
                        when {
                            it.second < fold -> it
                            it.second > fold -> it.first to 2 * fold - it.second
                            else -> null
                        }
                    }
                }
            }
            data
        }

    fun foldPaper(paper: Set<Pair<Int, Int>>, actions: Iterable<(Pair<Int, Int>) -> Pair<Int, Int>?>) =
        actions.fold(paper) { p, action ->
            p.fold(mutableSetOf()) { agg, dot ->
                val newDot = action(dot)
                if (newDot != null) {
                    agg.add(newDot)
                }
                agg
            }
        }

    fun part1(input: List<String>): Int = parseInput(input).let { (dots, folds) -> foldPaper(dots, folds.take(1)) }.size

    fun part2(input: List<String>): String =
        parseInput(input).let { (dots, folds) -> foldPaper(dots, folds) }.let { folded ->
            (0..folded.maxOf { it.second }).joinToString("\n") { y ->
                (0..folded.maxOf { it.first }).joinToString("") { x ->
                    if (folded.contains(x to y)) "#" else " "
                }
            }
        }

    val testInput1 = readInput("aoc2021/Day13_test")
    check(part1(testInput1) == 17)
    check(
        """
        #####
        #   #
        #   #
        #   #
        #####
    """.trimIndent() == part2(testInput1)
    )

    val input = readInput("aoc2021/Day13")
    println(part1(input))
    println(part2(input))
}
