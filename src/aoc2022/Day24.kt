package aoc2022

import manhattanDist
import readInput
import java.util.*

fun main() {
    val steps = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1, 0 to 0)

    class Blizzard(val startPos: Pair<Int, Int>, val direction: Pair<Int, Int>)
    class Basin(val blizzards: List<Blizzard>, val size: Pair<Int, Int>) {
        val start = 0 to -1
        val end = size.first - 1 to size.second

        fun Blizzard.posAt(time: Int) = (startPos.first + time * direction.first).mod(size.first) to
                (startPos.second + time * direction.second).mod(size.second)

        fun isFree(pos: Pair<Int, Int>, time: Int) = pos == start || pos == end ||
                (pos.first in 0 until size.first && pos.second in 0 until size.second &&
                        blizzards.none { it.posAt(time) == pos })

        fun calculateTime(start: Pair<Int, Int> = this.start, end: Pair<Int, Int> = this.end, startTime: Int = 0): Int {
            data class State(val pos: Pair<Int, Int>, val time: Int) {
                val potential = time + manhattanDist(pos.first, pos.second, end.first, end.second)
            }

            val startState = State(start, startTime)
            val toExplore = PriorityQueue<State> { a, b -> a.potential - b.potential }.apply {
                offer(startState)
            }
            val visited = mutableSetOf(startState)
            while (true) {
                val state = toExplore.poll()
                val newTime = state.time + 1
                steps.map { state.pos.first + it.first to state.pos.second + it.second }.forEach { newPos ->
                    if (newPos == end) {
                        return newTime
                    }
                    val newState = State(newPos, newTime)
                    if (!visited.contains(newState)) {
                        visited.add(newState)
                        if (isFree(newPos, newTime)) {
                            toExplore.offer(newState)
                        }
                    }
                }
            }
        }
    }

    fun parseInput(input: List<String>): Basin {
        val blizzards = mutableListOf<Blizzard>()
        val size = input.first().length - 2 to input.size - 2
        input.forEachIndexed { row, line ->
            line.forEachIndexed { col, c ->
                when (c) {
                    '>' -> blizzards.add(Blizzard(col - 1 to row - 1, 1 to 0))
                    'v' -> blizzards.add(Blizzard(col - 1 to row - 1, 0 to 1))
                    '<' -> blizzards.add(Blizzard(col - 1 to row - 1, -1 to 0))
                    '^' -> blizzards.add(Blizzard(col - 1 to row - 1, 0 to -1))
                }
            }
        }
        return Basin(blizzards, size)
    }

    fun part1(input: List<String>) = parseInput(input).calculateTime()

    fun part2(input: List<String>) = parseInput(input).let { basin ->
        val t1 = basin.calculateTime()
        val t2 = basin.calculateTime(basin.end, basin.start, t1)
        basin.calculateTime(basin.start, basin.end, t2)
    }

    val testInput = readInput(2022, 24, "test")
    check(part1((testInput)) == 18)
    check(part2((testInput)) == 54)

    val input = readInput(2022, 24)
    println(part1(input))
    println(part2(input))
}
