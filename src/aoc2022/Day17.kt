package aoc2022

import readInput

fun main() {
    class Chamber(private val jetPattern: String) {
        inner class Rock(def: List<String>) {
            val data = def.flatMapIndexed { y, s ->
                s.mapIndexedNotNull { x, c -> if (c == '#') x to y else null }
            }.toSet()

            fun canBeAt(pos: Pair<Int, Int>) = data.all { (x, y) ->
                val newX = pos.first + x
                val newY = pos.second + y
                (0..6).contains(newX) && newY > 0 && !this@Chamber.data.contains(pos.first + x to pos.second + y)
            }
        }

        val rocks = listOf(
            listOf("####"),
            listOf(".#.", "###", ".#."),
            listOf("###", "..#", "..#"),
            listOf("#", "#", "#", "#"),
            listOf("##", "##"),
        ).map { Rock(it) }
        var rockIndex = 0
        var jetIndex = 0

        val data = mutableSetOf<Pair<Int, Int>>()
        var top = 0

        fun nextRock(): Rock {
            return rocks[rockIndex].also {
                rockIndex = (rockIndex + 1) % rocks.size
            }
        }

        fun nextJet(): Char {
            return jetPattern[jetIndex].also {
                jetIndex = (jetIndex + 1) % jetPattern.length
            }
        }

        fun step() {
            var pos = 2 to top + 4
            val rock = nextRock()
            while (true) {
                val hOffset = if (nextJet() == '>') 1 to 0 else -1 to 0
                var newPos = pos.first + hOffset.first to pos.second + hOffset.second
                if (rock.canBeAt(newPos)) {
                    pos = newPos
                }
                newPos = pos.first to pos.second - 1
                if (!rock.canBeAt(newPos)) {
                    data.addAll(rock.data.map { (rx, ry) ->
                        pos.first + rx to pos.second + ry
                    })
                    top = maxOf(top, pos.second + rock.data.maxOf { it.second })
                    return
                }
                pos = newPos
            }
        }

        fun topString(lines: Int) = buildString {
            (1..lines).forEach { line ->
                (0..6).forEach { x ->
                    if (data.contains(x to top + 1 - line)) {
                        append('#')
                    } else {
                        append('.')
                    }
                }
            }
        }
    }

    fun findTop(input: String, steps: Long): Long {
        val c = Chamber(input)
        val tops = mutableListOf<Int>()
        val encountered = mutableMapOf<String, Int>()
        var step = 0
        while (true) {
            step++
            c.step()
            tops.add(c.top)
            val current = c.jetIndex.toString() + ',' + c.rockIndex + ',' + c.topString(10)

            if (step.toLong() == steps) {
                return c.top.toLong()
            } else if (encountered.containsKey(current)) {
                // loop found
                val prevStep = encountered[current]!!
                val remainingSteps = steps - step
                val loopSize = step - prevStep
                val offset = (remainingSteps % loopSize).toInt()
                val topLoopIncrement = c.top - tops[prevStep - 1]
                val topOffsetIncrement = tops[prevStep + offset - 1] - tops[prevStep - 1]
                return c.top + (remainingSteps / loopSize) * topLoopIncrement + topOffsetIncrement
            } else {
                encountered[current] = step
            }
        }
    }

    fun part1(input: String) = findTop(input, 2022)
    fun part2(input: String) = findTop(input, 1000000000000)

    val testInput = readInput(2022, 17, "test").first()
    check(part1(testInput) == 3068L)
    check(part2(testInput) == 1514285714288)

    val input = readInput(2022, 17).first()
    println(part1(input))
    println(part2(input))
}
