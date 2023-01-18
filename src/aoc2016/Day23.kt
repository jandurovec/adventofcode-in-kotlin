package aoc2016

import AdventDay
import TestCase

class Day23 : AdventDay<List<Day23.Instruction>, Int, Int>(2016, 23) {

    data class Instruction(val name: String, val args: List<String>)
    class Computer(prg: List<Instruction>, initialA: Int = 0) {
        private val program = prg.toTypedArray()
        val registers = mutableMapOf("a" to initialA, "b" to 0, "c" to 0, "d" to 0)
        private fun getValue(s: String) = if (s[0].isLetter()) registers[s]!! else s.toInt()

        fun run(): Computer {
            var ip = 0
            while (ip in 0..program.lastIndex) {
                val instruction = program[ip]
                when (instruction.name) {
                    "cpy" -> {
                        // (part 2 optimization: check for multiplication)
                        val next = (ip..ip + 5).filter { it in 0..program.lastIndex }.map { program[it] }
                            .joinToString(";") { it.name + " " + it.args.joinToString(" ") }
                        val match =
                            """cpy (.) (.);inc (.);dec \2;jnz \2 -2;dec (.);jnz \4 -5""".toRegex().matchEntire(next)
                        if (match != null) {
                            val (op1, tmp, result, op2) = match.destructured
                            registers[result] = registers[result]!! + getValue(op1) * getValue(op2)
                            registers[tmp] = 0
                            registers[op2] = 0
                            ip += 6
                        } else {
                            if (instruction.args[1][0].isLetter()) {
                                registers[instruction.args[1]] = getValue(instruction.args[0])
                            }
                            ip++
                        }
                    }

                    "inc" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! + 1
                        ip++
                    }

                    "dec" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! - 1
                        ip++
                    }

                    "jnz" -> ip += if (getValue(instruction.args[0]) != 0) getValue(instruction.args[1]) else 1
                    "tgl" -> {
                        val target = ip + getValue(instruction.args[0])
                        if (target in 0..program.lastIndex) {
                            program[target] = program[target].let {
                                if (it.args.size == 1) {
                                    Instruction(if (it.name == "inc") "dec" else "inc", it.args)
                                } else {
                                    Instruction(if (it.name == "jnz") "cpy" else "jnz", it.args)
                                }
                            }
                        }
                        ip += 1
                    }

                    else -> error("${instruction.name} not implemented")
                }
            }
            return this
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { l ->
        l.split(" ").let { Instruction(it.first(), it.drop(1)) }
    }

    override fun part1(input: List<Instruction>, testArg: Any?) =
        Computer(input, if (testArg is Int) testArg else 7).run().registers["a"]!!

    override fun part2(input: List<Instruction>, testArg: Any?) = part1(input, 12)

    override fun testCases1() = listOf(TestCase(getTestInput(), 3))
}

fun main() {
    Day23().main()
}
