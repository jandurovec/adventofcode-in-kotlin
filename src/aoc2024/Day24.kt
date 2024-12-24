package aoc2024

import TestCase
import UnparsedDay
import split


class Day24 : UnparsedDay<Long, String>(2024, 24) {

    private fun Map<String, Boolean>.getNumber(prefix: String): Long = keys.filter { it.startsWith(prefix) }
        .map { it.substring(prefix.length).toInt() to if (this[it]!!) "1" else "0" }
        .sortedByDescending { it.first }.joinToString("") { it.second }.toLong(2)

    private fun simulate(
        initBits: Map<String, Boolean>,
        instructions: Map<String, List<String>>
    ): Map<String, Boolean> {
        val wires = initBits.toMutableMap()
        fun getWire(name: String, unknown: Set<String> = emptySet()): Boolean = wires.getOrPut(name) {
            val (a, op, b) = instructions[name]!!
            if (name in unknown) {
                error("loop detected")
            }
            val nextUnknown = unknown + name
            val valueA = getWire(a, nextUnknown)
            val valueB = getWire(b, nextUnknown)
            when (op) {
                "AND" -> valueA and valueB
                "OR" -> valueA or valueB
                "XOR" -> valueA xor valueB
                else -> error("Unsupported operation $op")
            }
        }
        instructions.keys.filter { it.startsWith("z") }.forEach { getWire(it) }
        return wires
    }

    override fun part1(input: List<String>, testArg: Any?): Long {
        val (initBits, instructions) = input.split()
        return simulate(
            initBits.map { it.split(": ") }.associate { (name, value) -> name to (value == "1") },
            instructions.map { """(.*) (.*) (.*) -> (.*)""".toRegex().matchEntire(it)!!.groupValues.drop(1) }
                .associate { (a, op, b, res) -> res to listOf(a, op, b) }).getNumber("z")
    }

    override fun part2(input: List<String>, testArg: Any?): String {
        // first bit is x00 XOR y00
        // first carry is x00 AND y00
        // next bit is (xN XOR yN) XOR carryPrev
        // next carry is ((xN XOR yN) AND carryPrev) OR (xN AND yN)
        //
        //                  +---------+        +---------+
        //        xN --+--->|   XOR   |-+----->|   XOR   |------------> zN
        //        yN --|-+->| (gateA) | |   +->| (gateB) |
        //             | |  +---------+ |   |  +---------+
        //             | |              |   |
        // prevCarry --|-|--------+-----|---+
        //             | |        |     |  +---------+
        //             v v        |     +->|   AND   |
        //          +---------+   +------->| (gateD) |--+  +---------+
        //          |   AND   |            +---------+  +->|   OR    |--> nextCarry
        //          | (gateC) |--------------------------->| (gateE) |
        //          +---------+                            +---------+
        val (init, instStr) = input.split()
        val outputForWires =
            instStr.map { """(.*) (.*) (.*) -> (.*)""".toRegex().matchEntire(it)!!.groupValues.drop(1) }
                .associate { (a, op, b, res) -> setOf(a, op, b) to res }.toMutableMap()
        val bitNumbers = init.map { it.substringBefore(": ").substring(1) }.toSortedSet()

        val gateA = bitNumbers.map { outputForWires[setOf("x$it", "XOR", "y$it")] }
        val wrongGates = mutableSetOf<Pair<String, String>>()
        if (gateA[0] != "z00") {
            val wrongGate = gateA[0]!!
            wrongGates.add(wrongGate to "z00")
            outputForWires.entries.first { it.value == "z00" }.let { (k, _) -> outputForWires[k] = wrongGate }
            outputForWires[setOf("x00", "XOR", "y00")] = "z00"
        }
        // GateB instances are all remaining XORs
        outputForWires.filter { (k, _) -> "XOR" in k && k.none { it.startsWith("x") } }.forEach { (input, output) ->
            if (output.startsWith("z")) {
                val index = output.substring(1).toInt()
                if (gateA[index] !in input) {
                    // println("Wrong gateB: $input -> $output")
                    val inputOps =
                        (input - "XOR").associateWith { wire -> outputForWires.entries.first { it.value == wire }.key }
                    if (inputOps.count { (_, v) -> "OR" in v } == 1) {
                        // one of the operations is OR, the other should be replaced
                        val (k, _) = inputOps.entries.first { (_, v) -> "OR" !in v }
                        // find correct XOR operation
                        val correct = outputForWires[setOf(output.replace('z', 'x'), "XOR", output.replace('z', 'y'))]!!
                        // println("$k to $correct")
                        wrongGates.add(k to correct)
                    }
                }
            } else {
                // println("Wrong gateB: $input -> $output")
                (input - "XOR").firstNotNullOfOrNull { str ->
                    outputForWires.entries.firstOrNull { it.value == str && it.key.any { s -> s.startsWith("x") } }
                }?.also {
                    val correctValue = it.key.first { k -> k.startsWith("x") }.replace('x', 'z')
                    // println("$output to $correctValue")
                    wrongGates.add(output to correctValue)
                }
            }
        }
        if (wrongGates.size != 4) {
            error("Your input has different type of swaps than mine")
        }
        return wrongGates.flatMap { (from, to) -> listOf(from, to) }.sorted().joinToString(",")
    }

    override fun testCases1() = listOf(TestCase(getInput("test1"), 4L), TestCase(getInput("test2"), 2024L))

}

fun main() {
    Day24().main()
}
