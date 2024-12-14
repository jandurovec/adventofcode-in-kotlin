package aoc2023

import Point
import TestCase
import UnparsedDay


class Day18 : UnparsedDay<Int, Long>(2023, 18) {

    private fun digTrench(boundary: Set<Point>): Set<Point> {
        val trench = boundary.toMutableSet()
        val xRange = trench.minOf { it.x }..trench.maxOf { it.x }
        val yRange = trench.minOf { it.y }..trench.maxOf { it.y }
        val remaining = trench.flatMap { it.neighbors(false) }
            .filter { it.x in xRange && it.y in yRange && !trench.contains(it) }.toMutableSet()
        areaExploration@ while (remaining.isNotEmpty()) {
            val start = remaining.first().also { remaining.remove(it) }
            val area = mutableSetOf(start)
            val toExplore = ArrayDeque<Point>()
            toExplore.add(start)
            while (toExplore.isNotEmpty()) {
                val cur = toExplore.removeFirst()
                for (n in cur.neighbors(false).filter { !area.contains(it) && !trench.contains(it) }) {
                    if (n.x in xRange && n.y in yRange) {
                        toExplore.addLast(n)
                        area.add(n)
                        remaining.remove(n)
                    } else {
                        continue@areaExploration
                    }
                }
            }
            trench.addAll(area)
        }
        return trench
    }

    override fun part1(input: List<String>, testArg: Any?): Int {
        var cur = Point(0, 0)
        val boundary = mutableSetOf(cur)
        input.map { it.split(" ") }.forEach { (direction, distance) ->
            repeat(distance.toInt()) {
                cur = when (direction) {
                    "R" -> cur.right()
                    "L" -> cur.left()
                    "U" -> cur.up()
                    else -> cur.down()
                }
                boundary.add(cur)
            }
        }
        return digTrench(boundary).size
    }


    override fun part2(input: List<String>, testArg: Any?): Long {
        data class Segment(val from: Point, val to: Point, val direction: Char)

        val segments = buildList {
            var cur = Point(0, 0)
            input.map { it.split(" ")[2].trim('(', '#', ')') }.forEach { s ->
                val direction = s[5]
                val distance = s.substring(0, 5).toInt(16)
                val segment = when (direction) {
                    '0' -> Segment(cur, Point(cur.x + distance, cur.y), 'R')
                    '1' -> Segment(cur, Point(cur.x, cur.y + distance), 'D')
                    '2' -> Segment(cur, Point(cur.x - distance, cur.y), 'L')
                    else -> Segment(cur, Point(cur.x, cur.y - distance), 'U')
                }
                add(segment)
                cur = segment.to
            }
        }

        val xPoints = sortedSetOf<Int>()
        val yPoints = sortedSetOf<Int>()
        segments.flatMap { listOf(it.from, it.to) }.toSet().forEach {
            xPoints.add(it.x)
            yPoints.add(it.y)
        }
        val (xSizes, ySizes) = listOf(xPoints, yPoints).map { points ->
            points.windowed(2).flatMapIndexed { i, values ->
                val (from, to) = values
                listOf(2 * i to 1, 2 * i + 1 to (to - from - 1), 2 * (i + 1) to 1)
            }.map { (k, v) -> k to v.toLong() }.associate { it }
        }

        var cur = Point(2 * xPoints.indexOf(0), 2 * yPoints.indexOf(0))
        val mappedBoundary = mutableSetOf(cur)
        segments.forEach { (from, to, direction) ->
            when (direction) {
                'U' -> repeat((yPoints.count((to.y..from.y)::contains) - 1) * 2) {
                    cur = cur.up()
                    mappedBoundary.add(cur)
                }

                'D' -> repeat((yPoints.count((from.y..to.y)::contains) - 1) * 2) {
                    cur = cur.down()
                    mappedBoundary.add(cur)
                }

                'R' -> repeat((xPoints.count((from.x..to.x)::contains) - 1) * 2) {
                    cur = cur.right()
                    mappedBoundary.add(cur)
                }

                else -> repeat((xPoints.count((to.x..from.x)::contains) - 1) * 2) {
                    cur = cur.left()
                    mappedBoundary.add(cur)
                }
            }
        }

        return digTrench(mappedBoundary).sumOf { xSizes[it.x]!! * ySizes[it.y]!! }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 62))

    override fun testCases2() = listOf(TestCase(getTestInput(), 952408144115))
}

fun main() {
    Day18().main()
}
