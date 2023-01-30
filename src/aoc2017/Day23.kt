package aoc2017

import Primes
import UnparsedDay

class Day23 : UnparsedDay<Int, Int>(2017, 23) {

    data class Instruction(val name: String, val args: List<String>)


    open class Program2(private val instructions: List<Instruction>, initialRegisters: Map<String, Int> = mapOf()) {
        private val registers = initialRegisters.toMutableMap().withDefault { 0 }
        private var ip = 0
        private fun String.value() = toIntOrNull() ?: registers.getValue(this)

        fun run() = sequence {
            while (ip in instructions.indices) {
                yield(ip)
                with(instructions[ip]) {
                    when (name) {
                        "set" -> registers[args[0]] = args[1].value()
                        "sub" -> registers[args[0]] = args[0].value() - args[1].value()
                        "mul" -> registers[args[0]] = args[0].value() * args[1].value()

                        "jnz" -> if (args[0].value() != 0) {
                            ip += args[1].value() - 1
                        }

                        else -> error("Unknown instruction $this")
                    }
                }
                ip++
            }
        }
    }

    override fun part1(input: List<String>, testArg: Any?): Int {
        val instructions = input.map {
            val parts = it.split(" ")
            Instruction(parts[0], parts.drop(1))
        }

        return Program2(instructions).run().count { instructions[it].name == "mul" }
    }

    override fun part2(input: List<String>, testArg: Any?): Int {
        fun List<String>.normalize() =
            joinToString(";") { it.indexOf("#").let { pos -> if (pos >= 0) it.substring(0, pos) else it }.trim() }

        val actual = input.normalize()
        val expectedRegex = getInput("analyzed").normalize().toRegex()
        expectedRegex.matchEntire(actual)?.let { matchResult ->
            val baseNum = matchResult.groups[1]!!.value.toInt()
            val from = 100_000 + 100 * baseNum
            val to = from + 17_000
            val primes = Primes().takeWhile { it <= to }.toSet()
            return (from..to step 17).count { !primes.contains(it) }
        }
        error("Actual program does not match the analyzed one")
    }
}

fun main() {
    Day23().main()
}
