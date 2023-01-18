package aoc2016

import TestCase
import UnparsedDay
import permutations

class Day21 : UnparsedDay<String, String>(2016, 21) {

    private fun String.rotate(offset: Int) = buildString {
        this@rotate.indices.forEach { i ->
            append(this@rotate[(i - offset).mod(this@rotate.length)])
        }
    }

    private fun String.perform(operation: String): String {
        if (operation.startsWith("swap position")) {
            val (x, y) = "swap position (.*) with position (.*)".toRegex().matchEntire(operation)!!.groupValues.drop(1)
                .map { it.toInt() }
            return buildString {
                this@perform.indices.forEach {
                    append(this@perform[if (it == x) y else if (it == y) x else it])
                }
            }
        } else if (operation.startsWith("swap letter")) {
            val (x, y) = "swap letter (.) with letter (.)".toRegex().matchEntire(operation)!!.groupValues.drop(1)
                .map { it.first() }
            return buildString {
                this@perform.forEach {
                    append(if (it == x) y else if (it == y) x else it)
                }
            }
        } else if (operation.startsWith("rotate right ")) {
            val offset = "rotate right (.*) steps?".toRegex().matchEntire(operation)!!.groups[1]!!.value.toInt()
            return this.rotate(offset)
        } else if (operation.startsWith("rotate left ")) {
            val offset = "rotate left (.*) steps?".toRegex().matchEntire(operation)!!.groups[1]!!.value.toInt()
            return this.rotate(-offset)
        } else if (operation.startsWith("rotate based on position of letter")) {
            val x = operation[operation.lastIndexOf(' ') + 1]
            val index = this.indexOf(x)
            return this.rotate(1 + index + if (index >= 4) 1 else 0)
        } else if (operation.startsWith("reverse positions")) {
            val (x, y) = "reverse positions (.*) through (.*)".toRegex().matchEntire(operation)!!.groupValues.drop(1)
                .map { it.toInt() }
            return buildString {
                this@perform.indices.forEach {
                    append(this@perform[if (it in x..y) y - (it - x) else it])
                }
            }
        } else if (operation.startsWith("move position")) {
            val (x, y) = "move position (.*) to position (.*)".toRegex().matchEntire(operation)!!.groupValues.drop(1)
                .map { it.toInt() }
            return buildString {
                this@perform.mapIndexedNotNull { i, c ->
                    if (i == x) null else c
                }.forEachIndexed { i, c ->
                    if (i == y) {
                        append(this@perform[x])
                    }
                    append(c)
                }
                if (y == this@perform.lastIndex) {
                    append(this@perform[x])
                }
            }
        } else {
            error("Unknown operation $operation")
        }
    }

    private fun String.scramble(instructions: List<String>) =
        instructions.fold(this) { prev, op -> prev.perform(op) }

    override fun part1(input: List<String>, testArg: Any?) =
        (if (testArg is String) testArg else "abcdefgh").scramble(input)

    override fun part2(input: List<String>, testArg: Any?): String {
        return setOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h').permutations().map { it.joinToString("") }
            .first { it.scramble(input) == "fbgdceah" }
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), "decab", "abcde"))
}

fun main() {
    Day21().main()
}
