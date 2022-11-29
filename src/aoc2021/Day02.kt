package aoc2021

import readInput

fun main() {
    open class Sub1 {
        private val cmdRegex = """(forward|up|down) (\d+)""".toRegex()
        var depth = 0
        var horizontal = 0

        fun move(input: List<String>): Int {
            input.forEach {
                val (cmd: String, d: String) = cmdRegex.find(it)!!.destructured
                val dist = d.toInt()
                when (cmd) {
                    "forward" -> forward(dist)
                    "up" -> up(dist)
                    "down" -> down(dist)
                }
            }
            return horizontal * depth
        }

        open fun forward(d: Int) {
            horizontal += d
        }

        open fun up(d: Int) {
            depth -= d
        }

        open fun down(d: Int) {
            depth += d
        }
    }

    class Sub2 : Sub1() {
        var aim = 0

        override fun forward(d: Int) {
            horizontal += d
            depth += aim * d
        }

        override fun up(d: Int) {
            aim -= d
        }

        override fun down(d: Int) {
            aim += d
        }
    }

    fun part1(input: List<String>) = Sub1().move(input)

    fun part2(input: List<String>) = Sub2().move(input)

    val testInput = readInput("aoc2021/Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("aoc2021/Day02")
    println(part1(input))
    println(part2(input))
}
