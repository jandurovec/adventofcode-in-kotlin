package aoc2023

import AdventDay
import TestCase
import factorize
import gcd
import java.math.BigDecimal

class Day24 : AdventDay<List<Day24.Hailstone>, Int, BigDecimal>(2023, 24) {
    companion object {
        val EMPTY_RANGE = BigDecimal.ONE..BigDecimal.ZERO
    }

    data class Stone1d(val position: Long, val velocity: Long)
    data class Hailstone(val x: Stone1d, val y: Stone1d, val z: Stone1d)


    override fun parseInput(stringInput: List<String>) = stringInput.map { s ->
        s.replace(" ", "").split("@")
            .map { comp -> comp.split(",").map { it.toLong() } }
            .let { (pos, vel) -> pos.zip(vel).map { (p, v) -> Stone1d(p, v) } }
            .let { (x, y, z) -> Hailstone(x, y, z) }
    }

    private fun intersect2d(
        x1: Stone1d,
        y1: Stone1d,
        x2: Stone1d,
        y2: Stone1d,
        xRange: ClosedRange<BigDecimal>,
        yRange: ClosedRange<BigDecimal>
    ): Pair<ClosedRange<BigDecimal>, ClosedRange<BigDecimal>> {
        val firstValidX =
            (listOf(x1, x2).filter { it.velocity > 0 }.map { it.position.toBigDecimal() } + xRange.start).max()
        val lastValidX =
            (listOf(x1, x2).filter { it.velocity < 0 }.map { it.position.toBigDecimal() } + xRange.endInclusive).min()
        val firstValidY =
            (listOf(y1, y2).filter { it.velocity > 0 }.map { it.position.toBigDecimal() } + yRange.start).max()
        val lastValidY =
            (listOf(y1, y2).filter { it.velocity < 0 }.map { it.position.toBigDecimal() } + yRange.endInclusive).min()
        if (y1.velocity.toBigDecimal() * x2.velocity.toBigDecimal() == y2.velocity.toBigDecimal() * x1.velocity.toBigDecimal()) {
            // parallel lines
            return if ((y2.position.toBigDecimal() - y1.position.toBigDecimal()) * x1.velocity.toBigDecimal() == y1.velocity.toBigDecimal() * (x2.position.toBigDecimal() - x1.position.toBigDecimal())) {
                firstValidX..lastValidX to firstValidY..lastValidY
            } else {
                EMPTY_RANGE to EMPTY_RANGE
            }
        } else {
            val px1 = x1.position.toBigDecimal()
            val px2 = x2.position.toBigDecimal()
            val vx1 = x1.velocity.toBigDecimal()
            val vx2 = x2.velocity.toBigDecimal()
            val py1 = y1.position.toBigDecimal()
            val py2 = y2.position.toBigDecimal()
            val vy1 = y1.velocity.toBigDecimal()
            val vy2 = y2.velocity.toBigDecimal()
            // solving the following equation set
            // (x - px1)/vx1 = (y - py1)/vy1
            // (x - px2)/vx2 = (y - py2)/vy2
            // ------------------------------
            // vy1 * x - vy1 * px1 = vx1 * y - vx1 * py1
            // vy2 * x - vy2 * px2 = vx2 * y - vx2 * py2
            // ------------------------------
            // (vx2 * vy1 - vx1 * vy2) * x - vx2 * vy1 * px1 + vx1 * vy2 * px2 = vx1 * vx2 * py2 - vx1 * vx2 * py1
            // ------------------------------
            // x = (vx1 * vx2 * py2 - vx1 * vx2 * py1 + vx2 * vy1 * px1 - vx1 * vy2 * px2) / (vx2 * vy1 - vx1 * vy2)
            val xNom = (vx1 * vx2 * py2 - vx1 * vx2 * py1 + vx2 * vy1 * px1 - vx1 * vy2 * px2)
            val xDen = (vx2 * vy1 - vx1 * vy2)
            val x = xNom / xDen
            val yNom = (vy1 * x - vy1 * px1 + py1 * vx1)
            val y = yNom / vx1
            val resultXRange = maxOf(x, firstValidX)..minOf(x, lastValidX)
            val resultYRange = maxOf(y, firstValidY)..minOf(y, lastValidY)
            return resultXRange to resultYRange
        }
    }

