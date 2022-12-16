package aoc2022

import readInput
import kotlin.system.measureTimeMillis

fun main() {
    class Volcano(input: List<String>) {
        inner class Valve(val name: String, val flow: Int, val next: Map<String, Int>)

        val lineRegex = "Valve (.*) has flow rate=(.*); tunnels? leads? to valves? (.*)".toRegex()

        val scan = input
            .map { lineRegex.find(it)!!.destructured }
            .map { (name, flow, next) -> Valve(name, flow.toInt(), next.split(", ").associateWith { 1 }) }
            .associateBy { it.name }.let { scan ->
                val valves = scan.values.filter { it.flow > 0 }.map { it.name }.toSet()
                valves.plus("AA").map {
                    Valve(it, scan[it]!!.flow, findPaths(scan, it, valves.minus(it)))
                }.associateBy { it.name }
            }

        private fun findPaths(scan: Map<String, Valve>, start: String, dest: Set<String>): Map<String, Int> {
            val visited = mutableSetOf(start)
            val toVisit = dest.minus(start).toMutableSet()

            val toExplore = ArrayDeque(listOf(start to 0))
            val result = mutableMapOf<String, Int>()
            if (dest.contains(start)) {
                result[start] = 0
            }
            while (toExplore.isNotEmpty() && toVisit.isNotEmpty()) {
                val (at, len) = toExplore.removeFirst()
                scan[at]!!.next.forEach { (name, dist) ->
                    val candidate = name to len + dist
                    if (!visited.contains(name)) {
                        toExplore += candidate
                        visited += name
                        if (toVisit.contains(name)) {
                            result[name] = candidate.second
                            toVisit.remove(name)
                        }
                    }
                }
            }
            return result
        }

        fun findMax(current: String, toOpen: Set<String>, timeLeft: Int, released: Int = 0): Int {
            return if (timeLeft <= 1) {
                released // no time to move or release has no effect
            } else if (toOpen.isEmpty()) {
                released // all done
            } else { // go somewhere and open it if there's time
                val cur = scan[current]!!
                toOpen.maxOfOrNull {
                    val dist = cur.next[it]!!
                    val newTimeLeft = timeLeft - dist - 1
                    if (newTimeLeft > 0) {
                        findMax(it, toOpen.minus(it), newTimeLeft, released + newTimeLeft * scan[it]!!.flow)
                    } else {
                        released
                    }
                } ?: released
            }
        }
    }

    fun part1(input: List<String>) = Volcano(input).let { v ->
        v.findMax("AA", v.scan.values.filter { it.flow > 0 }.map { it.name }.toSet(), 30)
    }

    fun part2(input: List<String>) = Volcano(input).let { v ->
        val toVisitAll = v.scan.values.filter { it.flow > 0 }.map { it.name }.toTypedArray()
        (1 until (1 shl (toVisitAll.size - 1))).maxOf {
            val mine = mutableSetOf<String>()
            val elephants = mutableSetOf<String>()
            toVisitAll.forEachIndexed { i, s ->
                if (it and (1 shl i) > 0) {
                    elephants += s
                } else {
                    mine += s
                }
            }
            v.findMax("AA", mine, 26) + v.findMax("AA", elephants, 26)
        }
    }

    val testInput = readInput(2022, 16, "test")
    check(part1(testInput) == 1651)
    check(part2(testInput) == 1707)

    val input = readInput(2022, 16)
    println(part1(input))
    println(part2(input))
}
