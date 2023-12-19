package aoc2023

import AdventDay
import TestCase
import size
import split

class Day19 : AdventDay<Day19.Input, Int, Long>(2023, 19) {

    data class Input(val workflows: Map<String, List<String>>, val rankings: List<Map<Char, Int>>)

    override fun parseInput(stringInput: List<String>): Input {
        val (rawWorkflows, rawRatings) = stringInput.split()
        val rankings = rawRatings.map { line ->
            line.trim('{', '}').split(",").map { entry ->
                val (k, v) = entry.split("=")
                k[0] to v.toInt()
            }.associate { it }
        }
        val workflows = buildMap {
            rawWorkflows.forEach { line ->
                val (flowName, flowDesc) = line.split("{")
                this[flowName] = flowDesc.trim('}').split(",")
            }
        }
        return Input(workflows, rankings)
    }

    override fun part1(input: Input, testArg: Any?): Int {
        fun eval(rating: Map<Char, Int>, workflow: String = "in", offset: Int = 0): Boolean {
            if (workflow == "A") {
                return true
            } else if (workflow == "R") {
                return false
            } else {
                val rule = input.workflows[workflow]!![offset]
                if (rule.contains(":")) {
                    val (condition, destination) = rule.split(":")
                    val what = condition[0]
                    val op = condition[1]
                    val threshold = condition.substring(2).toInt()
                    return if (op == '>' && rating[what]!! > threshold || op == '<' && rating[what]!! < threshold) {
                        eval(rating, destination)
                    } else {
                        eval(rating, workflow, offset + 1)
                    }
                } else {
                    return eval(rating, rule)
                }
            }
        }
        return input.rankings.filter { eval(it) }.sumOf { it.values.sum() }
    }

    override fun part2(input: Input, testArg: Any?): Long {
        val max = 4000
        fun eval(ranges: Map<Char, IntRange>, workflow: String = "in", offset: Int = 0): Sequence<Map<Char, IntRange>> =
            sequence {
                if (workflow == "A") {
                    yield(ranges)
                } else if (workflow != "R") {
                    val rule = input.workflows[workflow]!![offset]
                    if (rule.contains(":")) {
                        val (condition, destination) = rule.split(":")
                        val what = condition[0]
                        val op = condition[1]
                        val threshold = condition.substring(2).toInt()
                        val origRange = ranges[what]!!
                        val (accepted, rejected) = if (op == '<') listOf(
                            origRange.first..minOf(origRange.last, threshold - 1),
                            maxOf(origRange.first, threshold)..origRange.last
                        ) else listOf(
                            maxOf(origRange.first, threshold + 1)..origRange.last,
                            origRange.first..minOf(origRange.last, threshold)
                        )
                        if (!accepted.isEmpty()) {
                            yieldAll(eval(ranges.filter { it.key != what } + (what to accepted), destination))
                        }
                        if (!rejected.isEmpty()) {
                            yieldAll(eval(ranges.filter { it.key != what } + (what to rejected), workflow, offset + 1))
                        }
                    } else {
                        yieldAll(eval(ranges, rule))
                    }
                }
            }
        return eval(mapOf('x' to 1..max, 'm' to 1..max, 'a' to 1..max, 's' to 1..max))
            .sumOf { acceptable -> acceptable.values.map { it.size().toLong() }.reduce(Long::times) }
    }

    override fun testCases1() = listOf(TestCase(getTestInput(), 19114))

    override fun testCases2() = listOf(TestCase(getTestInput(), 167409079868000))
}

fun main() {
    Day19().main()
}
