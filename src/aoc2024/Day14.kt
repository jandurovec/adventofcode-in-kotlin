package aoc2024

import AdventDay
import TestCase
import kotlin.math.sign


class Day14 : AdventDay<List<Day14.Robot>, Int, Int>(2024, 14) {

    companion object {
        const val WIDTH = 101
        const val HEIGHT = 103
    }

    private data class Dimensions(val width: Int, val height: Int)

    data class Robot(val px: Int, val py: Int, val vx: Int, val vy: Int) {
        fun move(w: Int, h: Int) = Robot((px + vx).mod(w), (py + vy).mod(h), vx, vy)
    }

    fun List<Robot>.sequence(width: Int, height: Int) = generateSequence(this) { it.map { r -> r.move(width, height) } }

    override fun parseInput(stringInput: List<String>) = stringInput.map { line ->
        """p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""".toRegex().matchEntire(line)!!.groupValues.drop(1)
            .map { it.toInt() }.let { (px, py, vx, vy) -> Robot(px, py, vx, vy) }
    }

    override fun part1(input: List<Robot>, testArg: Any?): Int {
        val (width, height) = if (testArg is Dimensions) testArg else Dimensions(WIDTH, HEIGHT)
        return input.sequence(width, height).take(101).last()
            .groupBy { ((width - 1) / 2 - it.px).sign to ((height - 1) / 2 - it.py).sign }
            .filter { it.key.first != 0 && it.key.second != 0 }
            .map { it.value.size }.reduce(Int::times)
    }

    override fun part2(input: List<Robot>, testArg: Any?): Int {
        return input.sequence(WIDTH, HEIGHT).mapIndexed { step, robots -> step to robots }
            .first { (_, robots) ->
                robots.map { it.px to it.py }.toSet().size == robots.size
            }.let { (steps, robots) ->
                for (y in 0 until HEIGHT) {
                    for (x in 0 until WIDTH) {
                        print(if (robots.any { it.px == x && it.py == y }) '#' else '.')
                    }
                    println()
                }
                steps
            }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 12, Dimensions(11, 7)))
}

fun main() {
    Day14().main()
}
