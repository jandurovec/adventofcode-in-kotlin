fun main() {
    fun part1(input: List<Int>): Int {
        return input.windowed(2, 1)
                .sumOf { l -> (if (l[1] > l[0]) 1 else 0) as Int }
    }

    fun part2(input: List<Int>): Int {
        return part1(input.windowed(3, 1).map { l -> l.sum() })
    }

    val testInput = readNumInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readNumInput("Day01")
    println(part1(input))
    println(part2(input))
}
