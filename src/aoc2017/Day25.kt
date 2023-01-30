package aoc2017

import AdventDay
import TestCase

class Day25 : AdventDay<Day25.Blueprint, Int, Unit>(2017, 25) {
    data class Blueprint(val startState: String, val steps: Int, val rules: Map<String, Map<Int, Step>>)
    data class Step(val value: Int, val increment: Int, val newState: String)

    private fun String.valFromRegex(regex: String) = regex.toRegex().matchEntire(this)!!.groupValues.drop(1).first()

    override fun parseInput(stringInput: List<String>): Blueprint {
        val startState = stringInput[0].valFromRegex("""Begin in state (.*)\.""")
        val steps = stringInput[1].valFromRegex("""Perform a diagnostic checksum after (.*) steps?\.""").toInt()
        val rules = stringInput.asSequence().drop(2).map { it.trim() }.chunked(10).map { stateDesc ->
            val state = stateDesc[1].valFromRegex("In state (.*):")
            val valueRules = stateDesc.drop(2).chunked(4).map { ruleDesc ->
                val value = ruleDesc[0].valFromRegex("If the current value is (.*):").toInt()
                value to Step(
                    ruleDesc[1].valFromRegex("""- Write the value (.*)\.""").toInt(),
                    if (ruleDesc[2].valFromRegex("""- Move one slot to the (.*)\.""") == "right") 1 else -1,
                    ruleDesc[3].valFromRegex("""- Continue with state (.*)\.""")
                )
            }.associate { it }
            state to valueRules
        }.associate { it }
        return Blueprint(startState, steps, rules)
    }

    override fun part1(input: Blueprint, testArg: Any?): Int {
        with(input) {
            val tape = mutableSetOf<Int>()
            var state = startState
            var pos = 0
            repeat(steps) {
                val step = rules[state]!![if (tape.contains(pos)) 1 else 0]!!
                if (step.value == 1) {
                    tape.add(pos)
                } else {
                    tape.remove(pos)
                }
                pos += step.increment
                state = step.newState
            }
            return tape.size
        }
    }

    override fun part2(input: Blueprint, testArg: Any?) {}

    override fun testCases1() = listOf(TestCase(getTestInput(), 3))
}

fun main() {
    Day25().main()
}
