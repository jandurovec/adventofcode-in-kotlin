package aoc2015

import AdventDay
import TestCase

class Day02 : AdventDay<List<Day02.Box>, Int, Int>(2015, 2) {
    class Box(def: String) {
        val l: Int
        val w: Int
        val h: Int

        init {
            val (ll, ww, hh) = def.split("x").map(String::toInt)
            l = ll; w = ww; h = hh
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { Box(it) }
    override fun part1(input: List<Box>, testArg: Any?) =
        input.sumOf { b -> listOf(b.l * b.w, b.w * b.h, b.h * b.l).let { 2 * it.sum() + it.min() } }

    override fun part2(input: List<Box>, testArg: Any?) =
        input.sumOf { b -> 2 * minOf(b.l + b.w, b.w + b.h, b.h + b.l) + b.l * b.w * b.h }

    override fun testCases1() = listOf(
        TestCase(listOf(Box("2x3x4")), 58),
        TestCase(listOf(Box("1x1x10")), 43)
    )

    override fun testCases2() = listOf(
        TestCase(listOf(Box("2x3x4")), 34),
        TestCase(listOf(Box("1x1x10")), 14)
    )
}

fun main() {
    Day02().main()
}
