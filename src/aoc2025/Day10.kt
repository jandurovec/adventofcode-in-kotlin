package aoc2025

import AdventDay
import TestCase
import java.util.*


class Day10 : AdventDay<List<Day10.Machine>, Int, Int>(2025, 10) {
    data class Machine(val lights: Set<Int>, val buttons: List<Set<Int>>, val joltage: List<Int>)

    override fun parseInput(stringInput: List<String>) =
        stringInput.map { s ->
            s.split(" ").let { chunks ->
                val ls = chunks.first()
                val ms = chunks.last()
                val buttonList = chunks.drop(1).dropLast(1)
                Machine(
                    (0..ls.length - 3).filter { ls[it + 1] == '#' }.toSet(),
                    buttonList.map { buttons ->
                        buttons.substring(1, buttons.length - 1).split(",").map { it.toInt() }.toSet()
                    },
                    ms.substring(1, ms.length - 1).split(",").map { it.toInt() }
                )
            }
        }

    override fun part1(input: List<Machine>, testArg: Any?) = input.sumOf { machine ->
        val presses = mutableMapOf<Set<Int>, Int>()
        presses[emptySet()] = 0
        val toExplore = ArrayDeque<Pair<Set<Int>, Int>>().also { it.add(emptySet<Int>() to 0) }
        while (toExplore.isNotEmpty() && !presses.contains(machine.lights)) {
            val (curLights, curCount) = toExplore.poll()
            machine.buttons.forEach { b ->
                val newLights = curLights.toMutableSet()
                b.forEach {
                    if (!newLights.add(it)) {
                        newLights.remove(it)
                    }
                }
                if (!presses.contains(newLights)) {
                    presses[newLights] = curCount + 1
                    toExplore.add(newLights to curCount + 1)
                }
            }
        }
        presses[machine.lights]!!
    }


    override fun part2(input: List<Machine>, testArg: Any?) = input.sumOf { machine ->
        val matrix = machine.joltage.mapIndexed { i, v ->
            machine.buttons.map { if (it.contains(i)) 1 else 0 }.plus(v).toIntArray()
        }.toTypedArray()

        // naive Gaussian elimination
        fun Array<IntArray>.rowEchelonForm() {
            fun swapRows(i: Int, j: Int) {
                val row = this[i]
                this[i] = this[j]
                this[j] = row
            }
            if (isNotEmpty()) {
                (0..minOf(lastIndex, this[0].lastIndex)).forEach { i ->
                    if (this[i][i] == 0) {
                        val toSwap = (i + 1..lastIndex).firstOrNull { this[it][i] != 0 }
                        if (toSwap != null) {
                            swapRows(i, toSwap)
                        } // else all zeroes
                    }
                    if (this[i][i] != 0) {
                        (i + 1..lastIndex).forEach { j ->
                            val toEliminate = this[j][i]
                            if (toEliminate != 0) {
                                for (k in i..this[j].lastIndex) {
                                    this[j][k] = this[j][k] * this[i][i] - this[i][k] * toEliminate
                                }
                            }
                        }
                    }
                }
            }
        }

        val threshold = 300
        var best = threshold
        fun findBest(matrix: Array<IntArray>, pressesSoFar: Int = 0) {
            val (processedIndex, processedRow) = matrix.mapIndexed { idx, row -> idx to row }
                .minBy { it.second.dropLast(1).count { n -> n != 0 } }
            val result = processedRow.last()
            val buttons =
                processedRow.dropLast(1).mapIndexedNotNull { idx, n -> if (n == 0) null else idx to n }.toMap()
            if (buttons.isEmpty()) {
                if (result == 0) {
                    if (matrix.size == 1) {
                        best = pressesSoFar
                    } else {
                        findBest(matrix.filterIndexed { idx, _ -> idx != processedIndex }.toTypedArray(), pressesSoFar)
                    }
                } // else cannot satisfy
            } else if (buttons.size == 1) { // unique or no solution
                val (cur, n) = buttons.entries.first()
                if (result.mod(n) == 0) {
                    val addedPresses = result / n
                    val newPresses = pressesSoFar + addedPresses
                    if (addedPresses >= 0 && newPresses < best) {
                        if (matrix.size == 1) {
                            best = newPresses
                        } else {
                            val newMatrix = matrix
                                .mapIndexed { idx, row -> idx to row }
                                .filter { it.first != processedIndex }.map { (_, row) ->
                                    val newRow = row.copyOf()
                                    newRow[newRow.lastIndex] -= newRow[cur] * addedPresses
                                    newRow[cur] = 0
                                    newRow
                                }.toTypedArray()
                            findBest(newMatrix, newPresses)
                        }
                    }
                }
            } else {
                // guessing time
                val cur = buttons.keys.first()
                var guessedPresses = 0
                while (pressesSoFar + guessedPresses < best) {
                    val newMatrix = matrix.map {
                        val newRow = it.copyOf()
                        newRow[newRow.lastIndex] -= newRow[cur] * guessedPresses
                        newRow[cur] = 0
                        newRow
                    }.toTypedArray()
                    findBest(newMatrix, pressesSoFar + guessedPresses)
                    guessedPresses++
                }
            }
        }
        matrix.rowEchelonForm()
        findBest(matrix)
        if (best < threshold) best else error("Cannot find a solution under $threshold total presses. Increase the threshold.")
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), 7))
    override fun testCases2() = listOf(TestCase(getTestInput(), 33))
}

fun main() {
    Day10().main()
}
