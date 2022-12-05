package aoc2021

import readInput

typealias Cavern = MutableMap<Pair<Int, Int>, Int>

fun main() {
    val FLASH_ENERGY = 10
    fun Cavern.neighbors(pos: Pair<Int, Int>) = listOf(
        0 to 1,
        1 to 1,
        1 to 0,
        1 to -1,
        0 to -1,
        -1 to -1,
        -1 to 0,
        -1 to 1
    ).map { pos.first + it.first to pos.second + it.second }.filter { containsKey(it) }

    /**
     * Updates the cavern by one step and returns the number of flashes in this step
     */
    fun Cavern.step(): Int {
        replaceAll { _, v -> v + 1 }
        val toFlash = filter { it.value == FLASH_ENERGY }.keys.toMutableList()
        var flashCount = 0
        while (toFlash.isNotEmpty()) {
            flashCount++
            neighbors(toFlash.removeFirst()).forEach {
                if (merge(it, 1, Int::plus) == FLASH_ENERGY) {
                    toFlash.add(it)
                }
            }
        }
        replaceAll { _, v -> if (v >= FLASH_ENERGY) 0 else v }
        return flashCount
    }

    fun parseInput(input: List<String>) =
        input.flatMapIndexed { row, r -> r.mapIndexed { col, c -> (row to col) to c.digitToInt() } }.toMap()
            .toMutableMap()

    fun part1(input: List<String>): Int {
        val cavern = parseInput(input)
        return (1..100).sumOf { cavern.step() }
    }

    fun part2(input: List<String>): Int {
        val cavern = parseInput(input)
        var steps = 1
        while (cavern.step() != cavern.size) {
            steps++
        }
        return steps
    }

    val testInput = readInput(2021, 11, "test")
    check(part1(testInput) == 1_656)
    check(part2(testInput) == 195)

    val input = readInput(2021, 11)
    println(part1(input))
    println(part2(input))
}
