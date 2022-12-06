package aoc2022

import readInput

fun main() {
    fun findPacketMarker(input: String, len: Int) = input.windowedSequence(len)
        .indexOfFirst { it.toSet().size == len } + len

    fun part1(input: String) = findPacketMarker(input, 4)
    fun part2(input: String) = findPacketMarker(input, 14)

    val testInput = readInput(2022, 6, "test").first()
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput(2022, 6).first()
    println(part1(input))
    println(part2(input))
}
