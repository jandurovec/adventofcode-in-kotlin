fun main() {
    fun check(line: String): Pair<Int, ArrayDeque<Char>> {
        val pairs = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
        val score = mapOf(')' to 3, ']' to 57, '}' to 1_197, '>' to 25_137)

        val stack = ArrayDeque<Char>()
        line.forEach {
            if (pairs.containsKey(it)) {
                stack.addFirst(pairs[it]!!)
            } else {
                if (stack.isNotEmpty() && stack.first() == it) {
                    stack.removeFirst()
                } else {
                    return score[it]!! to stack
                }
            }
        }
        return 0 to stack
    }

    fun part1(lines: List<String>) = lines.sumOf { check(it).first }

    fun part2(lines: List<String>): Long {
        val score = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
        val scores = lines.map { check(it) }.filter { it.first == 0 }.map {
            it.second.fold(0L) { agg, char -> 5 * agg + score[char]!! }
        }.sorted()
        return scores[scores.size / 2]
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26_397)
    check(part2(testInput) == 288_957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
