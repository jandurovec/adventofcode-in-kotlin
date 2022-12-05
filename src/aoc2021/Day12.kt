package aoc2021

import isLowerCase
import readInput

fun main() {
    val START = "start"
    val END = "end"

    fun parseInput(input: List<String>) =
        input.map { it.split('-') }.fold(mutableMapOf<String, MutableList<String>>()) { map, path ->
            map.merge(path[0], mutableListOf(path[1])) { l1, l2 -> l1.addAll(l2); l1 }
            map.merge(path[1], mutableListOf(path[0])) { l1, l2 -> l1.addAll(l2); l1 }
            map
        }

    fun findPaths(
        pos: String,
        map: Map<String, List<String>>,
        forbidden: Set<String> = setOf(pos),
        canVisitForbidden: Boolean = false
    ): Int {
        return if (pos == END) {
            1
        } else {
            map[pos]!!.sumOf { node ->
                if (!forbidden.contains(node)) {
                    findPaths(node, map, if (node.isLowerCase()) forbidden.plus(node) else forbidden, canVisitForbidden)
                } else if (canVisitForbidden && node != START) {
                    findPaths(node, map, forbidden, false)
                } else {
                    0
                }
            }
        }
    }

    fun part1(input: List<String>): Int = findPaths(START, parseInput(input))

    fun part2(input: List<String>): Int = findPaths(START, parseInput(input), canVisitForbidden = true)

    val testInput1 = readInput(2021, 12, "test1")
    check(part1(testInput1) == 10)
    check(part2(testInput1) == 36)

    val testInput2 = readInput(2021, 12, "test2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)

    val testInput3 = readInput(2021, 12, "test3")
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3_509)

    val input = readInput(2021, 12)
    println(part1(input))
    println(part2(input))
}
