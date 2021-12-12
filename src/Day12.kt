fun main() {
    val START = "start"
    val END = "end"

    fun parseInput(input: List<String>) =
        input.map { it.split('-') }.fold(mutableMapOf<String, MutableList<String>>()) { map, path ->
            map.merge(path[0], mutableListOf(path[1])) { l1, l2 -> l1.addAll(l2); l1 }
            map.merge(path[1], mutableListOf(path[0])) { l1, l2 -> l1.addAll(l2); l1 }
            map
        }

    fun findPaths1(path: List<String>, map: Map<String, List<String>>, forbidden: Set<String>): Int {
        val last = path.last()
        return if (last == END) {
            1
        } else {
            map[last]!!.sumOf { node ->
                if (forbidden.contains(node)) {
                    0
                } else {
                    val newForbidden = when {
                        node.isLowerCase() -> forbidden.plus(node)
                        else -> forbidden
                    }
                    findPaths1(path + node, map, newForbidden)
                }
            }
        }
    }

    fun findPaths2(path: List<String>, map: Map<String, List<String>>, forbidden: Set<String>): Int {
        val last = path.last()
        if (last == END) {
            return 1
        } else {
            val lowercaseCounts = path.filter { it.isLowerCase() }.groupingBy { it }.eachCount()
            return map[last]!!.sumOf { node ->
                if (forbidden.contains(node)) {
                    0
                } else {
                    val newForbidden = when {
                        node.isLowerCase() -> when {
                            lowercaseCounts.containsKey(node) -> forbidden.plus(lowercaseCounts.keys)
                            lowercaseCounts.filterValues { it == 2 }.isNotEmpty() -> forbidden.plus(node)
                            else -> forbidden
                        }
                        else -> forbidden
                    }
                    findPaths2(path + node, map, newForbidden)
                }
            }
        }
    }

    fun part1(input: List<String>): Int = findPaths1(arrayListOf(START), parseInput(input), setOf(START))

    fun part2(input: List<String>): Int = findPaths2(arrayListOf(START), parseInput(input), setOf(START))

    val testInput1 = readInput("Day12_test1")
    check(part1(testInput1) == 10)
    check(part2(testInput1) == 36)

    val testInput2 = readInput("Day12_test2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)

    val testInput3 = readInput("Day12_test3")
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3_509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
