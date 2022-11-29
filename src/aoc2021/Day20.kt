package aoc2021

import readInput
import java.util.*

fun main() {
    class TrenchMap(input: List<String>) {
        val algo = BitSet(512)
        var mapData = mutableMapOf<Pair<Int, Int>, Boolean>()
        var minX = 0
        var maxX = 0
        var minY = 0
        var maxY = 0
        var default = false

        init {
            input.first().forEachIndexed { i, c ->
                if (c == '#') {
                    algo.set(i)
                }
            }
            input.drop(2).forEachIndexed { y, s ->
                s.forEachIndexed { x, c ->
                    mapData[x to y] = c == '#'
                }
                maxX = s.lastIndex
                maxY = y
            }
        }

        fun lightAt(x: Int, y: Int) = mapData.getOrDefault(x to y, default)
        fun asNumber(x: Int, y: Int) = listOf(
            -1 to -1,
            0 to -1,
            1 to -1,
            -1 to 0,
            0 to 0,
            1 to 0,
            -1 to 1,
            0 to 1,
            1 to 1
        ).map { if (lightAt(x + it.first, y + it.second)) 1 else 0 }.fold(0) { agg, next -> (agg shl 1) + next }

        fun enhance(steps: Int = 1): Int {
            repeat(steps) {
                val newData = mutableMapOf<Pair<Int, Int>, Boolean>()
                for (x in minX - 1..maxX + 1) {
                    for (y in minY - 1..maxY + 1) {
                        newData[x to y] = algo[asNumber(x, y)]
                    }
                }
                mapData = newData
                default = if (default) algo[511] else algo[0]
                minX--
                maxX++
                minY--
                maxY++
            }
            return mapData.count { it.value }
        }
    }

    fun part1(input: List<String>) = TrenchMap(input).enhance(2)

    fun part2(input: List<String>) = TrenchMap(input).enhance(50)

    val testInput = readInput("aoc2021/Day20_test")
    check(part1(testInput) == 35)
    check(part2(testInput) == 3_351)

    val input = readInput("aoc2021/Day20")
    println(part1(input))
    println(part2(input))
}
