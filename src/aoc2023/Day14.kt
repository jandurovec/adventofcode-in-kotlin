package aoc2023

import AdventDay
import Point
import TestCase


class Day14 : AdventDay<Day14.Platform, Int, Int>(2023, 14) {
    data class Platform(
        private val h: Int,
        private val w: Int,
        private val fixed: Set<Point>,
        private val moving: Set<Point>
    ) {
        private fun tilt(order: Iterable<Point>, step: (Point) -> Point, valid: (Point) -> Boolean): Platform {
            val movedRocks = mutableSetOf<Point>()
            order.filter { moving.contains(it) }.forEach { point ->
                var cur = point
                var next = step(cur)
                while (valid(next) && !fixed.contains(next) && !movedRocks.contains(next)) {
                    cur = next
                    next = step(cur)
                }
                movedRocks.add(cur)
            }
            return Platform(h, w, fixed, movedRocks)
        }

        fun tiltNorth() = tilt((h downTo 1).flatMap { y -> (1..w).map { x -> Point(x, y) } }, Point::up) { it.y <= h }
        fun tiltWest() = tilt((1..w).flatMap { x -> (1..h).map { y -> Point(x, y) } }, Point::left) { it.x > 0 }
        fun tiltSouth() = tilt((1..h).flatMap { y -> (1..w).map { x -> Point(x, y) } }, Point::down) { it.y > 0 }
        fun tiltEast() = tilt((w downTo 1).flatMap { x -> (1..h).map { y -> Point(x, y) } }, Point::right) { it.x <= w }

        fun load() = moving.sumOf { it.y }
    }

    override fun parseInput(stringInput: List<String>): Platform {
        val h = stringInput.size
        val w = stringInput[0].length
        val fixed = mutableSetOf<Point>()
        val moving = mutableSetOf<Point>()
        stringInput.flatMapIndexed { row: Int, line: String ->
            line.mapIndexed { col, c -> Point(col + 1, h - row) to c }
        }.filter { it.second != '.' }.forEach { (p, c) -> if (c == '#') fixed.add(p) else moving.add(p) }
        return Platform(h, w, fixed, moving)
    }

    override fun part1(input: Platform, testArg: Any?) = input.tiltNorth().load()

    override fun part2(input: Platform, testArg: Any?): Int {
        val cycles = 1000000000
        var step = 0
        val platformToStep = mutableMapOf(input to 0)
        val stepToPlatform = mutableListOf(input)
        var cur = input
        while (step++ < cycles) {
            val next = cur.tiltNorth().tiltWest().tiltSouth().tiltEast()
            val seenAt = platformToStep[next]
            if (seenAt != null) {
                cur = stepToPlatform[seenAt + (cycles - step) % (step - seenAt)]
                break
            } else {
                platformToStep[next] = step
                stepToPlatform += next
                cur = next
            }
        }
        return cur.load()
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 136))

    override fun testCases2() = listOf(TestCase(getTestInput(), 64))
}

fun main() {
    Day14().main()
}
