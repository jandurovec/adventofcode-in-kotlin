package aoc2025

import AdventDay
import TestCase
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


class Day08 : AdventDay<List<Day08.Point3D>, Int, Int>(2025, 8) {
    data class Point3D(val x: Int, val y: Int, val z: Int)

    override fun parseInput(stringInput: List<String>) =
        stringInput.map { s -> s.split(",").map { it.toInt() }.let { (x, y, z) -> Point3D(x, y, z) } }

    private fun calculateDistances(input: List<Point3D>): PriorityQueue<Pair<Pair<Point3D, Point3D>, Double>> {
        val distances =
            PriorityQueue<Pair<Pair<Point3D, Point3D>, Double>> { a, b -> compareValuesBy(a, b) { it.second } }
        for (i in input.indices) {
            for (j in i + 1..input.lastIndex) {
                val a = input[i]
                val b = input[j]
                val dist =
                    sqrt((a.x.toDouble() - b.x).pow(2) + (a.y.toDouble() - b.y).pow(2) + (a.z.toDouble() - b.z).pow(2))
                distances.add(a to b to dist)
            }
        }
        return distances
    }

    private fun MutableSet<MutableSet<Point3D>>.connect(a: Point3D, b: Point3D) {
        val circuitA = firstOrNull { it.contains(a) }
        val circuitB = firstOrNull { it.contains(b) }
        val merged = mutableSetOf<Point3D>()
        if (circuitA != null) {
            remove(circuitA)
            merged.addAll(circuitA)
        } else {
            merged.add(a)
        }
        if (circuitB != null) {
            remove(circuitB)
            merged.addAll(circuitB)
        } else {
            merged.add(b)
        }
        add(merged)
    }

    override fun part1(input: List<Point3D>, testArg: Any?): Int {
        val distances = calculateDistances(input)
        val circuits = mutableSetOf<MutableSet<Point3D>>()
        repeat(testArg as? Int ?: 1000) {
            val (points, _) = distances.poll()
            circuits.connect(points.first, points.second)
        }
        return circuits.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }

    override fun part2(input: List<Point3D>, testArg: Any?): Int {
        val distances = calculateDistances(input)
        val circuits = mutableSetOf<MutableSet<Point3D>>()
        while (true) {
            val (points, _) = distances.poll()
            circuits.connect(points.first, points.second)
            if (circuits.size == 1 && circuits.first().size == input.size) {
                return points.first.x * points.second.x
            }
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 40, 10))
    override fun testCases2() = listOf(TestCase(getTestInput(), 25272))
}

fun main() {
    Day08().main()
}
