package aoc2021

import readInput
import java.util.*
import kotlin.streams.asSequence

fun main() {
    data class Instruction(
        val cmd: String, val fromX: Int, val toX: Int, val fromY: Int, val toY: Int, val fromZ: Int, val toZ: Int
    )

    fun parseInput(input: List<String>): List<Instruction> {
        val regex = """(.+) x=(-?[0-9]+)..(-?[0-9]+),y=(-?[0-9]+)..(-?[0-9]+),z=(-?[0-9]+)..(-?[0-9]+)""".toRegex()
        return input.map {
            val (cmd, fromX, toX, fromY, toY, fromZ, toZ) = regex.matchEntire(it)?.destructured!!
            Instruction(cmd, fromX.toInt(), toX.toInt(), fromY.toInt(), toY.toInt(), fromZ.toInt(), toZ.toInt())
        }
    }

    fun reboot(input: List<Instruction>): Long {
        val fromX = input.flatMap { listOf(it.fromX, it.toX + 1) }.toSortedSet()
        val toX = input.flatMap { listOf(it.fromX - 1, it.toX) }.toSortedSet()
        val xSteps = (fromX.take(fromX.size - 1) zip toX.drop(1)).toTypedArray()

        val fromY = input.flatMap { listOf(it.fromY, it.toY + 1) }.toSortedSet()
        val toY = input.flatMap { listOf(it.fromY - 1, it.toY) }.toSortedSet()
        val ySteps = (fromY.take(fromY.size - 1) zip toY.drop(1)).toTypedArray()

        val fromZ = input.flatMap { listOf(it.fromZ, it.toZ + 1) }.toSortedSet()
        val toZ = input.flatMap { listOf(it.fromZ - 1, it.toZ) }.toSortedSet()
        val zSteps = (fromZ.take(fromZ.size - 1) zip toZ.drop(1)).toTypedArray()

        val reactor = Array(xSteps.size) { Array(ySteps.size) { BitSet(zSteps.size) } }
        input.forEach { instr ->
            val relevantX = xSteps.indices.filter { xSteps[it].first >= instr.fromX && xSteps[it].second <= instr.toX }
            val relevantY = ySteps.indices.filter { ySteps[it].first >= instr.fromY && ySteps[it].second <= instr.toY }
            val relevantZ = zSteps.indices.filter { zSteps[it].first >= instr.fromZ && zSteps[it].second <= instr.toZ }
            for (x in relevantX) {
                for (y in relevantY) {
                    for (z in relevantZ) {
                        if (instr.cmd == "on") {
                            reactor[x][y].set(z)
                        } else {
                            reactor[x][y].clear(z)
                        }
                    }
                }
            }
        }
        return xSteps.indices.sumOf { x ->
            ySteps.indices.sumOf { y ->
                reactor[x][y].stream().asSequence().sumOf { z ->
                    (1L + xSteps[x].second - xSteps[x].first) * (1L + ySteps[y].second - ySteps[y].first) * (1L + zSteps[z].second - zSteps[z].first)
                }
            }
        }
    }

    fun part1(steps: List<Instruction>): Long {
        val rng = -50..50
        return reboot(steps.filter {
            (it.fromX in rng || it.toX in rng) && (it.fromY in rng || it.toY in rng) && (it.fromZ in rng || it.toZ in rng)
        }.map {
            Instruction(
                it.cmd,
                maxOf(rng.first, it.fromX),
                minOf(rng.last, it.toX),
                maxOf(rng.first, it.fromY),
                minOf(rng.last, it.toY),
                maxOf(rng.first, it.fromZ),
                minOf(rng.last, it.toZ)
            )
        })
    }

    fun part2(steps: List<Instruction>) = reboot(steps)

    check(part1(parseInput(readInput(2021, 22, "test1"))) == 39L)
    check(part1(parseInput(readInput(2021, 22, "test2"))) == 590_784L)
    check(part2(parseInput(readInput(2021, 22, "test3"))) == 2_758_514_936_282_235)

    val input = parseInput(readInput(2021, 22))
    println(part1(input))
    println(part2(input))
}
