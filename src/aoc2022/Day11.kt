package aoc2022

import readInput

fun main() {
    class Monkey(
        val items: MutableList<Long>,
        private val operation: (Long) -> Long,
        private val getNext: (Long) -> Int,
        private val worryDivisor: Int,
        var modBy: Long = Long.MAX_VALUE
    ) {
        fun inspectItems(): List<Pair<Long, Int>> {
            val result = items.map {
                val newItem = (operation(it) / worryDivisor) % modBy
                newItem to getNext(newItem)
            }
            items.clear()
            return result
        }
    }

    fun parseInput(input: List<String>, worryDivisor: Int): List<Monkey> {
        val result = ArrayList<Monkey>()
        var modulus = 1L
        input.chunked(7).forEach {
            val items = it[1].substringAfter("items: ").split(", ").map(String::toLong).toMutableList()
            val opArg = it[2].substringAfterLast(' ').toLongOrNull()
            val intOp = if (it[2].substringAfter("old ").startsWith('*'))
                { a: Long, b: Long -> a * b }
            else {
                { a: Long, b: Long -> a + b }
            }
            val op = if (opArg == null) {
                { a: Long -> intOp.invoke(a, a) }
            } else {
                { a: Long -> intOp.invoke(a, opArg) }
            }
            val testDivisor = it[3].substringAfter("divisible by ").toLong()
            val truePath = it[4].substringAfterLast(' ').toInt()
            val falsePath = it[5].substringAfterLast(' ').toInt()
            val getNext = { item: Long -> if (item % testDivisor == 0L) truePath else falsePath }
            result.add(Monkey(items, op, getNext, worryDivisor))
            // lcm not needed as divisors in input are distinct primes
            modulus *= testDivisor
        }
        result.forEach { it.modBy = modulus }
        return result
    }

    fun solve(input: List<String>, worryDivisor: Int, steps: Int): Long {
        val monkeys = parseInput(input, worryDivisor)
        val inspections = IntArray(monkeys.size)
        repeat(steps) {
            monkeys.forEachIndexed { i, m ->
                val thrown = m.inspectItems()
                inspections[i] += thrown.size
                thrown.forEach { t ->
                    monkeys[t.second].items += t.first
                }
            }
        }
        return inspections.sortedDescending().take(2).fold(1L) { a, b -> a * b }
    }

    fun part1(input: List<String>) = solve(input, 3, 20)
    fun part2(input: List<String>) = solve(input, 1, 10000)

    val testInput = readInput(2022, 11, "test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158)

    val input = readInput(2022, 11)
    println(part1(input))
    println(part2(input))
}
