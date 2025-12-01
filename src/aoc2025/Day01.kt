package aoc2025

import AdventDay
import TestCase
import java.util.function.Function
import kotlin.math.absoluteValue

class Day01 : AdventDay<List<Int>, Int, Int>(2025, 1) {
    override fun parseInput(stringInput: List<String>): List<Int> {
        return stringInput.map { s ->
            val direction = s[0]
            val distance = s.substring(1).toInt()
            if (direction == 'L') -distance else distance
        }
    }

    fun processRotations(input: List<Int>, resultIncrement: Function<Pair<Int, Int>, Int>): Int {
        var pos = 50
        var result = 0
        input.forEach { rotation ->
            pos = (pos + rotation).mod(100)
            result += resultIncrement.apply(rotation to pos)
        }
        return result
    }

    override fun part1(input: List<Int>, testArg: Any?) =
        processRotations(input) { (_, pos) -> if (pos == 0) 1 else 0 }

    override fun part2(input: List<Int>, testArg: Any?) =
        processRotations(input) { (rotation, pos) ->
            val fullRotations = rotation.absoluteValue / 100
            val remainder = rotation.absoluteValue % 100
            fullRotations + if (remainder > 0 &&
                if (rotation > 0) {
                    0 in pos - remainder + 1..pos
                } else {
                    pos == 0 || 100 in pos..<pos + remainder
                }
            ) 1 else 0
        }

    override fun testCases1() = listOf(TestCase(getTestInput(), 3))

    override fun testCases2() = listOf(TestCase(getTestInput(), 6))

}

fun main() {
    Day01().main()
}
