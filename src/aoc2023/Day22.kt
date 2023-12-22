package aoc2023

import AdventDay
import TestCase

class Day22 : AdventDay<List<Day22.Brick>, Int, Int>(2023, 22) {
    data class Point3D(val x: Int, val y: Int, val z: Int)
    data class Brick(val start: Point3D, val end: Point3D) {
        val x = start.x..end.x
        val y = start.y..end.y
        val z = start.z..end.z
    }

    class Tower(bricks: List<Brick>) {
        val fallenBricks: List<Brick>
        val standsOn: Map<Brick, Set<Brick>>
        val supports: Map<Brick, Set<Brick>>

        init {
            val (maxX, maxY) = bricks.fold(0 to 0) { (maxX, maxY), b ->
                maxOf(maxX, b.end.x) to maxOf(maxY, b.end.y)
            }
            val heightMap = Array(maxX + 1) { IntArray(maxY + 1) { 0 } }
            val brickAt = mutableMapOf<Point3D, Brick>()
            fallenBricks = bricks.sortedBy { it.start.z }.map { b ->
                val newZ1 = b.x.maxOf { x -> b.y.maxOf { y -> heightMap[x][y] } } + 1
                val newZ2 = (newZ1 + b.end.z - b.start.z)
                b.x.forEach { x -> b.y.forEach { y -> heightMap[x][y] = newZ2 } }
                val fallen = Brick(Point3D(b.start.x, b.start.y, newZ1), Point3D(b.end.x, b.end.y, newZ2))
                fallen.x.forEach { x ->
                    fallen.y.forEach { y ->
                        fallen.z.forEach { z ->
                            brickAt[Point3D(x, y, z)] = fallen
                        }
                    }
                }
                fallen
            }
            standsOn = buildMap {
                fallenBricks.forEach { b ->
                    put(b, b.x.flatMap { x -> b.y.map { y -> Point3D(x, y, b.end.z + 1) } }
                        .mapNotNull { brickAt[it] }.toSet())
                }
            }
            supports = buildMap {
                fallenBricks.forEach { b ->
                    put(b, b.x.flatMap { x -> b.y.map { y -> Point3D(x, y, b.start.z - 1) } }
                        .mapNotNull { brickAt[it] }.toSet())
                }
            }
        }
    }

    override fun parseInput(stringInput: List<String>) = stringInput.map { line ->
        val (start, end) = line.split("~").map { s ->
            val (x, y, z) = s.split(",").map { it.toInt() }
            Point3D(x, y, z)
        }
        Brick(start, end)
    }

    override fun part1(input: List<Brick>, testArg: Any?): Int {
        val tower = Tower(input)
        return tower.fallenBricks.count { brick ->
            tower.standsOn[brick]!!.let { bricks ->
                bricks.isEmpty() || bricks.all { tower.supports[it]!!.size > 1 }
            }
        }
    }

    override fun part2(input: List<Brick>, testArg: Any?): Int {
        val tower = Tower(input)
        return tower.fallenBricks.sumOf { b ->
            val falling = mutableSetOf(b)
            val toProcess = ArrayDeque<Brick>()
            toProcess.addLast(b)
            while (toProcess.isNotEmpty()) {
                val cur = toProcess.removeFirst()
                tower.standsOn[cur]!!.filter { (tower.supports[it]!! - falling).isEmpty() }.also {
                    toProcess.addAll(it)
                    falling.addAll(it)
                }
            }
            falling.size - 1
        }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 5))

    override fun testCases2() = listOf(TestCase(getTestInput(), 7))
}

fun main() {
    Day22().main()
}
