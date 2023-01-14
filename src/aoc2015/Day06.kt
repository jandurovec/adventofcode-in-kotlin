package aoc2015

import AdventDay
import TestCase
import java.util.BitSet

class Day06 : AdventDay<List<Day06.Instruction>, Int, Int>(2015, 6) {
    companion object {
        const val SIZE = 1000
    }

    enum class Command { ON, OFF, TOGGLE }
    data class Instruction(val cmd: Command, val x: IntRange, val y: IntRange)

    override fun parseInput(stringInput: List<String>) =
        "(?:turn )?(.*) (.*),(.*) through (.*),(.*)".toRegex().let { cmd ->
            stringInput.map {
                val match = cmd.matchEntire(it)!!.groupValues.drop(1)
                val (x1, y1, x2, y2) = match.drop(1).map(String::toInt)
                Instruction(Command.valueOf(match.first().uppercase()), x1..x2, y1..y2)
            }
        }

    private fun BitSet.countBits() =
        generateSequence(nextSetBit(0)) { nextSetBit(it + 1) }.takeWhile { it != -1 }.count()

    override fun part1(input: List<Instruction>, testArg: Any?): Int {
        val lights = Array(SIZE) { BitSet(SIZE) }
        input.forEach { instr ->
            instr.x.forEach { x ->
                when (instr.cmd) {
                    Command.ON -> lights[x].set(instr.y.first, instr.y.last + 1)
                    Command.OFF -> lights[x].clear(instr.y.first, instr.y.last + 1)
                    Command.TOGGLE -> lights[x].flip(instr.y.first, instr.y.last + 1)
                }
            }
        }
        return lights.sumOf { it.countBits() }
    }

    override fun part2(input: List<Instruction>, testArg: Any?): Int {
        val lights = Array(SIZE) { IntArray(SIZE) }
        input.forEach { instr ->
            instr.x.forEach { x ->
                instr.y.forEach { y ->
                    when (instr.cmd) {
                        Command.ON -> lights[x][y]++
                        Command.OFF -> if (lights[x][y] > 0) {
                            lights[x][y]--
                        }

                        Command.TOGGLE -> lights[x][y] += 2
                    }
                }
            }
        }
        return lights.sumOf { it.sum() }
    }

    override fun testCases1() = listOf(
        TestCase(getInput("test"), 998_996)
    )

}

fun main() {
    Day06().main()
}
