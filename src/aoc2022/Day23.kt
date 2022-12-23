package aoc2022

import readInput
import size

fun main() {
    val around = listOf(
        -1 to -1, 0 to -1, 1 to -1,
        -1 to 0, 1 to 0,
        -1 to 1, 0 to 1, 1 to 1
    )

    class Elves(val positions: MutableSet<Pair<Int, Int>>) {
        val movements = ArrayDeque(
            listOf(
                listOf(0 to -1, 1 to -1, -1 to -1) to (0 to -1),
                listOf(0 to 1, 1 to 1, -1 to 1) to (0 to 1),
                listOf(-1 to 0, -1 to -1, -1 to 1) to (-1 to 0),
                listOf(1 to 0, 1 to -1, 1 to 1) to (1 to 0)
            )
        )

        fun move(): Boolean {
            val moves = positions.asSequence().mapNotNull { elf ->
                if (around.any { positions.contains(elf.first + it.first to elf.second + it.second) }) {
                    val step = movements.firstOrNull { (directions, _) ->
                        directions.none { positions.contains(elf.first + it.first to elf.second + it.second) }
                    }?.second
                    if (step != null) elf to (elf.first + step.first to elf.second + step.second) else null
                } else {
                    null
                }
            }.groupBy { it.second }.filter { it.value.size == 1 }.map { it.value.first() }

            moves.forEach { (oldPos, newPos) ->
                positions.remove(oldPos)
                positions.add(newPos)
            }

            movements.addLast(movements.removeFirst())
            return moves.isNotEmpty()
        }
    }

    fun part1(input: List<String>): Int {
        val elves = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c -> if (c == '#') x to y else null }
        }.toMutableSet().let { Elves(it) }
        repeat(10) {
            elves.move()
        }
        var minX = Int.MAX_VALUE
        var minY = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var maxY = Int.MIN_VALUE
        elves.positions.forEach { (x, y) ->
            minX = minOf(minX, x)
            minY = minOf(minY, y)
            maxX = maxOf(maxX, x)
            maxY = maxOf(maxY, y)
        }
        return (minX..maxX).size() * (minY..maxY).size() - elves.positions.size
    }

    fun part2(input: List<String>): Int {
        val elves = input.flatMapIndexed { y, line ->
            line.mapIndexedNotNull { x, c -> if (c == '#') x to y else null }
        }.toMutableSet().let { Elves(it) }
        return generateSequence { elves.move() }.takeWhile { it }.count() + 1
    }


    val testInput = readInput(2022, 23, "test")
    check(part1((testInput)) == 110)
    check(part2((testInput)) == 20)

    val input = readInput(2022, 23)
    println(part1(input))
    println(part2(input))
}
