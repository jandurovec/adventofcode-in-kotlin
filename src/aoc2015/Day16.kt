package aoc2015

import AdventDay

class Day16 : AdventDay<List<Day16.AuntSue>, Int, Int>(2015, 16) {
    companion object {
        val MFCSAM = mapOf(
            "children" to 3,
            "cats" to 7,
            "samoyeds" to 2,
            "pomeranians" to 3,
            "akitas" to 0,
            "vizslas" to 0,
            "goldfish" to 5,
            "trees" to 3,
            "cars" to 2,
            "perfumes" to 1
        )
    }

    class AuntSue(val number: Int, val owns: Map<String, Int>)

    override fun parseInput(stringInput: List<String>) = stringInput.map { def ->
        val x = def.indexOf(':')
        AuntSue(def.substring(4, x).toInt(),
            def.substring(x + 2).split(Regex(", ")).map { it.split(Regex(": ")) }
                .associate { (what, howMuch) -> what to howMuch.toInt() })
    }

    private fun List<AuntSue>.find(predicate: (AuntSue) -> Boolean) = first(predicate).number

    override fun part1(input: List<AuntSue>, testArg: Any?) = input.find { aunt ->
        MFCSAM.all { (item, amount) -> aunt.owns[item].let { it == null || it == amount } }
    }

    override fun part2(input: List<AuntSue>, testArg: Any?) = input.find { aunt ->
        MFCSAM.all { (item, amount) ->
            when (item) {
                "cats", "trees" -> aunt.owns[item].let { it == null || it > amount }
                "pomeranians", "goldfish" -> aunt.owns[item].let { it == null || it < amount }
                else -> aunt.owns[item].let { it == null || it == amount }
            }
        }
    }
}

fun main() {
    Day16().main()
}
