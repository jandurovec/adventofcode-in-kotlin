package aoc2023

import AdventDay
import TestCase
import gcd

class Day20 : AdventDay<Map<String, Day20.Module>, Int, Long>(2023, 20) {
    data class Signal(val from: String, val to: String, val high: Boolean)

    abstract class Module(open val next: List<String>) {
        abstract fun process(signal: Signal): List<Signal>
    }

    data class FlipFlop(override val next: List<String>) : Module(next) {
        private var on = false
        override fun process(signal: Signal) = if (signal.high) {
            emptyList()
        } else {
            on = !on
            next.map { Signal(signal.to, it, on) }
        }
    }

    data class Conjunction(override val next: List<String>) : Module(next) {
        val inputs = mutableMapOf<String, Boolean>()
        fun connectInput(input: String) {
            inputs[input] = false
        }

        override fun process(signal: Signal): List<Signal> {
            inputs[signal.from] = signal.high
            val out = inputs.values.contains(false)
            return next.map { Signal(signal.to, it, out) }
        }
    }

    data class Broadcast(override val next: List<String>) : Module(next) {
        override fun process(signal: Signal) = next.map { Signal(signal.to, it, signal.high) }
    }

    private fun Map<String, Module>.pushButton() = sequence {
        val toProcess = ArrayDeque<Signal>()
        toProcess.addLast(Signal("button", "broadcaster", false))
        while (toProcess.isNotEmpty()) {
            val signal = toProcess.removeFirst()
            yield(signal)
            this@pushButton[signal.to]?.process(signal)?.let(toProcess::addAll)
        }
    }

    override fun parseInput(stringInput: List<String>) = buildMap<String, Module> {
        stringInput.forEach { line ->
            val (name, nextStr) = line.split(" -> ")
            val next = nextStr.split(", ")
            if (name == "broadcaster") {
                this[name] = Broadcast(next)
            } else if (name[0] == '%') {
                this[name.substring(1)] = FlipFlop(next)
            } else if (name[0] == '&') {
                this[name.substring(1)] = Conjunction(next)
            }
        }
        entries.forEach { (name, module) ->
            module.next.forEach { nextName ->
                this[nextName]?.also {
                    if (it is Conjunction) {
                        it.connectInput(name)
                    }
                }
            }
        }
    }

    override fun part1(input: Map<String, Module>, testArg: Any?): Int {
        var lo = 0
        var hi = 0
        repeat(1000) { input.pushButton().forEach { if (it.high) hi++ else lo++ } }
        return lo * hi
    }


    override fun part2(input: Map<String, Module>, testArg: Any?): Long {
        var pushes = 0
        val (rxFeederName, rxFeederModule) = input.entries.first { it.value.next.contains("rx") }
        if (rxFeederModule !is Conjunction) error("Unsupported input")
        val inputPeriods = mutableMapOf<String, Int>()
        while (inputPeriods.size != rxFeederModule.inputs.size) {
            pushes++
            input.pushButton().forEach {
                if (it.to == rxFeederName && inputPeriods[it.from] == null && it.high) {
                    inputPeriods[it.from] = pushes
                }
            }
        }
        return inputPeriods.values.map { it.toLong() }.reduce { a, b -> a * b / gcd(a, b) }
    }

    override fun testCases1() = listOf(
        TestCase(getInput("test1"), 32000000),
        TestCase(getInput("test2"), 11687500),
    )

    override fun testCases2() = emptyList<TestCase<Map<String, Module>, Long>>()
}

fun main() {
    Day20().main()
}
