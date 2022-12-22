package aoc2022

import readInput

fun main() {
    val directions = listOf(1 to 0, 0 to 1, -1 to 0, 0 to -1)

    fun navigate(
        input: List<String>,
        step: (Pair<Int, Int>, Int, List<IntRange>, List<IntRange>) -> Pair<Pair<Int, Int>, Int>
    ): Int {
        val minX = mutableMapOf<Int, Int>()
        val maxX = mutableMapOf<Int, Int>()
        val minY = mutableMapOf<Int, Int>()
        val maxY = mutableMapOf<Int, Int>()
        input.take(input.size - 2).forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != ' ') {
                    if (!minX.containsKey(y)) {
                        minX[y] = x
                    }
                    maxX[y] = x
                    if (!minY.containsKey(x)) {
                        minY[x] = y
                    }
                    maxY[x] = y
                }
            }
        }
        val path = input.last().split("((?<=[LR])|(?=[LR]))".toRegex())
        val xRanges = (0..minX.maxOf { it.key }).map { minX[it]!!..maxX[it]!! }
        val yRanges = (0..minY.maxOf { it.key }).map { minY[it]!!..maxY[it]!! }

        var pos = xRanges[0].first to 0
        var facing = 0
        path.forEach { token ->
            val dist = token.toIntOrNull()
            if (dist != null) {
                for (i in 1..dist) {
                    val (newPos, newFacing) = step(pos, facing, xRanges, yRanges)
                    if (input[newPos.second][newPos.first] == '#') {
                        break
                    }
                    pos = newPos
                    facing = newFacing
                }
            } else {
                facing = if (token == "R") {
                    (facing + 1) % directions.size
                } else {
                    (facing - 1 + directions.size) % directions.size
                }
            }
        }
        return 1000 * (pos.second + 1) + 4 * (pos.first + 1) + facing
    }

    fun part1(input: List<String>) = navigate(input) { at, facing, xRanges, yRanges ->
        fun wrap(v: Int, rng: IntRange) = if (v < rng.first) rng.last else if (v > rng.last) rng.first else v
        val x = wrap(at.first + directions[facing].first, xRanges[at.second])
        val y = wrap(at.second + directions[facing].second, yRanges[at.first])
        x to y to facing
    }

    /**
     * Hardcoded for the shape of my actual input cube size 50 with the following layout
     * +---+
     * | AB|
     * | C |
     * |DE |
     * |F  |
     * +---+
     */
    fun part2(input: List<String>, cubeSize: Int = 50) = navigate(input) { at, facing, xRanges, yRanges ->
        val x = at.first + directions[facing].first
        val y = at.second + directions[facing].second

        if (y in xRanges.indices && x in yRanges.indices && x in xRanges[y] && y in yRanges[x]) {
            x to y to facing
        } else if (y == -1) {
            when (x) {
                in cubeSize until 2 * cubeSize -> 0 to x + 2 * cubeSize to 0 // A to F
                else -> x - 2 * cubeSize to 4 * cubeSize - 1 to 3 // B to F
            }
        } else if (y in 0 until cubeSize) {
            when (facing) {
                0 -> 2 * cubeSize - 1 to (3 * cubeSize - 1 - y) to 2 // B to E
                else -> 0 to (3 * cubeSize - 1 - y) to 0 // A to D
            }
        } else if (y in cubeSize until 2 * cubeSize) {
            when (facing) {
                0 -> y + cubeSize to cubeSize - 1 to 3 // C to B
                1 -> 2 * cubeSize - 1 to x - cubeSize to 2 // B to C
                2 -> y - cubeSize to 2 * cubeSize to 1 // C to D
                else -> cubeSize to x + cubeSize to 0// D to C
            }
        } else if (y in 2 * cubeSize until 3 * cubeSize) {
            when (facing) {
                0 -> 3 * cubeSize - 1 to 3 * cubeSize - 1 - y to 2 // E to B
                else -> cubeSize to 3 * cubeSize - 1 - y to 0 // D to A
            }
        } else if (y in 3 * cubeSize until 4 * cubeSize) {
            when (facing) {
                0 -> y - 2 * cubeSize to 3 * cubeSize - 1 to 3 // F to E
                1 -> cubeSize - 1 to x + 2 * cubeSize to 2 // E to F
                else -> y - 2 * cubeSize to 0 to 1 // F to A
            }
        } else {
            x + 2 * cubeSize to 0 to 1 // F to B
        }
    }

    val testInput = readInput(2022, 22, "test")
    check(part1(testInput) == 6032)

    val input = readInput(2022, 22)
    println(part1(input))
    println(part2(input))
}
