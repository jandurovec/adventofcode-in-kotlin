package aoc2024

import AdventDay
import TestCase


class Day09 : AdventDay<List<Day09.Block>, Long, Long>(2024, 9) {

    data class Block(val id: Int, val pos: Int, val size: Int)

    override fun parseInput(stringInput: List<String>) = stringInput.first()
        .mapIndexed { index, c -> Block(if (index % 2 == 0) index / 2 else -1, index, c.digitToInt()) }
        .runningReduce { prev, cur -> Block(cur.id, prev.pos + prev.size, cur.size) }

    private fun checksum(mem: List<Block>) = mem.filter { it.id >= 0 }.sumOf { (id, pos, size) ->
        (2L * pos + size - 1) * id * size / 2
    }

    override fun part1(input: List<Block>, testArg: Any?): Long {
        val mem = input.toMutableList()
        var freeIdx = 1
        var toMoveIdx = input.lastIndex
        if (toMoveIdx % 2 == 1) {
            toMoveIdx--
        }
        while (freeIdx < toMoveIdx) {
            val (id, pos, size) = mem[toMoveIdx]
            val freeBlock = mem[freeIdx]
            if (freeBlock.size == size) {
                mem[freeIdx] = Block(id, freeBlock.pos, size)
                mem.removeAt(toMoveIdx)
                freeIdx += 2
                toMoveIdx -= 2
            } else if (freeBlock.size > size) {
                mem[freeIdx] = Block(-1, freeBlock.pos + size, freeBlock.size - size)
                mem.removeAt(toMoveIdx)
                mem.add(freeIdx, Block(id, freeBlock.pos, size))
                freeIdx++
                toMoveIdx--
            } else {
                mem[freeIdx] = Block(id, freeBlock.pos, freeBlock.size)
                mem[toMoveIdx] = Block(id, pos, size - freeBlock.size)
                freeIdx += 2
            }
        }

        return checksum(mem)
    }

    override fun part2(input: List<Block>, testArg: Any?): Long {
        val mem = input.toMutableList()
        var toMoveIdx = input.lastIndex
        while (toMoveIdx > 0) {
            if (mem[toMoveIdx].id > -1) {
                val (id, _, size) = mem[toMoveIdx]
                val freeIdx = mem.indexOfFirst { it.id == -1 && it.size >= size }
                if (freeIdx in 0..<toMoveIdx) {
                    val freeBlock = mem[freeIdx]
                    if (freeBlock.size == size) {
                        mem[freeIdx] = Block(id, freeBlock.pos, size)
                        mem.removeAt(toMoveIdx)
                    } else {
                        mem[freeIdx] = Block(-1, freeBlock.pos + size, freeBlock.size - size)
                        mem.removeAt(toMoveIdx)
                        mem.add(freeIdx, Block(id, freeBlock.pos, size))
                        toMoveIdx++
                    }
                }
            }
            toMoveIdx--
        }

        return checksum(mem)
    }


    override fun testCases1() = listOf(TestCase(getTestInput(), 1928L))

    override fun testCases2() = listOf(TestCase(getTestInput(), 2858L))

}

fun main() {
    Day09().main()
}
