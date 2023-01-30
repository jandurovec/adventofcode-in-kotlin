package aoc2017

import SingleStringDay
import TestCase

class Day09 : SingleStringDay<Int, Int>(2017, 9) {

    interface Node {
        fun score() = 0
        fun countGarbage(): Int
    }

    class Group(private val depth: Int, private val children: List<Node>) : Node {
        override fun score(): Int = depth + children.filterIsInstance<Group>().sumOf { it.score() }
        override fun countGarbage() = children.sumOf { it.countGarbage() }
    }

    class Garbage(private val content: String) : Node {
        override fun countGarbage() = content.length
    }

    class Parser(val s: String) {
        private var pos = 0
        private var depth = 0

        private fun parseGroup(): Group {
            depth++
            pos++
            val children = mutableListOf<Node>()
            while (s[pos] != '}') {
                children.add(parse())
                if (s[pos] == ',') {
                    pos++
                }
            }
            pos++
            return Group(depth--, children)
        }

        private fun parseGarbage(): Garbage {
            return Garbage(buildString {
                var cancelled = false
                pos++
                while (cancelled || s[pos] != '>') {
                    if (cancelled) {
                        cancelled = false
                    } else if (s[pos] == '!') {
                        cancelled = true
                    } else {
                        append(s[pos])
                    }
                    pos++
                }
                pos++
            })
        }

        fun parse() = when (s[pos]) {
            '{' -> parseGroup()
            '<' -> parseGarbage()
            else -> error("Unexpected character ${s[pos]} at position $pos")
        }
    }

    override fun part1(input: String, testArg: Any?) = Parser(input).parse().score()
    override fun part2(input: String, testArg: Any?) = Parser(input).parse().countGarbage()

    override fun testCases1() = listOf(
        TestCase("{}", 1),
        TestCase("{{{}}}", 6),
        TestCase("{{},{}}", 5),
        TestCase("{{{},{},{{}}}}", 16),
        TestCase("{<a>,<a>,<a>,<a>}", 1),
        TestCase("{{<ab>},{<ab>},{<ab>},{<ab>}}", 9),
        TestCase("{{<!!>},{<!!>},{<!!>},{<!!>}}", 9),
        TestCase("{{<a!>},{<a!>},{<a!>},{<ab>}}", 3)
    )

    override fun testCases2() = listOf(
        TestCase("""<>""", 0),
        TestCase("""<random characters>""", 17),
        TestCase("""<<<<>""", 3),
        TestCase("""<{!>}>""", 2),
        TestCase("""<!!>""", 0),
        TestCase("""<!!!>>""", 0),
        TestCase("""<{o"i!a,<{i<a>""", 10)
    )
}

fun main() {
    Day09().main()
}
