package aoc2016

import Point
import SingleStringDay
import TestCase
import md5

class Day17 : SingleStringDay<String, Int>(2016, 17) {
    companion object {
        const val SIZE = 4
        val END = Point(SIZE - 1, SIZE - 1)
    }

    data class State(val pos: Point, val path: String)

    private fun generatePoints(passCode: String) = sequence {
        val start = Point(0, 0)
        val toExplore = ArrayDeque<State>()
        toExplore.addLast(State(start, ""))
        while (toExplore.isNotEmpty()) {
            val cur = toExplore.removeFirst()
            yield(cur)
            if (cur.pos != END) {
                val openDoors = (passCode + cur.path).md5().padStart(32, '0').subSequence(0, 4).let { hash ->
                    listOf(
                        'U' to Point(0, -1), 'D' to Point(0, 1), 'L' to Point(-1, 0), 'R' to Point(1, 0)
                    ).filterIndexed { index, _ ->
                        hash[index] in 'b'..'f'
                    }
                }
                openDoors.map { (c, d) -> c to (cur.pos + d) }
                    .filter { (_, p) -> p.x in 0 until SIZE && p.y in 0 until SIZE }.forEach { (c, p) ->
                        toExplore.addLast(State(p, cur.path + c))
                    }
            }
        }
    }

    override fun part1(input: String, testArg: Any?) = generatePoints(input).first { (pos, _) -> pos == END }.path
    override fun part2(input: String, testArg: Any?) = generatePoints(input).last { (pos, _) -> pos == END }.path.length

    override fun testCases1() = listOf(
        TestCase("ihgpwlah", "DDRRRD"),
        TestCase("kglvqrro", "DDUDRLRRUDRD"),
        TestCase("ulqzkmiv", "DRURDRUDDLLDLUURRDULRLDUUDDDRR")
    )

    override fun testCases2() = listOf(
        TestCase("ihgpwlah", 370),
        TestCase("kglvqrro", 492),
        TestCase("ulqzkmiv", 830)
    )
}

fun main() {
    Day17().main()
}
