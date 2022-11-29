package aoc2021

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var seafloor = input.map { it.toCharArray() }.toTypedArray()
        val width = seafloor[0].size
        val height = seafloor.size
        var moved = true
        var steps = 0
        while (moved) {
            moved = false
            val newSeafloor = Array(height) { CharArray(width) { '.' } }
            seafloor.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    val nextX = (x + 1) % width
                    when (c) {
                        '>' -> {
                            if (seafloor[y][nextX] == '.') {
                                newSeafloor[y][nextX] = c
                                moved = true
                            } else {
                                newSeafloor[y][x] = c
                            }
                        }
                        'v' -> {
                            val nextY = (y + 1) % height
                            val prevX = (x + width - 1) % width
                            if (seafloor[nextY][x] == '.' && seafloor[nextY][prevX] != '>' || (seafloor[nextY][x] == '>' && seafloor[nextY][nextX] == '.')) {
                                newSeafloor[nextY][x] = c
                                moved = true
                            } else {
                                newSeafloor[y][x] = c
                            }
                        }
                    }
                }
            }
            seafloor = newSeafloor
            steps++
        }
        return steps
    }

    val testInput = readInput("aoc2021/Day25_test")
    check(part1(testInput) == 58)

    val input = readInput("aoc2021/Day25")
    println(part1(input))
}