    override fun part1(input: List<Hailstone>, testArg: Any?): Int {
        val range = (if (testArg is LongRange) testArg else 200000000000000..400000000000000).let {
            it.first.toBigDecimal()..it.last.toBigDecimal()
        }
        return (0..<input.lastIndex).sumOf { h1i ->
            (h1i + 1..input.lastIndex).count { h2i ->
                val h1 = input[h1i]
                val h2 = input[h2i]
                val (xRange, yRange) = intersect2d(h1.x, h1.y, h2.x, h2.y, range, range)
                !(xRange.isEmpty() || yRange.isEmpty())
            }
        }
    }

    override fun part2(input: List<Hailstone>, testArg: Any?): BigDecimal {
        fun Long.divisors(): Sequence<Long> {
            fun divisors(factorized: Map<Long, Int>): Sequence<Long> = sequence {
                if (factorized.isEmpty()) yield(1) else {
                    val (k, v) = factorized.entries.first()
                    (1..v).runningFold(1L) { acc, _ -> acc * k }.asSequence().forEach { n ->
                        yieldAll(divisors(factorized.filter { it.key != k }).map { n * it })
                    }
                }
            }
            return divisors(this.factorize())
        }

        fun findAxisVelocityCandidates(comp: List<Stone1d>) =
            comp.groupBy { it.velocity }.filter { it.value.size > 1 }.map { (vel, stones) ->
                // for hailstone with the same speed, the speed diff (rock vs. hailstone) must divide the distance
                val sorted = stones.sortedBy { it.position }
                (0..<sorted.lastIndex).flatMap { h1 ->
                    (h1 + 1..sorted.lastIndex).map { h2 -> sorted[h2].position - sorted[h1].position }
                }.reduce(::gcd) // GCD of distances
                    .divisors().flatMap { sequenceOf(-it, +it) }.map { it + vel } // valid velocities
                    .toSet()
            }.reduce { a, b -> a.intersect(b) }

        findAxisVelocityCandidates(input.map { it.x }).forEach { vx ->
            findAxisVelocityCandidates(input.map { it.y }).forEach { vy ->
                findAxisVelocityCandidates(input.map { it.z }).forEach { vz ->
                    // if the flying rock intersects all trajectories, then from the point of view of the stone
                    // (if it was stationary) all trajectories should intersect in one point
                    val relX = input.map { it.x }.map { Stone1d(it.position, it.velocity - vx) }
                    val relY = input.map { it.y }.map { Stone1d(it.position, it.velocity - vy) }
                    val relZ = input.map { it.z }.map { Stone1d(it.position, it.velocity - vz) }
                    val fullRange = Long.MIN_VALUE.toBigDecimal()..Long.MAX_VALUE.toBigDecimal()
                    // find intersection on xy plane
                    val (xRange, yRange) = relX.zip(relY)
                        .fold(fullRange to fullRange) { (xr, yr), (x, y) ->
                            if (xr.isEmpty() || yr.isEmpty()) xr to yr else intersect2d(relX[0], relY[0], x, y, xr, yr)
                        }
                    // find intersection on xz plane with x in calculated valid range
                    if (!xRange.isEmpty() && !yRange.isEmpty()) {
                        val (xRangeReduced, zRange) = relX.zip(relZ)
                            .fold(xRange to fullRange) { (xr, zr), (x, z) ->
                                if (xr.isEmpty() || zr.isEmpty()) xr to zr else
                                    intersect2d(relX[0], relZ[0], x, z, xr, zr)
                            }
                        if (!xRangeReduced.isEmpty() && !zRange.isEmpty()) {
                            if (xRangeReduced.start == xRangeReduced.endInclusive && yRange.start == yRange.endInclusive && zRange.start == zRange.endInclusive)
                                return xRangeReduced.start + yRange.start + zRange.start
                        }
                    }
                }
            }
        }
        error("No stone can hit all hailstones")
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 2, 7L..27L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 47.toBigDecimal()))
}

fun main() {
    Day24().main()
}
