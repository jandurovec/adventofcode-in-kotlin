package aoc2022

import Rational
import readInput

fun main() {
    fun parseInput(input: List<String>) = input.map { line -> line.split(": ") }.associate { (k, v) -> k to v }

    fun part1(input: List<String>) = parseInput(input).let { map ->
        fun getValue(name: String): Long {
            val s = map[name]!!
            val l = s.toLongOrNull()
            return if (l != null) {
                l
            } else {
                val (m1, op, m2) = s.split(" ")
                when (op) {
                    "+" -> getValue(m1) + getValue(m2)
                    "-" -> getValue(m1) - getValue(m2)
                    "*" -> getValue(m1) * getValue(m2)
                    else -> getValue(m1) / getValue(m2)
                }
            }
        }
        getValue("root")
    }

    fun part2(input: List<String>) = parseInput(input).let { map ->
        /**
         * Represents "a * humn + b"
         */
        class Value(val a: Rational, val b: Rational) {
            operator fun plus(other: Value) = Value(a + other.a, b + other.b)
            operator fun minus(other: Value) = Value(a - other.a, b - other.b)
            operator fun times(other: Value) =
                if (a.n == 0L) {
                    Value(b * other.a, b * other.b)
                } else if (other.a.n == 0L) {
                    Value(other.b * a, other.b * b)
                } else {
                    error("Monkeys don't solve quadratic equations")
                }

            operator fun div(other: Value) =
                if (other.a.n == 0L) {
                    Value(a / other.b, b / other.b)
                } else {
                    error("Not ready to divide by humans")
                }

            override fun toString() = if (a.n != 0L) "$a * humn + $b" else b.toString()
        }

        abstract class Node {
            abstract fun value(): Value
        }

        class Expr(val name: String, val left: Node, val op: String, val right: Node) : Node() {
            override fun value(): Value {
                if (name == "root") {
                    val l = left.value()
                    val r = right.value()
                    return if (l.a.n != 0L) {
                        Value(Rational(0), (r.b - l.b) / l.a)
                    } else if (r.a.n != 0L) {
                        Value(Rational(0), (l.b - r.b) / r.a)
                    } else {
                        error("No humans found")
                    }
                } else {
                    return when (op) {
                        "+" -> left.value() + right.value()
                        "-" -> left.value() - right.value()
                        "*" -> left.value() * right.value()
                        else -> left.value() / right.value()
                    }
                }
            }

            override fun toString() = if (name == "root") "${left.value()} == ${right.value()}" else "${value()}"
        }

        class Literal(val name: String, val l: Long) : Node() {
            override fun value() =
                if (name == "humn") Value(Rational(1), Rational(0)) else Value(Rational(0), Rational(l))
        }

        fun buildNode(name: String): Node {
            val s = map[name]!!
            val l = s.toLongOrNull()
            return if (l != null) {
                Literal(name, l)
            } else {
                val (m1, op, m2) = s.split(" ")
                Expr(name, buildNode(m1), op, buildNode(m2))
            }
        }

        val root = buildNode("root")
        root.value().b.n
    }

    val testInput = readInput(2022, 21, "test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 301L)

    val input = readInput(2022, 21)
    println(part1(input))
    println(part2(input))
}
