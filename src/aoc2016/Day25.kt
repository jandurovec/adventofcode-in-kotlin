package aoc2016

import UnparsedDay

class Day25 : UnparsedDay<Int, Unit>(2016, 25) {

    override fun part1(input: List<String>, testArg: Any?): Int {
        val analyzed = getInput("analyzed").map { it.substring(0, it.indexOf('#')).trim().toRegex() }
        if (input.size != analyzed.size || input.mapIndexed { i, s -> i to s }
                .any { (i, s) -> !s.trim().matches(analyzed[i]) }) {
            error("Input does not matched the analyzed one")
        }
        // The analyzed program calculates d = "a + X * Y" and then keeps printing its binary representation
        // (from the least significant bit) in a loop.
        // I.e. we're looking for a number that has binary representation of alternating 1 and 0
        val x = analyzed[1].matchEntire(input[1])!!.groups[1]!!.value.toInt()
        val y = analyzed[2].matchEntire(input[2])!!.groups[1]!!.value.toInt()
        val binCheck = "^(10)+$".toRegex()
        return generateSequence(0) { it + 1 }.first { (it + x * y).toString(2).matches(binCheck) }
    }

    override fun part2(input: List<String>, testArg: Any?) {}
}

fun main() {
    Day25().main()
}
