package aoc2022

import manhattanDist
import readInput
import size
import subtractRange
import union
import kotlin.math.absoluteValue

fun main() {

    val cmdRegex = """Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)""".toRegex()

    class Reading(val sensor: Pair<Int, Int>, val beacon: Pair<Int, Int>) {
        val dist = manhattanDist(sensor.first, sensor.second, beacon.first, beacon.second)
    }

    fun List<Reading>.rangesAt(row: Int) = this.mapNotNull { r ->
        val diff = r.dist - (r.sensor.second - row).absoluteValue
        if (diff >= 0) {
            r.sensor.first - diff..r.sensor.first + diff
        } else null
    }.union()

    fun parseInput(input: List<String>) = input.map {
        cmdRegex.find(it)!!.destructured.toList()
    }.map { it.map(String::toInt) }.map { (sx, sy, bx, by) ->
        Reading(sx to sy, bx to by)
    }

    fun part1(input: List<String>, row: Int) = parseInput(input).let { readings ->
        val beacons = readings.map { it.beacon }
            .filter { it.second == row }
            .map { it.first }.toSet()
        readings.rangesAt(row).sumOf { it.size() - beacons.count { b -> it.contains(b) } }
    }

    fun part2(input: List<String>, max: Int): Long = parseInput(input).let { readings ->
        for (row in 0..max) {
            readings.rangesAt(row).fold(listOf(0..max)) { acc, next -> acc.subtractRange(next) }.let { free ->
                if (free.isNotEmpty()) {
                    return 4000000 * free.first().first.toLong() + row.toLong()
                }
            }
        }
        error("Empty location not found")
    }

    val testInput = readInput(2022, 15, "test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput(2022, 15)
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}
