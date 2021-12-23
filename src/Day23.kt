fun main() {
    val costs = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)
    val allowedStops = setOf(1, 2, 4, 6, 8, 10, 11)

    class Burrow(val plan: Array<CharArray>, val amphipods: List<Pair<Int, Int>>) {
        fun homeColumn(name: Char) = 3 + (name.code - 'A'.code) * 2
        fun homeOpen(name: Char): Boolean {
            val homeColumn = homeColumn(name)
            return plan.all { it[homeColumn] == name || !it[homeColumn].isUpperCase() }
        }

        fun homeSpot(col: Int) = plan.indexOfLast { it[col] == '.' }
        fun needsToMove(x: Int, y: Int): Boolean {
            val pod = plan[y][x]
            val homeColumn = homeColumn(pod)
            return x != homeColumn || plan.drop(y + 1).any { it[x] != pod && it[x] != '#' }
        }

        fun toMove(): List<Pair<Int, Int>> = amphipods.filter { needsToMove(it.first, it.second) }

        fun destinations(x: Int, y: Int): List<Pair<Pair<Int, Int>, Int>> {
            val pod = plan[y][x]
            val homeColumn = homeColumn(pod)
            val homePos = homeSpot(homeColumn)
            if (homeOpen(pod)
                // path to hallhway free
                && (1 until y).all { plan[it][x] == '.' }
                // hallway free
                && (minOf(x, homeColumn)..maxOf(x, homeColumn)).all { plan[1][it] == '.' || (y == 1 && it == x) }
            ) {
                // go straight home
                val pathLength = manhattanDist(x, y, x, 1) +
                        manhattanDist(x, 1, homeColumn, 1) +
                        manhattanDist(homeColumn, 1, homeColumn, homePos)
                return listOf((homeColumn to homePos) to (costs[pod]!! * pathLength))
            } else {
                return if (y == 1) emptyList()
                else buildList {
                    var curY = y
                    // go to hallway first
                    while (curY > 1) {
                        if (plan[--curY][x] != '.') {
                            return@buildList
                        }
                    }
                    // go to sides
                    sequenceOf(-1, 1).forEach { dx ->
                        var steps = y - 1 // init with steps to hallway
                        var curX = x + dx
                        while (plan[1][curX] == '.') {
                            steps++
                            if (allowedStops.contains(curX)) {
                                add((curX to 1) to costs[pod]!! * steps)
                            }
                            curX += dx
                        }
                    }
                }
            }
        }
    }

    fun parseInput(name: String): Burrow {
        val data = readInput(name)
        val amphipods = buildList {
            data.forEachIndexed { y, line ->
                line.forEachIndexed { x, c ->
                    if (c.isUpperCase()) {
                        this.add(x to y)
                    }
                }
            }
        }
        return Burrow(data.map { it.toCharArray() }.toTypedArray(), amphipods)
    }

    fun solve(burrow: Burrow, energy: Int = 0, best: Int = Int.MAX_VALUE): Int {
        if (energy > best) {
            return best
        }
        val toMove = burrow.toMove()
        if (toMove.isEmpty()) {
            return energy
        }
        var newBest = best
        toMove.forEach { pos ->
            val amphipod = burrow.plan[pos.second][pos.first]
            val others = toMove.filter { it != pos }
            burrow.destinations(pos.first, pos.second).forEach { (dest, cost) ->
                val newPlan = burrow.plan
                newPlan[pos.second][pos.first] = '.'
                newPlan[dest.second][dest.first] = amphipod
                newBest = solve(Burrow(newPlan, others.plus(dest)), energy + cost, newBest)
                newPlan[pos.second][pos.first] = amphipod
                newPlan[dest.second][dest.first] = '.'
            }
        }
        return newBest
    }

    check(solve(parseInput("Day23_test1")) == 12_521)
    check(solve(parseInput("Day23_test2")) == 44_169)

    println(solve(parseInput("Day23-1")))
    println(solve(parseInput("Day23-2")))
}
