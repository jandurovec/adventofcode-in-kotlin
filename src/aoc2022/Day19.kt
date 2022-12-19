package aoc2022

import readInput

fun main() {
    data class Materials(val ore: Int = 0, val clay: Int = 0, val obsidian: Int = 0, val geode: Int = 0) {
        operator fun plus(other: Materials) =
            Materials(ore + other.ore, clay + other.clay, obsidian + other.obsidian, geode + other.geode)

        operator fun minus(other: Materials) =
            Materials(ore - other.ore, clay - other.clay, obsidian - other.obsidian, geode - other.geode)

        operator fun get(i: Int) = when (i) {
            0 -> ore
            1 -> clay
            2 -> obsidian
            3 -> geode
            else -> throw NoSuchElementException()
        }

        fun canCover(req: Materials) =
            ore >= req.ore && clay >= req.clay && obsidian >= req.obsidian && geode >= req.geode
    }

    data class State(val inventory: Materials, val increment: Materials)

    operator fun List<Int>.component6() = this[5]

    fun parseInput(input: List<String>): List<List<Pair<Materials, Materials>>> {
        val blueprintRegex =
            "Blueprint \\d+: Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()
        return input.map { bp ->
            val (ore1, ore2, ore3, clay3, ore4, obsidian4) = blueprintRegex.find(bp)!!.destructured.toList()
                .map { it.toInt() }
            listOf(
                Materials(ore1, 0, 0, 0) to Materials(1, 0, 0, 0),
                Materials(ore2, 0, 0, 0) to Materials(0, 1, 0, 0),
                Materials(ore3, clay3, 0, 0) to Materials(0, 0, 1, 0),
                Materials(ore4, 0, obsidian4, 0) to Materials(0, 0, 0, 1)
            )
        }

    }

    fun sumArithmetic(from: Int, size: Int) = if (size <= 0) 0 else (size * (2 * from + size - 1)) / 2

    fun maxGeodes(costs: List<Pair<Materials, Materials>>, time: Int): Int {
        var states = setOf(State(Materials(0, 0, 0, 0), Materials(1, 0, 0, 0)))
        /*
         Since we can only produce one robot per turn, we don't need more material-collecting robots
         than the maximum of what is needed for any particular recipe
         */
        val maxNeeded = costs.map { it.first }.map { listOf(it.ore, it.clay, it.obsidian) }.reduce { prev, next ->
            (0..prev.lastIndex).map { maxOf(prev[it], next[it]) }
        }
        var maxFoundGeodes = 0
        // countdown to 2 mins left as there's no need to produce a robot when there's only 1 min left
        (time downTo 2).forEach { remainingTime ->
            val newStates = mutableSetOf<State>()
            for (state in states) {
                val newInv = state.inventory + state.increment // if nothing is done
                maxFoundGeodes = maxOf(maxFoundGeodes, state.inventory.geode + remainingTime * state.increment.geode)
                // if we can manufacture geode robot, always do that
                val (geodeRobotCost, geodeRobotIncrement) = costs[3]
                if (state.inventory.canCover(geodeRobotCost)) {
                    newStates.add(State(newInv - geodeRobotCost, state.increment + geodeRobotIncrement))
                    maxFoundGeodes =
                        maxOf(
                            maxFoundGeodes,
                            state.inventory.geode + remainingTime * state.increment.geode + remainingTime - 1
                        )
                    continue
                }
                costs.take(3).forEachIndexed { i, (cost, robotIncrement) ->
                    if (maxNeeded[i] > state.increment[i] && state.inventory.canCover(cost)) {
                        newStates.add(State(newInv - cost, state.increment + robotIncrement))
                    }
                }
                newStates.add(State(newInv, state.increment))
            }
            states = newStates.filter {
                // if I keep adding new geode robot every next step
                val potential = sumArithmetic(it.increment.geode, remainingTime - 1)
                it.inventory.geode + potential >= maxFoundGeodes
            }.toSet()
            //println("Remaining: $remainingTime, States: ${states.size}")
        }
        // last step
        return states.maxOf { it.inventory.geode + it.increment.geode }
    }

    fun part1(input: List<String>) =
        parseInput(input).mapIndexed { idx, blueprint ->
            // println("Checking ${idx + 1}")
            (idx + 1) * maxGeodes(blueprint, 24)
        }.sum()


    fun part2(input: List<String>) = parseInput(input).take(3).mapIndexed { idx, blueprint ->
        // println("Checking ${idx + 1}")
        maxGeodes(blueprint, 32)
    }.reduce { a, b -> a * b }

    val testInput = readInput(2022, 19, "test")
    check(part1(testInput) == 33)
    check(part2(testInput.subList(0, 1)) == 56)
    check(part2(testInput.subList(1, 2)) == 62)

    val input = readInput(2022, 19)
    println(part1(input))
    println(part2(input))
}
