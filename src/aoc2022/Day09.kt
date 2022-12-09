package aoc2022

import readInput
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    fun moveTail(head: Pair<Int, Int>, tail: Pair<Int, Int>): Pair<Int, Int> =
        if (abs(head.first - tail.first) > 1 || abs(head.second - tail.second) > 1) {
            val dx = if (head.first == tail.first) 0 else (head.first - tail.first).sign
            val dy = if (head.second == tail.second) 0 else (head.second - tail.second).sign
            tail.first + dx to tail.second + dy
        } else {
            tail
        }

    fun simulateRope(instructions: List<String>, ropeLength: Int = 2): Int {
        val rope = Array(ropeLength) { 0 to 0 }
        val tailVisited = mutableSetOf<Pair<Int, Int>>()
        tailVisited.add(rope.last())
        for (instr in instructions) {
            val (dir: String, dist: String) = instr.split(" ")
            val step = when (dir) {
                "R" -> 1 to 0
                "L" -> -1 to 0
                "U" -> 0 to 1
                "D" -> 0 to -1
                else -> error("Unknown direction")
            }
            repeat(dist.toInt()) {
                rope[0] = rope[0].first + step.first to rope[0].second + step.second
                for (i in 0 until rope.lastIndex) {
                    rope[i + 1] = moveTail(rope[i], rope[i+1])
                }
                tailVisited += rope.last()
            }
        }
        return tailVisited.size
    }

    fun part1(input: List<String>) = simulateRope(input)
    fun part2(input: List<String>) = simulateRope(input, 10)

    val testInput = readInput(2022, 9, "test1")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    check(part2(readInput(2022, 9, "test2")) == 36)

    val input = readInput(2022, 9)
    println(part1(input))
    println(part2(input))
}
