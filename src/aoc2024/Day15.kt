package aoc2024

import AdventDay
import Grid
import Point
import TestCase
import split


class Day15 : AdventDay<Pair<Day15.Warehouse, List<Char>>, Int, Int>(2024, 15) {

    companion object {
        const val BOX = 'O'
        const val BOX_L = '['
        const val BOX_R = ']'
        const val WALL = '#'
        const val ROBOT = '@'
    }

    data class Warehouse(val data: Grid<Char>, var robot: Point) {
        fun simulate(moves: List<Char>) {
            moves.forEach { move ->
                val direction = when (move) {
                    '^' -> Point.UP
                    'v' -> Point.DOWN
                    '<' -> Point.LEFT
                    '>' -> Point.RIGHT
                    else -> error("Invalid move: $move")
                }
                var checkArea = listOf(robot + direction)
                val boxesToMove = mutableListOf<Pair<Point, Char>>()
                while (checkArea.any { data[it] != null } && checkArea.none { data[it] == WALL }) {
                    val boxes =
                        checkArea.filter { pos -> data[pos].let { it == BOX || it == BOX_L || it == BOX_R } }
                            .flatMap {
                                if (direction == Point.UP || direction == Point.DOWN) {
                                    when (data[it]) {
                                        BOX -> listOf(it)
                                        BOX_L -> listOf(it, it.right())
                                        BOX_R -> listOf(it.left(), it)
                                        else -> emptyList()
                                    }
                                } else {
                                    listOf(it)
                                }
                            }.toSet()
                    boxesToMove.addAll(boxes.map { it to data[it]!! })
                    checkArea = boxes.map { it + direction }
                }
                if (checkArea.all { data[it] == null }) {
                    boxesToMove.forEach { data.remove(it.first) }
                    boxesToMove.map { it.first + direction to it.second }.forEach { data[it.first] = it.second }
                    robot += direction
                }
            }
        }
    }

    override fun parseInput(stringInput: List<String>): Pair<Warehouse, List<Char>> {
        val data = Grid<Char>()
        var robot: Point? = null
        val (map, moves) = stringInput.split()
        map.forEachIndexed { row, s ->
            s.forEachIndexed { col, c ->
                when (c) {
                    ROBOT -> robot = Point(row, col)
                    BOX, WALL -> data[Point(col, row)] = c
                }
            }
        }
        return Warehouse(data, robot!!) to moves.flatMap { l -> l.map { it } }
    }

    override fun part1(input: Pair<Warehouse, List<Char>>, testArg: Any?): Int {
        val (sokoban, moves) = input
        sokoban.simulate(moves)
        return sokoban.data.entries.filter { it.value == BOX }.sumOf { it.key.x + 100 * it.key.y }
    }

    override fun part2(input: Pair<Warehouse, List<Char>>, testArg: Any?): Int {
        val (origWarehouse, moves) = input
        val warehouse2 = Warehouse(origWarehouse.data.flatMap { (point, obj) ->
            when (obj) {
                BOX -> listOf(Point(2 * point.x, point.y) to BOX_L, Point(2 * point.x + 1, point.y) to BOX_R)
                WALL -> listOf(Point(2 * point.x, point.y) to WALL, Point(2 * point.x + 1, point.y) to WALL)
                else -> emptyList()
            }
        }.toMap(mutableMapOf()), Point(2 * origWarehouse.robot.x, origWarehouse.robot.y))
        warehouse2.simulate(moves)
        return warehouse2.data.entries.filter { it.value == BOX_L }.sumOf { it.key.x + 100 * it.key.y }
    }

    override fun testCases1() = listOf(
        TestCase(getInput("test1"), 2028),
        TestCase(getInput("test2"), 10092)
    )

    override fun testCases2() = listOf(TestCase(getInput("test2"), 9021))

}

fun main() {
    Day15().main()
}
