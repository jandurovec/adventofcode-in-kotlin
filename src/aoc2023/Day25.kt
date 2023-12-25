package aoc2023

import AdventDay
import TestCase

class Day25 : AdventDay<Map<String, Set<String>>, Int, Unit>(2023, 25) {
    override fun parseInput(stringInput: List<String>) = buildMap<String, Set<String>> {
        stringInput.forEach { s ->
            val (from, list) = s.split(": ")
            list.split(" ").forEach { to ->
                sequenceOf(from to to, to to from).forEach { (f, t) -> this.merge(f, setOf(t)) { a, b -> a + b } }
            }
        }
    }

    override fun part1(input: Map<String, Set<String>>, testArg: Any?): Int {
        val edgeCount = mutableMapOf<Set<String>, Int>()
        input.keys.forEach { start ->
            val visited = mutableSetOf(start)
            val toExplore = ArrayDeque<String>().also { it.add(start) }
            while (toExplore.isNotEmpty()) {
                val from = toExplore.removeFirst()
                input[from]!!.filter { !visited.contains(it) }.forEach { to ->
                    visited.add(to)
                    toExplore.addLast(to)
                    edgeCount.merge(setOf(from, to), 1) { a, b -> a + b }
                }
            }
        }
        fun components(graph: Map<String, Set<String>>) = buildList<Set<String>> {
            val remaining = graph.entries.flatMap { it.value + it.key }.toMutableSet()
            while (remaining.isNotEmpty()) {
                val start = remaining.first()
                val visited = mutableSetOf(start)
                val toExplore = ArrayDeque<String>().also { it.add(start) }
                while (toExplore.isNotEmpty()) {
                    val from = toExplore.removeFirst()
                    graph[from]!!.filter { !visited.contains(it) }.forEach { to ->
                        visited.add(to)
                        toExplore.addLast(to)
                    }
                }
                add(visited)
                remaining.removeAll(visited)
            }
        }

        val sortedEdges = edgeCount.entries.sortedByDescending { it.value }.map { it.key }
        (0..<sortedEdges.lastIndex - 1).forEach { i1 ->
            (i1 + 1..<sortedEdges.lastIndex).forEach { i2 ->
                (i2 + 1..sortedEdges.lastIndex).forEach { i3 ->
                    val toRemove = setOf(sortedEdges[i1], sortedEdges[i2], sortedEdges[i3])
                    val filtered = input.map { (from, to) ->
                        from to to.filter { !toRemove.contains(setOf(from, it)) }.toSet()
                    }.associate { it }
                    val components = components(filtered)
                    if (components.size == 2) {
                        return components[0].size * components[1].size
                    }
                }
            }
        }
        error("Nothing found")
    }

    override fun part2(input: Map<String, Set<String>>, testArg: Any?) {}

    override fun testCases1() = listOf(TestCase(getTestInput(), 54))

    override fun testCases2() = emptyList<TestCase<Map<String, Set<String>>, Unit>>()
}

fun main() {
    Day25().main()
}
