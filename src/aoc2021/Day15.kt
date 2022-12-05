package aoc2021

import readInput
import java.util.*

fun main() {
    open class SquareGrid(val size: Int, val data: Array<IntArray>) {
        open fun risk(row: Int, col: Int) = data[row][col]
        fun neighbors(row: Int, col: Int) =
            listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0).map { row + it.first to col + it.second }
                .filter { it.first in 0 until size && it.second in 0 until size }
    }

    fun parseInput(lines: List<String>): SquareGrid {
        val data = lines.map { r -> r.toCharArray().map { it.digitToInt() }.toIntArray() }.toTypedArray()
        return SquareGrid(lines.size, data)
    }

    fun part1(grid: SquareGrid): Int {
        val risk = Array(grid.size) { IntArray(grid.size) { Int.MAX_VALUE } }
        risk[0][0] = 0
        val toExplore =
            PriorityQueue { a: Pair<Pair<Int, Int>, Int>, b: Pair<Pair<Int, Int>, Int> -> a.second - b.second }
        toExplore.add((0 to 0) to 0)
        while (toExplore.isNotEmpty()) {
            val (pos, curVal) = toExplore.poll()
            grid.neighbors(pos.first, pos.second).forEach { n ->
                val newRisk = curVal + grid.risk(n.first, n.second)
                if (newRisk < risk[n.first][n.second]) {
                    risk[n.first][n.second] = newRisk
                    toExplore.offer(n to newRisk)
                }
            }
        }
        return risk[grid.size - 1][grid.size - 1]
    }

    fun part2(grid: SquareGrid): Int {
        return part1(object : SquareGrid(5 * grid.size, grid.data) {
            override fun risk(row: Int, col: Int) =
                1 + (super.risk(row % grid.size, col % grid.size) + row / grid.size + col / grid.size - 1) % 9
        })
    }

    val testInput = parseInput(readInput(2021, 15, "test"))
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = parseInput(readInput(2021, 15))
    println(part1(input))
    println(part2(input))
}
