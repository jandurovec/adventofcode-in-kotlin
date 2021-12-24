import kotlin.math.pow

fun main() {
    data class Values(val divZ: Int, val addX: Int, val addY: Int)

    fun parseInput(name: String) = readInput(name).windowed(18, 18).map {
        Values(
            it[4].split(" ")[2].toInt(),
            it[5].split(" ")[2].toInt(),
            it[15].split(" ")[2].toInt()
        )
    }

    fun monadStep(w: Int, z: Long, values: Values): Long {
        var newZ = z / values.divZ
        if (w.toLong() != (z % 26) + values.addX) {
            newZ = (newZ * 26) + w + values.addY
        }
        return newZ
    }

    fun search(
        values: Array<Values>,
        digits: IntProgression,
        pos: Int = 0,
        z: Long = 0,
        cache: Array<MutableMap<Long, Long>> = Array(values.size) { mutableMapOf() }
    ): Long {
        if (pos == values.size) {
            return if (z == 0L) 0 else -1
        }
        if (cache[pos].contains(z)) {
            return cache[pos][z]!!
        }
        for (w in digits) {
            val rest = search(values, digits, pos + 1, monadStep(w, z, values[pos]), cache)
            if (rest != -1L) {
                return (w * 10.toDouble().pow(values.size - pos - 1).toLong() + rest).also { cache[pos][z] = it }
            }
        }
        return (-1L).also { cache[pos][z] = it }
    }

    fun part1(input: List<Values>) = search(input.toTypedArray(), 9 downTo 1)
    fun part2(input: List<Values>) = search(input.toTypedArray(), 1..9)

    val input = parseInput("Day24")
    println(part1(input))
    println(part2(input))
}
