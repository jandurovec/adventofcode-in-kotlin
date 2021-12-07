fun main() {
    fun parseInput(name: String) = readInput(name).first().toIntList()

    // count of offsprings of fish with counter = 0 after N days (indexed by N)
    val offsprings = mutableMapOf<Int, Long>()
    fun offspringCount(counter: Int, days: Int): Long {
        return when {
            counter >= days -> 0L
            counter > 0 -> offspringCount(0, days - counter)
            else -> offsprings.getOrPut(days) { 1 + offspringCount(0, days - 7) + offspringCount(0, days - 9) }
        }
    }

    fun solve(fish: List<Int>, days: Int) = fish.sumOf { 1 + offspringCount(it, days) }

    val testInput = parseInput("Day06_test")

    check(solve(testInput, 18) == 26L)
    check(solve(testInput, 80) == 5_934L)
    check(solve(testInput, 256) == 26_984_457_539L)

    val input = parseInput("Day06")
    println(solve(input, 80))
    println(solve(input, 256))
}
