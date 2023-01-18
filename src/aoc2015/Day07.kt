package aoc2015

import AdventDay
import TestCase

class Day07 : AdventDay<List<Pair<String, String>>, Int, Int>(2015, 7) {

    override fun parseInput(stringInput: List<String>) =
        stringInput.map {
            it.split(" -> ".toRegex()).let { (def, res) ->
                res to def
            }
        }

    class Circuit(def: List<Pair<String, String>>) {
        inner class Gate(val def: String) {
            private var signal: Int? = null
            private fun getValue(s: String) = s.toIntOrNull() ?: data[s]!!.signal()
            fun signal(): Int = signal ?: when {
                def.startsWith("NOT ") -> getValue(def.substring(4)).inv() and 0xffff
                !def.contains(" ") -> getValue(def)
                else -> def.split(" ").let { (a, op, b) ->
                    when (op) {
                        "AND" -> getValue(a) and getValue(b)
                        "OR" -> getValue(a) or getValue(b)
                        "LSHIFT" -> getValue(a) shl getValue(b)
                        "RSHIFT" -> getValue(a) shr getValue(b)
                        else -> error("Unsupported operation $op")
                    }
                }
            }.also { signal = it }
        }

        val data = def.associate { it.first to Gate(it.second) }
    }

    override fun part1(input: List<Pair<String, String>>, testArg: Any?) = Circuit(input).data["a"]!!.signal()
    override fun part2(input: List<Pair<String, String>>, testArg: Any?): Int {
        val bValue = part1(input, null)
        return input.map { i -> if (i.first == "b") ("b" to bValue.toString()) else i }.let {
            Circuit(it).data["a"]!!.signal()
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 65079))
}

fun main() {
    Day07().main()
}
