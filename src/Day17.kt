fun main() {
    fun hitsTarget(x: Int, y: Int, dx: Int, dy: Int, targetX: IntRange, targetY: IntRange): Boolean =
        if (y < targetY.minOf { it } || x > targetX.maxOf { it }) {
            false
        } else if (x in targetX && y in targetY) {
            true
        } else if (dx == 0 && x !in targetX) {
            false
        } else {
            hitsTarget(x + dx, y + dy, maxOf(dx - 1, 0), dy - 1, targetX, targetY)
        }

    fun part1(targetX: IntRange, targetY: IntRange): Int {
        for (dy in -targetY.first - 1 downTo 0) {
            for (dx in targetX.last downTo 0) {
                if (hitsTarget(0, 0, dx, dy, targetX, targetY)) {
                    return (dy + 1) * dy / 2
                }
            }
        }
        return -1
    }

    fun part2(targetX: IntRange, targetY: IntRange): Int {
        var count = 0
        for (dy in -targetY.first - 1 downTo targetY.first) {
            for (dx in targetX.last downTo 0) {
                if (hitsTarget(0, 0, dx, dy, targetX, targetY)) {
                    count++
                }
            }
        }
        return count
    }

    val testTargetX = 20..30
    val testTargetY = -10..-5
    check(part1(testTargetX, testTargetY) == 45)
    check(part2(testTargetX, testTargetY) == 112)

    val targetX = 70..96
    val targetY = -179..-124
    println(part1(targetX, targetY))
    println(part2(targetX, targetY))
}

