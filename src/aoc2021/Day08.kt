package aoc2021

import readInput

fun main() {
    fun parseInput(name: String) =
        readInput(name).map { line -> line.split(" | ").map { it.split(' ') } }

    fun part1(input: List<List<List<String>>>) =
        input.sumOf { (_, values) -> values.count { it.length <= 4 || it.length == 7 } }

    fun part2(input: List<List<List<String>>>) = input.sumOf { (patterns, values) ->
        val byLength = patterns.map { it.toSet() }.groupBy { it.size }
        val one = byLength[2]!!.first()
        val three = byLength[5]!!.first { it.containsAll(one) }
        val four = byLength[4]!!.first()
        val seven = byLength[3]!!.first()
        val eight = byLength[7]!!.first()
        val e = eight.first { !three.contains(it) && !four.contains(it) }
        val two = byLength[5]!!.first { it.contains(e) }
        val five = byLength[5]!!.first { it != two && it != three }
        val six = byLength[6]!!.first { it.containsAll(five) && it.contains(e) }
        val nine = byLength[6]!!.first { it.containsAll(five) && !it.contains(e) }
        val zero = byLength[6]!!.first { it != six && it != nine }

        val mapping = mapOf(
            one to "1",
            two to "2",
            three to "3",
            four to "4",
            five to "5",
            six to "6",
            seven to "7",
            eight to "8",
            nine to "9",
            zero to "0"
        )

        values.joinToString("") { mapping[it.toSet()]!! }.toInt()
    }

    val testInput = parseInput("aoc2021/Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61_229)

    val input = parseInput("aoc2021/Day08")
    println(part1(input))
    println(part2(input))
}
