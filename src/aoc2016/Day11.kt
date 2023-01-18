package aoc2016

import AdventDay
import TestCase

class Day11 : AdventDay<Day11.Facility, Int, Int>(2016, 11) {
    private fun Set<String>.isSafe(): Boolean {
        val chips = filter { it.contains("microchip") }.map { it.substring(0, it.indexOf('-')) }.toSet()
        val generators = filter { it.contains("generator") }.map { it.substring(0, it.indexOf(' ')) }.toSet()
        val unshieldedChips = chips - generators
        return generators.isEmpty() || unshieldedChips.isEmpty()
    }

    data class Facility(val elevator: Int, val floors: List<Set<String>>) {
        private fun normalize(): List<Pair<Int, Int>> = floors.flatMap { f ->
            f.map { it.substring(0, if (it.contains("microchip")) it.indexOf('-') else it.indexOf(' ')) }
        }.toSet().map { type ->
            floors.indexOfFirst { it.contains("$type generator") } to floors.indexOfFirst { it.contains("$type-compatible microchip") }
        }.sortedWith { a, b -> (a.first - b.first).let { if (it == 0) a.second - b.second else it } }

        override fun hashCode() = normalize().hashCode() + elevator.hashCode()
        override fun equals(other: Any?) =
            other is Facility && elevator == other.elevator && normalize() == other.normalize()
    }

    override fun parseInput(stringInput: List<String>) = Facility(0, stringInput.map { l ->
        "([a-z]*(?:-compatible microchip| generator))".toRegex().findAll(l).map { it.groupValues[1] }.toSet()
    })

    override fun part1(input: Facility, testArg: Any?): Int {
        val toExplore = ArrayDeque<Pair<Facility, Int>>()
        toExplore.addLast(input to 0)
        val explored = mutableSetOf(input)
        while (toExplore.isNotEmpty()) {
            val (curFacility, curSteps) = toExplore.removeFirst()
            curFacility.floors[curFacility.elevator].let { floor ->
                floor.forEach { item ->
                    val candidates =
                        floor.filter { it > item }.map { (setOf(item, it)) }.plusElement(setOf(item))
                    candidates.forEach { inTransit ->
                        val whatRemains = floor - inTransit
                        if (whatRemains.isSafe()) {
                            listOf(curFacility.elevator + 1, curFacility.elevator - 1)
                                .filter { it in 0..curFacility.floors.lastIndex }
                                .forEach { newFloorNumber ->
                                    val newFloor = curFacility.floors[newFloorNumber] + inTransit
                                    if (newFloor.isSafe()) {
                                        val newFloors = curFacility.floors.mapIndexed { i, f ->
                                            when (i) {
                                                curFacility.elevator -> whatRemains
                                                newFloorNumber -> newFloor
                                                else -> f
                                            }
                                        }
                                        val newFacility = Facility(newFloorNumber, newFloors)
                                        if (!explored.contains(newFacility)) {
                                            if (newFloors.take(newFloors.size - 1).all { it.isEmpty() }) {
                                                return curSteps + 1
                                            }
                                            explored.add(newFacility)
                                            toExplore.addLast(newFacility to curSteps + 1)
                                        }
                                    }
                                }
                        }
                    }
                }
            }
        }
        error("Can't bring all objects to floor #${input.floors.size}")
    }

    override fun part2(input: Facility, testArg: Any?): Int {
        val newFacility = Facility(input.elevator, input.floors.mapIndexed { i, f ->
            if (i == 0) f + setOf(
                "elerium generator",
                "elerium-compatible microchip",
                "dilithium generator",
                "dilithium-compatible microchip"
            ) else f
        })
        return part1(newFacility, testArg)
    }

    override fun testCases1() = listOf(
        TestCase(getTestInput(), 11)
    )
}

fun main() {
    Day11().main()
}
