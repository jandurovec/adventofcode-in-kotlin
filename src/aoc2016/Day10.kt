package aoc2016

import TestCase
import UnparsedDay

class Day10 : UnparsedDay<String, Int>(2016, 10) {
    class Factory(def: List<String>) {
        inner class BalanceBot(
            val id: String,
            private val lowType: String,
            private val lowName: String,
            private val highType: String,
            private val highName: String
        ) {
            val numbers = mutableListOf<Int>()
            fun offer(n: Int) {
                numbers += n
                if (numbers.size == 2) {
                    pass(lowType, lowName, numbers.min())
                    pass(highType, highName, numbers.max())
                }
            }
        }

        val bots = mutableMapOf<String, BalanceBot>()
        val outputs = mutableMapOf<String, Int>()

        private fun pass(type: String, name: String, value: Int) {
            if (type == "bot") {
                bots[name]!!.offer(value)
            } else {
                outputs[name] = value
            }
        }

        init {
            def.filter { it.startsWith("bot") }.forEach {
                val (name, lowType, lowName, highType, highName) = "bot (.*) gives low to (.*) (.*) and high to (.*) (.*)".toRegex()
                    .matchEntire(it)!!.destructured
                bots[name] = BalanceBot(name, lowType, lowName, highType, highName)
            }
            def.filter { it.startsWith("value") }.forEach {
                val (value, type, name) = "value (.*) goes to (.*) (.*)".toRegex().matchEntire(it)!!.destructured
                pass(type, name, value.toInt())
            }
        }
    }

    override fun part1(input: List<String>, testArg: Any?): String {
        val f = Factory(input)
        val needle = if (testArg is List<*>) testArg else listOf(61, 17)
        return f.bots.values.single { bot -> needle.all { bot.numbers.contains(it) } }.id
    }

    override fun part2(input: List<String>, testArg: Any?): Int {
        val f = Factory(input)
        return f.outputs.filter { setOf("0", "1", "2").contains(it.key) }.map { it.value }.reduce(Int::times)
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), "2", listOf(5, 2)))
}

fun main() {
    Day10().main()
}
