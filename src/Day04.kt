fun main() {
    val boardSize = 5

    class BingoBoard(input: List<String>) {
        val rowCounts = Array(boardSize) { 0 }
        val colCounts = Array(boardSize) { 0 }
        val data = mutableMapOf<Int, Pair<Int, Int>>()
        var unmarkedSum: Int
        var victory = false

        init {
            input.mapIndexed { row, rowData ->
                rowData.trim().split(" +".toRegex()).mapIndexed { col, s ->
                    data[s.toInt()] = row to col
                }
            }
            unmarkedSum = data.keys.sumOf { it }
        }

        fun mark(n: Int): Boolean {
            val loc = data[n]
            if (loc != null) {
                rowCounts[loc.first] = rowCounts[loc.first] + 1
                colCounts[loc.second] = colCounts[loc.second] + 1
                unmarkedSum -= n
                if (rowCounts[loc.first] == boardSize || colCounts[loc.second] == boardSize) {
                    victory = true
                    return true
                }
            }
            return false
        }
    }

    fun parseInput(input: List<String>): Pair<List<Int>, List<BingoBoard>> {
        val draw = input[0].split(',').map { it.toInt() }
        return draw to input.subList(1, input.size).windowed(boardSize + 1, boardSize + 1)
            .map { BingoBoard(it.subList(1, boardSize + 1)) }
    }

    fun part1(input: List<String>): Int {
        val (draw, boards) = parseInput(input)
        for (n in draw) {
            for (board in boards) {
                if (board.mark(n)) {
                    return board.unmarkedSum * n
                }
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        var (draw, boards) = parseInput(input)
        for (n in draw) {
            for (board in boards) {
                if (board.mark(n) && boards.size == 1) {
                    return board.unmarkedSum * n
                }
            }
            boards = boards.filter { !it.victory }
        }
        return -1
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
