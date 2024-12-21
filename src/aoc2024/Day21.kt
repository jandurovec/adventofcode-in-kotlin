package aoc2024

import Point
import TestCase
import UnparsedDay


class Day21 : UnparsedDay<Long, Long>(2024, 21) {

    private fun String.numericPart() = takeWhile { it.isDigit() }.dropWhile { it == '0' }.toInt()

    data class Keypad(private val layout: List<String>) {
        val moves: Map<Pair<Char, Char>, List<String>>

        init {
            moves = mutableMapOf()
            val keys = layout.flatMapIndexed { y, s ->
                s.mapIndexed { x, c ->
                    Point(x, y) to c
                }
            }.filter { it.second != ' ' }.associate { it }
            keys.forEach { from ->
                keys.forEach { to ->
                    moves[from.value to to.value] = if (from == to) {
                        listOf("A")
                    } else {
                        val paths = mutableListOf<List<Point>>()
                        val toExplore = ArrayDeque<List<Point>>()
                        toExplore.add(listOf(from.key))
                        var minPathLen = Int.MAX_VALUE
                        while (toExplore.isNotEmpty()) {
                            val cur = toExplore.removeFirst()
                            if (cur.size < minPathLen) {
                                cur.last().neighbors(false).filter { keys.containsKey(it) && it !in cur }.forEach {
                                    if (it == to.key) {
                                        minPathLen = cur.size + 1
                                        paths.add(cur + it)
                                    } else {
                                        toExplore.add(cur + it)
                                    }
                                }
                            }
                        }
                        paths.map { path ->
                            path.windowed(2).joinToString("") { (src, dst) ->
                                when (val diff = dst - src) {
                                    Point.UP -> "^"
                                    Point.DOWN -> "v"
                                    Point.LEFT -> "<"
                                    Point.RIGHT -> ">"
                                    else -> error("Invalid move: $diff")
                                }
                            } + "A"
                        }
                    }
                }
            }
        }
    }

    private fun String.segments() = "A$this".windowed(2).map { it[0] to it[1] }
    private val cache = mutableMapOf<Pair<List<Keypad>, String>, Long>()
    private fun List<Keypad>.shortestSequence(code: String): Long = cache.getOrPut(this to code) {
        if (size == 1) {
            code.segments().sumOf { (from, to) -> first().moves[from to to]!!.minOf { it.length.toLong() } }
        } else {
            code.segments().sumOf { (from, to) ->
                first().moves[from to to]!!.minOf { move -> drop(1).shortestSequence(move) }
            }
        }
    }

    private fun solve(codes: List<String>, robots: Int): Long {
        val keyPadChain = mutableListOf(Keypad(listOf("789", "456", "123", " 0A")))
        repeat(robots) {
            keyPadChain.add(Keypad(listOf(" ^A", "<v>")))
        }
        return codes.sumOf { code ->
            keyPadChain.shortestSequence(code) * code.numericPart()
        }
    }

    override fun part1(input: List<String>, testArg: Any?) = solve(input, 2)

    override fun part2(input: List<String>, testArg: Any?) = solve(input, 25)

    override fun testCases1() = listOf(TestCase(getTestInput(), 126384L))

}

fun main() {
    Day21().main()
}
