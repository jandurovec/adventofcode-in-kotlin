package aoc2017

import AdventDay
import kotlin.math.absoluteValue

class Day20 : AdventDay<List<Day20.Particle>, Int, Int>(2017, 20) {
    companion object {
        val POINT_REGEX = "p=<(.*),(.*),(.*)>, v=<(.*),(.*),(.*)>, a=<(.*),(.*),(.*)>".toRegex()
    }

    data class Point3d(val x: Int, val y: Int, val z: Int) {
        val size: Int
            get() = x.absoluteValue + y.absoluteValue + z.absoluteValue

        fun dist(other: Point3d) =
            (x - other.x).absoluteValue + (y - other.y).absoluteValue + (z - other.z).absoluteValue

        operator fun plus(other: Point3d) = Point3d(x + other.x, y + other.y, z + other.z)
    }

    data class Particle(val id: Int, val pos: Point3d, val v: Point3d, val a: Point3d) {
        val nextV = v + a
        val nextPos = pos + nextV
    }

    override fun parseInput(stringInput: List<String>) = stringInput.mapIndexed { index, s ->
        val data = POINT_REGEX.matchEntire(s)!!.groupValues.drop(1).map { it.toInt() }
        Particle(
            index,
            Point3d(data[0], data[1], data[2]),
            Point3d(data[3], data[4], data[5]),
            Point3d(data[6], data[7], data[8])
        )
    }

    override fun part1(input: List<Particle>, testArg: Any?) = input.minWith { p1, p2 ->
        (p1.a.size - p2.a.size).let { compA ->
            if (compA == 0) {
                (p1.v.size - p2.v.size).let { compV -> if (compV == 0) p1.pos.size - p2.pos.size else compV }
            } else compA
        }
    }.id

    override fun part2(input: List<Particle>, testArg: Any?): Int {
        fun List<Particle>.removeCollisions(): List<Particle> {
            val map = groupBy { it.pos }.toMutableMap()
            val it = map.entries.iterator()
            while (it.hasNext()) {
                if (it.next().value.size > 1) {
                    it.remove()
                }
            }
            return map.values.flatten()
        }

        var particles = input.removeCollisions()
        while (particles.any { p1 -> particles.any { p2 -> p1.pos.dist(p2.pos) > (p1.nextPos).dist(p2.nextPos) } }) {
            particles = particles.map { Particle(it.id, it.nextPos, it.nextV, it.a) }.removeCollisions()
        }
        return particles.count()
    }

}

fun main() {
    Day20().main()
}
