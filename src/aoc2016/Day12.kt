package aoc2016

import AdventDay
import TestCase

class Day12 : AdventDay<List<Day12.Instruction>, Int, Int>(2016, 12) {

    class Instruction(val name: String, val args: List<String>)
    class Computer(private val program: List<Instruction>, initialC: Int = 0) {
        val registers = mutableMapOf("a" to 0, "b" to 0, "c" to initialC, "d" to 0)
        private fun getValue(s: String) = if (s[0].isLetter()) registers[s]!! else s.toInt()

        fun run(): Computer {
            var ip = 0
            while (ip in 0..program.lastIndex) {
                val instruction = program[ip]
                when (instruction.name) {
                    "cpy" -> {
                        registers[instruction.args[1]] = getValue(instruction.args[0])
                        ip++
                    }

                    "inc" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! + 1
                        ip++
                    }

                    "dec" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! - 1
                        ip++
                    }

                    "jnz" -> ip += if (getValue(instruction.args[0]) != 0) instruction.args[1].toInt() else 1
                    else -> error("${instruction.name} not implemented")
                }
            }
            return this
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { l ->
        l.split(" ").let { Instruction(it.first(), it.drop(1)) }
    }

    override fun part1(input: List<Instruction>, testArg: Any?) = Computer(input).run().registers["a"]!!
    override fun part2(input: List<Instruction>, testArg: Any?) = Computer(input, 1).run().registers["a"]!!

    override fun testCases1() = listOf(
        TestCase(getTestInput(), 42)
    )
}

fun main() {
    Day12().main()
}
