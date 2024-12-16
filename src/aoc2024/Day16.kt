package aoc2024

import AdventDay
import Grid
import Point
import Point.Companion.DOWN
import Point.Companion.LEFT
import Point.Companion.RIGHT
import Point.Companion.UP
import TestCase
import java.util.*

class Day16 : AdventDay<Day16.Input, Int, Int>(2024, 16) {

    data class Input(val maze: Grid<Boolean>, val start: Point, val end: Point)

    override fun parseInput(stringInput: List<String>): Input {
        val maze = Grid<Boolean>()
        var start: Point? = null
        var end: Point? = null
        stringInput.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val point = Point(x, y)
                when (c) {
                    '#' -> maze[point] = true
                    'S' -> start = point
                    'E' -> end = point
                }
            }
        }
        return Input(maze, start!!, end!!)
    }

    override fun part1(input: Input, testArg: Any?): Int {
        data class State(val pos: Point, val dir: Point, val score: Int)

        val toExplore = PriorityQueue<State>(Comparator.comparing { it.score })
        toExplore.add(State(input.start, RIGHT, 0))
        val visited = mutableSetOf(input.start to RIGHT)
        while (toExplore.isNotEmpty()) {
            val cur = toExplore.remove()
            if (cur.pos == input.end) {
                return cur.score
            }
            listOf(UP, DOWN, LEFT, RIGHT).filter { it != -cur.dir }.forEach { dir ->
                val newState = when (dir) {
                    cur.dir -> State(cur.pos + dir, dir, cur.score + 1)
                    else -> State(cur.pos, dir, cur.score + 1000)
                }
                if (!input.maze.containsKey(newState.pos) && visited.add(newState.pos to newState.dir)) {
                    toExplore.add(newState)
                }
            }
        }
        error("No path from ${input.start} to ${input.end}")
    }

    override fun part2(input: Input, testArg: Any?): Int {
        data class State(val pos: Point, val dir: Point, val score: Int, val path: Set<Point>)

        val toExplore = PriorityQueue<State>(Comparator.comparing { it.score })
        toExplore.add(State(input.start, RIGHT, 0, setOf(input.start)))
        val bestScoreToPoint = mutableMapOf(input.start to RIGHT to 0)
        val bestSeatsToPoint = mutableMapOf(input.start to RIGHT to setOf(input.start))
        var bestScoreToEnd = Int.MAX_VALUE
        while (toExplore.isNotEmpty()) {
            val cur = toExplore.remove()
            if (cur.score > bestScoreToEnd) {
                break
            }
            listOf(UP, DOWN, LEFT, RIGHT).filter { it != -cur.dir }.forEach { dir ->
                val newState = when (dir) {
                    cur.dir -> State(cur.pos + dir, dir, cur.score + 1, cur.path + (cur.pos + dir))
                    else -> State(cur.pos, dir, cur.score + 1000, cur.path)
                }
                if (!input.maze.containsKey(newState.pos)) {
                    val bestSoFar = bestScoreToPoint.getOrDefault(newState.pos to newState.dir, Int.MAX_VALUE)
                    if (newState.score < bestSoFar) {
                        bestScoreToPoint[newState.pos to newState.dir] = newState.score
                        bestSeatsToPoint[newState.pos to newState.dir] = bestSeatsToPoint[cur.pos to cur.dir]!! + newState.pos
                        if (newState.pos == input.end) {
                            bestScoreToEnd = newState.score
                        } else {
                            toExplore.add(newState)
                        }
                    } else if (newState.score == bestSoFar) {
                        bestSeatsToPoint[newState.pos to newState.dir] = bestSeatsToPoint[newState.pos to newState.dir]!! + bestSeatsToPoint[cur.pos to cur.dir]!!
                    }
                }
            }
        }
        return bestSeatsToPoint.keys.filter { it.first == input.end }.flatMap { bestSeatsToPoint[it]!! }.toSet().size
    }


    override fun testCases1() = listOf(
        TestCase(getInput("test1"), 7036),
        TestCase(getInput("test2"), 11048)
    )

    override fun testCases2() = listOf(
        TestCase(getInput("test1"), 45),
        TestCase(getInput("test2"), 64)
    )

}

fun main() {
    Day16().main()
}
