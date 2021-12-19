import kotlin.math.absoluteValue

fun main() {
    data class Point3D(val x: Int, val y: Int, val z: Int) {
        fun translate(offset: Point3D) = Point3D(x + offset.x, y + offset.y, z + offset.z)
    }

    data class Navigation(val scanners: List<Point3D>, val beacons: Set<Point3D>)

    val rotations = listOf(
        { p: Point3D -> Point3D(p.x, p.y, p.z) },
        { p: Point3D -> Point3D(p.x, p.z, -p.y) },
        { p: Point3D -> Point3D(p.x, -p.y, -p.z) },
        { p: Point3D -> Point3D(p.x, -p.z, p.y) },

        { p: Point3D -> Point3D(-p.y, p.x, p.z) },
        { p: Point3D -> Point3D(-p.z, p.x, -p.y) },
        { p: Point3D -> Point3D(p.y, p.x, -p.z) },
        { p: Point3D -> Point3D(p.z, p.x, p.y) },

        { p: Point3D -> Point3D(-p.x, -p.y, p.z) },
        { p: Point3D -> Point3D(-p.x, p.z, p.y) },
        { p: Point3D -> Point3D(-p.x, p.y, -p.z) },
        { p: Point3D -> Point3D(-p.x, -p.z, -p.y) },

        { p: Point3D -> Point3D(p.y, -p.x, p.z) },
        { p: Point3D -> Point3D(p.z, -p.x, -p.y) },
        { p: Point3D -> Point3D(-p.y, -p.x, -p.z) },
        { p: Point3D -> Point3D(-p.z, -p.x, p.y) },

        { p: Point3D -> Point3D(p.z, p.y, -p.x) },
        { p: Point3D -> Point3D(-p.y, p.z, -p.x) },
        { p: Point3D -> Point3D(-p.z, -p.y, -p.x) },
        { p: Point3D -> Point3D(p.y, -p.z, -p.x) },

        { p: Point3D -> Point3D(-p.z, p.y, p.x) },
        { p: Point3D -> Point3D(p.y, p.z, p.x) },
        { p: Point3D -> Point3D(p.z, -p.y, p.x) },
        { p: Point3D -> Point3D(-p.y, -p.z, p.x) }
    )

    fun parseInput(name: String): List<Set<Point3D>> {
        val report = mutableListOf<Set<Point3D>>()
        var readings: MutableSet<Point3D>? = null
        readInput(name).forEach { line ->
            if (line.startsWith("--- ")) {
                readings = mutableSetOf()
                report.add(readings!!)
            } else if (line.isNotBlank()) {
                val c = line.split(",").map { it.toInt() }
                readings!!.add(Point3D(c[0], c[1], c[2]))
            }
        }
        return report
    }

    fun align(report: List<Set<Point3D>>): Navigation {
        val scanners = mutableListOf<Point3D>()
        val beacons = mutableSetOf<Point3D>()
        scanners.add(Point3D(0, 0, 0))
        beacons.addAll(report.first())
        val unaligned = report.drop(1).toMutableSet()
        while (unaligned.isNotEmpty()) {
            var offset = Point3D(0, 0, 0)
            var rotation = rotations.first()
            val candidate = unaligned.first { rep ->
                rotations.forEach { r ->
                    val rotated = rep.map(r)
                    beacons.forEach { beacon ->
                        rotated.forEach { beaconCandidate ->
                            offset = Point3D(
                                beacon.x - beaconCandidate.x,
                                beacon.y - beaconCandidate.y,
                                beacon.z - beaconCandidate.z
                            )
                            var matched = 0
                            rotated.map { it.translate(offset) }.forEach {
                                if (beacons.contains(it) && ++matched == 12) {
                                    rotation = r
                                    return@first true
                                }
                            }
                        }
                    }
                }
                false
            }
            scanners.add(offset)
            beacons.addAll(candidate.map(rotation).map { it.translate(offset) })
            unaligned.remove(candidate)
        }
        return Navigation(scanners, beacons)
    }

    fun part1(nav: Navigation) = nav.beacons.size
    fun part2(nav: Navigation) = nav.scanners.maxOf { a ->
        nav.scanners.maxOf { b -> (a.x - b.x).absoluteValue + (a.y - b.y).absoluteValue + (a.z - b.z).absoluteValue }
    }

    val testInput = parseInput("Day19_test")
    val testNavigation = align(testInput)
    check(part1(testNavigation) == 79)
    check(part2(testNavigation) == 3_621)

    val input = parseInput("Day19")
    val navigation = align(input)
    println(part1(navigation))
    println(part2(navigation))
}
