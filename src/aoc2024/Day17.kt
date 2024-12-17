package aoc2024

import AdventDay
import TestCase
import split

class Day17 : AdventDay<Day17.Computer, String, Long>(2024, 17) {

    class Computer(private val registers: LongArray, val program: List<Int>) {
        private var ip = 0
        private fun combo(value: Int) = when (value) {
            in 0..3 -> value.toLong()
            in 4..6 -> registers[value - 4]
            else -> error("Invalid combo operand $value")
        }

        fun run() = sequence {
            while (ip < program.size) {
                when (val opcode = program[ip]) {
                    0, 6, 7 -> registers[if (opcode == 0) 0 else opcode - 5] =
                        registers[0] / (1L shl combo(program[ip + 1]).toInt()) // adv, bdv, cdv
                    1 -> registers[1] = registers[1] xor program[ip + 1].toLong()  // bxl
                    2 -> registers[1] = combo(program[ip + 1]).mod(8L) // bst
                    3 -> if (registers[0] != 0L) { //jnx
                        ip = program[ip + 1] - 2 // -2 is compensation for increment at the end of the loop
                    }

                    4 -> registers[1] = registers[1] xor registers[2] // bxc
                    5 -> yield(combo(program[ip + 1]).mod(8))
                    else -> error("Invalid opcode $opcode")
                }
                ip += 2
            }
        }
    }

    override fun parseInput(stringInput: List<String>): Computer {
        val (registers, program) = stringInput.split()
        return Computer(
            registers.map { it.substringAfter(": ").toLong() }.toLongArray(),
            program.first().substringAfter(": ").split(",").map { it.toInt() })
    }

    override fun part1(input: Computer, testArg: Any?) = input.run().joinToString(",")

    /*
        My Input does the following
           1) takes last 3 bits of register A (=> register B)
           2) inverts them
           3) takes 3 bits from position calculated in step 2 (=> register C)
           4) (inverts bytes from step 1 back)
           5) outputs "register B XOR register C"
           6) shifts register A by 3 bits to the right and repeat, until there's nothing left

         Therefore, we try to reconstruct the number by filling the known bit mask from the right, because
         for known "last 3 bits" the bits at corresponding "position for register C value" also have known values.
     */
    override fun part2(input: Computer, testArg: Any?): Long {
        fun Int.matches(mask: Map<Int, Boolean>) = mask.all { ((this shr it.key) and 1 == 1) == it.value }
        fun Int.toMask() = mapOf(0 to (this and 1 == 1), 1 to (this and 2 == 2), 2 to (this and 4 == 4))
        fun findNumber(
            program: List<Int>,
            known: Map<Int, Boolean> = emptyMap(),
            offset: Int = 0
        ): Sequence<Map<Int, Boolean>> =
            sequence {
                if (offset == program.size) {
                    if (known.filter { it.value }.keys.all { it < 3 * program.size }) {
                        yield(known)
                    }
                } else {
                    val bMask =
                        known.map { it.key - 3 * offset to it.value }.filter { it.first in 0..2 }.toMap()
                    (0..7).filter { it.matches(bMask) }.forEach { b ->
                        val newKnown = known.toMutableMap()
                        newKnown += b.toMask().map { (it.key + 3 * offset) to it.value }
                        val cOffset = b xor 0b111
                        val cMask =
                            newKnown.map { (it.key - 3 * offset - cOffset) to it.value }.filter { it.first in 0..2 }
                                .toMap()
                        val expectedC = program[offset] xor b
                        if (expectedC.matches(cMask)) {
                            newKnown += expectedC.toMask().map { (it.key + 3 * offset + cOffset) to it.value }
                            yieldAll(findNumber(program, newKnown, offset + 1))
                        }
                    }
                }
            }

        return findNumber(input.program).map { bits ->
            bits.entries.fold(0L) { acc, (key, value) -> acc or (if (value) 1L shl key else 0) }
        }.sorted().first()
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), "4,6,3,5,6,3,5,2,1,0"))

}

fun main() {
    Day17().main()
}
