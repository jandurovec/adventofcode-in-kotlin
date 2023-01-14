package aoc2015

import AdventDay
import TestCase
import factorize

class Day20 : AdventDay<Int, Int, Int>(2015, 20) {
    override fun parseInput(stringInput: List<String>) = stringInput.first().toInt()

    private fun divisors(n: Int): Sequence<Int> {
        val primeExponents = n.factorize()
        fun divisorSequence(exp: Map<Int, Int>, aggregate: Int = 1): Sequence<Int> = sequence {
            if (exp.isEmpty()) {
                yield(aggregate)
            } else {
                val (p, e) = exp.entries.first()
                var pp = 1
                val newExp = exp.minus(p)
                repeat(e + 1) {
                    yieldAll(divisorSequence(newExp, aggregate * pp))
                    pp *= p
                }
            }
        }
        return divisorSequence(primeExponents)
    }

    override fun part1(input: Int, testArg: Any?): Int {
        return generateSequence(1) { it + 1 }.first { house ->
            10 * divisors(house).sum() >= input
        }
    }

    override fun part2(input: Int, testArg: Any?): Int {
        return generateSequence(1) { it + 1 }.first { house ->
            11 * divisors(house).filter { it * 50 >= house }.sum() >= input
        }
    }

    override fun testCases1() = listOf(TestCase(100, 6))
}

fun main() {
    Day20().main()
}
