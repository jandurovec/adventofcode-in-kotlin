package aoc2022

import readInput
import kotlin.math.max

fun main() {
    fun parseInput(classifier: String = "") = readInput(2022, 8, classifier)
        .map { s -> s.map { it.digitToInt() }.toIntArray() }
        .toTypedArray()

    fun Array<IntArray>.view(pos: Pair<Int, Int>, direction: Pair<Int, Int>): Sequence<Int> = sequence {
        var curX = pos.first + direction.first
        var curY = pos.second + direction.second
        while (this@view.indices.contains(curX) && this@view[curX].indices.contains(curY)) {
            yield(this@view[curX][curY])
            curX += direction.first
            curY += direction.second
        }
    }

    fun isVisible(input: Array<IntArray>, pos: Pair<Int, Int>, direction: Pair<Int, Int>) =
        input.view(pos, direction).all { h -> h < input[pos.first][pos.second] }

    fun viewingDistance(input: Array<IntArray>, pos: Pair<Int, Int>, direction: Pair<Int, Int>): Int {
        var result = 0
        for (cur in input.view(pos, direction)) {
            result++
            if (cur >= input[pos.first][pos.second]) {
                break
            }
        }
        return result
    }

    fun part1(forest: Array<IntArray>) = forest.foldIndexed(0) { i, acc, line ->
        acc + line.indices.count { j ->
            isVisible(forest, i to j, -1 to 0)
                    || isVisible(forest, i to j, 1 to 0)
                    || isVisible(forest, i to j, 0 to -1)
                    || isVisible(forest, i to j, 0 to 1)
        }
    }

    fun part2(forest: Array<IntArray>) = forest.foldIndexed(0) { i, acc, line ->
        max(acc, line.indices.maxOf { j ->
            viewingDistance(forest, i to j, -1 to 0) *
                    viewingDistance(forest, i to j, 1 to 0) *
                    viewingDistance(forest, i to j, 0 to -1) *
                    viewingDistance(forest, i to j, 0 to 1)
        })
    }

    val testInput = parseInput("test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = parseInput()
    println(part1(input))
    println(part2(input))
}
