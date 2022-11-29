package aoc2021

import manhattanDist
import readInput

fun main() {
    val costs = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
    val allowedStops = setOf(1, 2, 4, 6, 8, 10, 11)

    data class Move(val from: Pair<Int, Int>, val to: Pair<Int, Int>, val cost: Int)
    class Burrow(val plan: Array<CharArray>, val amphipods: List<Pair<Int, Int>>) {
        fun homeColumn(name: Char) = 3 + (name.code - 'A'.code) * 2
        fun homeOpen(name: Char): Boolean {
            val homeColumn = homeColumn(name)
            return plan.all { it[homeColumn] == name || !it[homeColumn].isUpperCase() }
        }

        fun homeSpot(col: Int) = plan.indexOfLast { it[col] == '.' }
        fun needsToMove(x: Int, y: Int): Boolean {
            val pod = plan[y][x]
            val homeColumn = homeColumn(pod)
            return x != homeColumn || plan.drop(y + 1).any { it[x] != pod && it[x] != '#' }
        }

        fun toMove(): List<Pair<Int, Int>> = amphipods.filter { needsToMove(it.first, it.second) }

        fun destinations(x: Int, y: Int): List<Move> {
            val pod = plan[y][x]
            val homeColumn = homeColumn(pod)
            val homePos = homeSpot(homeColumn)
            if (homeOpen(pod)
                // path to hallhway free
                && (1 until y).all { plan[it][x] == '.' }
                // hallway free
                && (minOf(x, homeColumn)..maxOf(x, homeColumn)).all { plan[1][it] == '.' || (y == 1 && it == x) }
            ) {
                // go straight home
                val pathLength = manhattanDist(x, y, x, 1) +
                        manhattanDist(x, 1, homeColumn, 1) +
                        manhattanDist(homeColumn, 1, homeColumn, homePos)
                return listOf(Move(x to y, homeColumn to homePos, costs[pod]!! * pathLength))
            } else if (y == 1) {
                return emptyList()
            } else {
                return buildList {
                    var curY = y
                    // go to hallway first
                    while (curY > 1) {
                        if (plan[--curY][x] != '.') {
                            return@buildList
                        }
                    }
                    // go to sides
                    sequenceOf(-1, 1).forEach { dx ->
                        var steps = y - 1 // init with steps to hallway
                        var curX = x + dx
                        while (plan[1][curX] == '.') {
                            steps++
                            if (allowedStops.contains(curX)) {
                                add(Move(x to y, curX to 1, costs[pod]!! * steps))
                            }
                            curX += dx
                        }
                    }
                }
            }
        }
    }

    fun parseInput(input: List<String>) = Burrow(input.map { it.toCharArray() }.toTypedArray(), buildList {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c.isUpperCase()) {
                    this.add(x to y)
                }
            }
        }
    })

    fun solve(burrow: Burrow, energy: Int = 0, best: Int = Int.MAX_VALUE): Int {
        if (energy > best) {
            return best
        }
        val toMove = burrow.toMove()
        if (toMove.isEmpty()) {
            return energy
        }
        var newBest = best
        val moves = ArrayDeque<Move>()
        toMove.forEach {
            burrow.destinations(it.first, it.second).forEach { move ->
                if (move.to.second > 1) {
                    moves.addFirst(move) // put home moves first
                } else {
                    moves.add(move)
                }
            }
        }
        moves.forEach { m ->
            val amphipod = burrow.plan[m.from.second][m.from.first]
            val others = toMove.filter { it != m.from }
            burrow.plan[m.from.second][m.from.first] = '.'
            burrow.plan[m.to.second][m.to.first] = amphipod
            newBest = solve(Burrow(burrow.plan, others.plus(m.to)), energy + m.cost, newBest)
            burrow.plan[m.from.second][m.from.first] = amphipod
            burrow.plan[m.to.second][m.to.first] = '.'
        }
        return newBest
    }

    fun part1(input: List<String>) = solve(parseInput(input))
    fun part2(input: List<String>) =
        part1(input.toMutableList().also { it.addAll(3, listOf("  #D#C#B#A#", "  #D#B#A#C#")) })

    val testInput = readInput("aoc2021/Day23_test")
    check(part1(testInput) == 12_521)
    check(part2(testInput) == 44_169)

    val input = readInput("aoc2021/Day23")
    println(part1(input))
    println(part2(input))
}
