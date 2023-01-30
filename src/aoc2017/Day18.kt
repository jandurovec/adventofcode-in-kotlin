package aoc2017

import AdventDay
import TestCase

class Day18 : AdventDay<List<Day18.Instruction>, Long, Int>(2017, 18) {

    data class Instruction(val name: String, val args: List<String>)

    override fun parseInput(stringInput: List<String>) = stringInput.map {
        val parts = it.split(" ")
        Instruction(parts[0], parts.drop(1))
    }

    open class Program1(private val instructions: List<Instruction>, initialRegisters: Map<String, Long> = mapOf()) {
        protected val registers = initialRegisters.toMutableMap().withDefault { 0L }
        private var ip = 0L
        var lastSound = 0L
            private set

        protected fun String.value() = toLongOrNull() ?: registers.getValue(this)

        /**
         * @return true if the program has finished, false if it is waiting for input
         */
        fun run(): Boolean {
            while (ip in instructions.indices) {
                if (processInstr(instructions[ip.toInt()])) {
                    ip++
                } else {
                    return false
                }
            }
            return true
        }

        /**
         * @return true if the program should continue, false if it should stop at the current instruction
         */
        protected open fun processInstr(instr: Instruction): Boolean {
            when (instr.name) {
                "snd" -> lastSound = instr.args[0].value()
                "set" -> registers[instr.args[0]] = instr.args[1].value()
                "add" -> registers[instr.args[0]] = instr.args[0].value() + instr.args[1].value()
                "mul" -> registers[instr.args[0]] = instr.args[0].value() * instr.args[1].value()
                "mod" -> registers[instr.args[0]] = instr.args[0].value().mod(instr.args[1].value())
                "rcv" -> if (instr.args[0].value() != 0L) {
                    return false
                }

                "jgz" -> if (instr.args[0].value() > 0) {
                    ip += instr.args[1].value() - 1
                }

                else -> error("Unknown instruction $instr")
            }
            return true
        }
    }

    class Program2(
        instructions: List<Instruction>,
        private val send: ArrayDeque<Long>,
        private val receive: ArrayDeque<Long>,
        initialRegisters: Map<String, Long>
    ) : Program1(instructions, initialRegisters) {
        var sendCount = 0
            private set

        override fun processInstr(instr: Instruction): Boolean {
            when (instr.name) {
                "snd" -> send.addLast(instr.args[0].value()).also { sendCount++ }
                "rcv" -> if (receive.isEmpty()) {
                    return false
                } else {
                    registers[instr.args[0]] = receive.removeFirst()
                }

                else -> return super.processInstr(instr)
            }
            return true
        }
    }

    override fun part1(input: List<Instruction>, testArg: Any?): Long {
        val prg = Program1(input)
        prg.run()
        return prg.lastSound
    }

    override fun part2(input: List<Instruction>, testArg: Any?): Int {
        val zeroToOne = ArrayDeque<Long>()
        val oneToZero = ArrayDeque<Long>()
        val prg0 = Program2(input, zeroToOne, oneToZero, mapOf("p" to 0))
        val prg1 = Program2(input, oneToZero, zeroToOne, mapOf("p" to 1))
        var prg0Finished: Boolean
        var prg1Finished: Boolean
        do {
            prg0Finished = prg0.run()
            prg1Finished = prg1.run()
        } while ((zeroToOne.isNotEmpty() && !prg1Finished) || (oneToZero.isNotEmpty() && !prg0Finished))

        return prg1.sendCount
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 4L))
}

fun main() {
    Day18().main()
}
