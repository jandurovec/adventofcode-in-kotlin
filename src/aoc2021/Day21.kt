package aoc2021

import readInput

fun main() {
    fun parseInput(input: List<String>) =
        input.map { it.substringAfterLast(": ").toInt() }

    fun part1(input: List<Int>): Int {
        val playerPos = input.toIntArray()
        val rollSeq = generateSequence(1) { 1 + it % 100 }.windowed(3, 3).map { it.sum() }.iterator()
        val score = IntArray(2)
        var rolls = 0
        var currentPlayer = 1
        do {
            currentPlayer = (currentPlayer + 1) % 2
            playerPos[currentPlayer] = 1 + (playerPos[currentPlayer] + rollSeq.next() - 1) % 10
            rolls += 3
            score[currentPlayer] += playerPos[currentPlayer]
        } while (score[currentPlayer] < 1000)
        val losingPlayer = (currentPlayer + 1) % 2
        return score[losingPlayer] * rolls
    }

    fun part2(input: List<Int>): Long {
        data class GameState(val pos: Pair<Int, Int>, val score: Pair<Int, Int>)
        // map<GameState, universes>
        var data = mapOf(GameState(input[0] to input[1], 0 to 0) to 1L)
        var playerOneTurn = false
        do {
            playerOneTurn = !playerOneTurn
            data = data.flatMap { entry ->
                if (entry.key.score.first >= 21 || entry.key.score.second >= 21)
                    listOf(entry.key to entry.value)
                else
                    (1..3).flatMap { d1 ->
                        (1..3).flatMap { d2 ->
                            (1..3).map { d3 ->
                                val roll = d1 + d2 + d3
                                if (playerOneTurn) {
                                    val newPos = 1 + (entry.key.pos.first + roll - 1) % 10
                                    GameState(
                                        newPos to entry.key.pos.second,
                                        entry.key.score.first + newPos to entry.key.score.second
                                    ) to entry.value
                                } else {
                                    val newPos = 1 + (entry.key.pos.second + roll - 1) % 10
                                    GameState(
                                        entry.key.pos.first to newPos,
                                        entry.key.score.first to entry.key.score.second + newPos
                                    ) to entry.value
                                }
                            }
                        }
                    }
            }.groupingBy { it.first }
                .aggregate { _, acc, e, first -> if (first) e.second else acc!! + e.second }
        } while (data.keys.any { it.score.first < 21 && it.score.second < 21 })
        return data.map { if (it.key.score.first >= 21) it.value to 0L else 0L to it.value }
            .reduce { p, q -> (p.first + q.first) to (p.second + q.second) }.toList().maxOf { it }
    }

    val testInput = parseInput(readInput(2021, 21, "test"))
    check(part1(testInput) == 739_785)
    check(part2(testInput) == 444_356_092_776_315)

    val input = parseInput(readInput(2021, 21))
    println(part1(input))
    println(part2(input))
}
