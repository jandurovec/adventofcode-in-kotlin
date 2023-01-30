package aoc2017

import AdventDay
import TestCase

class Day08 : AdventDay<List<Day08.Instruction>, Int, Int>(2017, 8) {
    companion object {
        val INPUT_REGEX = """(.*) (.*) (.*) if (.*) (.*) (.*)""".toRegex()
    }

    data class Instruction(
        val target: String,
        val increment: Int,
        val checkReg: String,
        val check: (Int, Int) -> Boolean,
        val checkVal: Int
    ) {
        fun apply(reg: MutableMap<String, Int>) {
            if (check(reg.getValue(checkReg), checkVal)) {
                reg[target] = reg.getValue(target) + increment
            }
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { l ->
        val (target, op, increment, checkReg, checkOp, checkVal) = INPUT_REGEX.matchEntire(l)!!.destructured
        Instruction(target, (if (op == "inc") 1 else -1) * increment.toInt(), checkReg,
            when (checkOp) {
                ">" -> { a, b -> a > b }
                "<" -> { a, b -> a < b }
                ">=" -> { a, b -> a >= b }
                "<=" -> { a, b -> a <= b }
                "==" -> { a, b -> a == b }
                "!=" -> { a, b -> a != b }
                else -> error("Unknown operation $checkOp")
            },
            checkVal.toInt())
    }

    private fun regSequence(instructions: List<Instruction>) = sequence {
        val registers = mutableMapOf<String, Int>().withDefault { 0 }
        instructions.forEach {
            it.apply(registers)
            yield(registers)
        }
    }

    override fun part1(input: List<Instruction>, testArg: Any?) = regSequence(input).last().maxOf { it.value }
    override fun part2(input: List<Instruction>, testArg: Any?) =
        regSequence(input).maxOf { r -> r.maxOfOrNull { it.value } ?: 0 }

    override fun testCases1() = listOf(TestCase(getTestInput(), 1))
    override fun testCases2() = listOf(TestCase(getTestInput(), 10))
}

fun main() {
    Day08().main()
}
