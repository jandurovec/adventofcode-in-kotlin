package aoc2015

import AdventDay

class Day23 : AdventDay<List<Day23.Instruction>, Int, Int>(2015, 23) {
    companion object {
        val REGEX = ",? ".toRegex()
    }

    class Instruction(val name: String, val args: List<String>)
    class Computer(val program: List<Instruction>, initialA: Int = 0) {
        val registers = mutableMapOf("a" to initialA, "b" to 0)
        fun run(): Computer {
            var ip = 0
            while (ip in 0..program.lastIndex) {
                val instruction = program[ip]
                when (instruction.name) {
                    "hlf" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! / 2
                        ip++
                    }

                    "tpl" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! * 3
                        ip++
                    }

                    "inc" -> {
                        registers[instruction.args[0]] = registers[instruction.args[0]]!! + 1
                        ip++
                    }

                    "jmp" -> ip += instruction.args[0].toInt()
                    "jie" -> ip += if (registers[instruction.args[0]]!! % 2 == 0) instruction.args[1].toInt() else 1
                    "jio" -> ip += if (registers[instruction.args[0]]!! == 1) instruction.args[1].toInt() else 1
                    else -> error("${instruction.name} not implemented")
                }
            }
            return this
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { l ->
        l.split(REGEX).let { Instruction(it.first(), it.drop(1)) }
    }

    override fun part1(input: List<Instruction>, testArg: Any?) = Computer(input).run().registers["b"]!!
    override fun part2(input: List<Instruction>, testArg: Any?) = Computer(input, 1).run().registers["b"]!!
}

fun main() {
    Day23().main()
}
