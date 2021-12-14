fun main() {
    data class Polymer(var start: Char = ' ', var segments: MutableMap<String, Long> = mutableMapOf())

    fun parseInput(input: List<String>) =
        input.fold(Polymer() to mutableMapOf<String, String>()) { data, line ->
            val (poly, rules) = data
            when {
                line.contains(" -> ") -> line.split(" -> ").let { (from, to) -> rules[from] = to }
                line.isNotBlank() -> {
                    poly.start = line[0]
                    for (i in 0 until line.lastIndex) {
                        poly.segments.merge(line.substring(i, i + 2), 1, Long::plus)
                    }
                }
            }
            data
        }

    fun polymerise(input: List<String>, steps: Int): Long {
        var (polymer, rules) = parseInput(input)
        repeat(steps) {
            val newPolymer = Polymer(polymer.start, mutableMapOf())
            polymer.segments.forEach { (segment, count) ->
                val insert = rules[segment]
                if (insert != null) {
                    newPolymer.segments.merge(segment[0] + insert, count, Long::plus)
                    newPolymer.segments.merge(insert + segment[1], count, Long::plus)
                } else {
                    newPolymer.segments.merge(segment, count, Long::plus)
                }
            }
            polymer = newPolymer
        }
        val counts = mutableMapOf(polymer.start to 1L)
        for (e in polymer.segments.entries) {
            counts.merge(e.key[1], e.value, Long::plus)
        }
        return counts.maxOf { it.value } - counts.minOf { it.value }
    }

    val testInput1 = readInput("Day14_test")
    check(polymerise(testInput1, 10) == 1_588L)
    check(polymerise(testInput1, 40) == 2_188_189_693_529)

    val input = readInput("Day14")
    println(polymerise(input, 10))
    println(polymerise(input, 40))
}
