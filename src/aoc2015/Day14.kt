package aoc2015

import AdventDay
import TestCase

class Day14 : AdventDay<List<Day14.Reindeer>, Int, Int>(2015, 14) {
    companion object {
        val INPUT_REGEX = Regex("""(.*) can fly (.*) km/s for (.*) seconds?, but then must rest for (.*) seconds?\.""")
    }

    class Reindeer(val name: String, private val speed: Int, private val flyTime: Int, restTime: Int) {
        private val cycle = flyTime + restTime
        fun distAt(seconds: Int) =
            (seconds / cycle) * speed * flyTime + minOf(seconds.mod(cycle), flyTime) * speed
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { line ->
        val (name, speed, fly, rest) = INPUT_REGEX.matchEntire(line)!!.destructured
        Reindeer(name, speed.toInt(), fly.toInt(), rest.toInt())
    }

    override fun part1(input: List<Reindeer>, testArg: Any?) =
        input.maxOf { it.distAt(if (testArg is Int) testArg else 2503) }

    override fun part2(input: List<Reindeer>, testArg: Any?) =
        (1..if (testArg is Int) testArg else 2503).fold(mutableMapOf<String, Int>()) { acc, time ->
            val distances = input.associate { it.name to it.distAt(time) }
            val max = distances.maxOf { it.value }
            distances.filter { it.value == max }.keys.forEach {
                acc[it] = acc.getOrDefault(it, 0) + 1
            }
            acc
        }.values.max()

    override fun testCases1() = listOf(TestCase(getTestInput(), 1120, 1000))
    override fun testCases2() = listOf(TestCase(getTestInput(), 689, 1000))
}


fun main() {
    Day14().main()
}
