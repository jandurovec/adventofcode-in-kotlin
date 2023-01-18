package aoc2016

import AdventDay
import TestCase

class Day15 : AdventDay<List<Day15.Disc>, Int, Int>(2016, 15) {
    data class Disc(val size: Int, val pos0: Int)

    override fun parseInput(stringInput: List<String>) = stringInput.map { s ->
        """Disc #.* has (.*) positions; at time=0, it is at position (.*)\.""".toRegex().matchEntire(s)!!.groupValues
            .drop(1).map { it.toInt() }.let { (size, pos) -> Disc(size, pos) }
    }

    override fun part1(input: List<Disc>, testArg: Any?) = input.foldIndexed(0 to 1) { i, acc, disc ->
        var (t, cycle) = acc
        while (t.mod(disc.size) != (-(i + 1) - disc.pos0).mod(disc.size)) {
            t += cycle
        }
        cycle *= disc.size // normally lcm, but disc sizes are primes
        t.mod(cycle) to cycle
    }.first

    override fun part2(input: List<Disc>, testArg: Any?) = part1(input + Disc(11, 0))

    override fun testCases1() = listOf(TestCase(getTestInput(), 5))
}

fun main() {
    Day15().main()
}
