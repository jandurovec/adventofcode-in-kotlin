package aoc2022

import readInput

fun main() {
    class CircularListElement(val value: Long) {
        var next = this
        var prev = this

        fun add(other: CircularListElement): CircularListElement {
            val oldPrev = this.prev
            this.prev = other.prev
            other.prev.next = this
            other.prev = oldPrev
            oldPrev.next = other
            return this
        }

        private fun moveForward(n: Int) {
            repeat(n) {
                val oldPrev = this.prev
                val oldNext = this.next
                this.next = oldNext.next
                this.next.prev = this
                this.prev = oldNext
                this.prev.next = this
                oldPrev.next = oldNext
                oldNext.prev = oldPrev
            }
        }

        private fun moveBack(n: Int) {
            repeat(n) {
                val oldPrev = this.prev
                val oldNext = this.next
                this.prev = oldPrev.prev
                this.prev.next = this
                this.next = oldPrev
                this.next.prev = this
                oldNext.prev = oldPrev
                oldPrev.next = oldNext
            }
        }

        fun move(modulus: Int) {
            val offset = (value % modulus).toInt()
            if (offset > 0) {
                moveForward(offset)
            } else if (offset < 0) {
                moveBack(-offset)
            }
        }

        fun find(needle: Long): CircularListElement {
            val stop = this
            var cur = this
            do {
                if (cur.value == needle) {
                    return cur
                }
                cur = cur.next
            } while (cur != stop)
            throw NoSuchElementException()
        }

        operator fun get(offset: Int): CircularListElement {
            var cur = this
            repeat(offset) {
                cur = cur.next
            }
            return cur
        }
    }

    fun parseInput(input: List<String>) = input.map { it.toLong() }

    fun mixAndExtract(input: List<String>, decryptionKey: Int = 1, mixCount: Int = 1) =
        parseInput(input).map { CircularListElement(it * decryptionKey) }.let { elements ->
            val zero = elements.reduce { a, b -> a.add(b) }.find(0)
            repeat(mixCount) {
                elements.forEach {
                    it.move(input.size - 1)
                }
            }
            listOf(1000, 2000, 3000).map { it % elements.size }.map { zero[it].value }.reduce(Long::plus)
        }

    fun part1(input: List<String>) = mixAndExtract(input)
    fun part2(input: List<String>) = mixAndExtract(input, 811589153, 10)

    val testInput = readInput(2022, 20, "test")
    check(part1(testInput) == 3L)
    check(part2(testInput) == 1623178306L)

    val input = readInput(2022, 20)
    println(part1(input))
    println(part2(input))
}
