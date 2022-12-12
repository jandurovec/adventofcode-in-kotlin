package aoc2022

import readInput

fun main() {
    class AreaMap(val heightMap: Array<IntArray>, val start: Pair<Int, Int>, val end: Pair<Int, Int>)

    fun parseInput(input: List<String>): AreaMap {
        var start = -1 to -1
        var end = -1 to -1
        val heightMap = input.mapIndexed { row, line ->
            line.mapIndexed { col, c ->
                when (c) {
                    'S' -> { start = row to col; 'a' }
                    'E' -> { end = row to col; 'z' }
                    else -> c
                } - 'a'
            }.toIntArray()
        }.toTypedArray()
        return AreaMap(heightMap, start, end)
    }

    fun bfs(
        heightMap: Array<IntArray>,
        start: Pair<Int, Int>,
        end: Set<Pair<Int, Int>>,
        validStep: (Int, Int) -> Boolean
    ): Int {
        val steps = listOf(0 to -1, 0 to 1, 1 to 0, -1 to 0)
        val toExplore = ArrayDeque(listOf(start to 0))
        val visited = mutableSetOf(start)
        while (toExplore.isNotEmpty()) {
            val (cur, curSteps) = toExplore.removeFirst()
            steps.forEach { step ->
                val next = cur.first + step.first to cur.second + step.second
                if (!visited.contains(next)
                    && next.first in heightMap.indices
                    && next.second in heightMap[next.first].indices
                    && validStep(heightMap[cur.first][cur.second], heightMap[next.first][next.second])
                ) {
                    if (end.contains(next)) {
                        return curSteps + 1
                    } else {
                        toExplore.add(next to curSteps + 1)
                        visited.add(next)
                    }
                }
            }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        val areaMap = parseInput(input)
        return bfs(areaMap.heightMap, areaMap.start, setOf(areaMap.end)) { cur, next ->
            cur >= next - 1
        }
    }

    fun part2(input: List<String>): Int {
        val areaMap = parseInput(input)
        val end = areaMap.heightMap.flatMapIndexed { row, line ->
            line.mapIndexed { col, h -> (row to col) to h }.filter { it.second == 0 }.map { it.first }
        }.toSet()
        return bfs(areaMap.heightMap, areaMap.end, end) { cur, next ->
            next >= cur - 1
        }
    }

    val testInput = readInput(2022, 12, "test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput(2022, 12)
    println(part1(input))
    println(part2(input))
}
