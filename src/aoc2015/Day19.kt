package aoc2015

import AdventDay
import TestCase

class Day19 : AdventDay<Day19.Input, Int, Int>(2015, 19) {

    data class Input(val molecule: String, val replacements: List<Pair<String, String>>)

    override fun parseInput(stringInput: List<String>) =
        Input(stringInput.last(), stringInput.dropLast(2).map { it.split(Regex(" => ")) }.map { (l, r) -> l to r })

    private fun String.replace(replacements: List<Pair<String, String>>) = buildSet {
        replacements.forEach { (old, new) ->
            var start = 0
            while (true) {
                val i = indexOf(old, start)
                if (i != -1) {
                    add(substring(0, i) + new + substring(i + old.length))
                    start = i + 1
                } else {
                    break
                }
            }
        }
    }

    override fun part1(input: Input, testArg: Any?) = input.molecule.replace(input.replacements).size

    override fun part2(input: Input, testArg: Any?): Int {
        val replacements = input.replacements.sortedByDescending { it.second }
        fun stepsTo(molecule: String, steps: Int = 0): Sequence<Int> =
            sequence {
                if (molecule == "e") {
                    yield(steps)
                }
                replacements.forEach {
                    var start = molecule.indexOf(it.second)
                    while (start != -1) {
                        val prevMolecule =
                            molecule.substring(0, start) + it.first + molecule.substring(start + it.second.length)
                        yieldAll(stepsTo(prevMolecule, steps + 1))
                        start = molecule.indexOf(it.second, start + 1)
                    }
                }

            }
        return stepsTo(input.molecule).first()
    }

    override fun testCases1() = listOf(TestCase(Input("HOH", listOf("H" to "HO", "H" to "OH", "O" to "HH")), 4))
    override fun testCases2() = listOf(
        TestCase(Input("HOH", listOf("e" to "H", "e" to "O", "H" to "HO", "H" to "OH", "O" to "HH")), 3),
        TestCase(Input("HOHOHO", listOf("e" to "H", "e" to "O", "H" to "HO", "H" to "OH", "O" to "HH")), 6)
    )
}

fun main() {
    Day19().main()
}
