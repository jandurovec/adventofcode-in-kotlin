package aoc2021

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val len = input[0].length
        val gamma = input.fold(List(len) { 0 }) { acc, cur ->
            acc.zip(cur.toList().map { if (it == '1') 1 else -1 }, Int::plus)
        }.map { if (it > 0) '1' else '0' }.joinToString("").toInt(2)
        val epsilon = List(len) { 1 }.joinToString("").toInt(2).xor(gamma)
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        fun eliminate(list: List<String>, cond1: (String, Int) -> Boolean, cond2: (String, Int) -> Boolean): Int {
            var i = 0
            var lst = list
            while (lst.size > 1) {
                val bitDiff = lst.sumOf { if (it[i] == '1') 1L else -1L }
                lst = lst.filter { if (bitDiff >= 0) cond1.invoke(it, i) else cond2.invoke(it, i) }
                i++
            }
            return lst[0].toInt(2)
        }

        val o2 = eliminate(input, { s, i -> s[i] == '1' }, { s, i -> s[i] == '0' })
        val co2 = eliminate(input, { s, i -> s[i] == '0' }, { s, i -> s[i] == '1' })

        return o2 * co2
    }

    val testInput = readInput("aoc2021/Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("aoc2021/Day03")
    println(part1(input))
    println(part2(input))
}
