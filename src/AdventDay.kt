import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class TestCase<I, E>(val input: I, val expected: E, val testArg: Any? = null)

@OptIn(ExperimentalTime::class)
abstract class AdventDay<I, R1 : Any, R2 : Any>(private val year: Int, private val day: Int) {

    protected abstract fun parseInput(stringInput: List<String>): I
    protected open fun readInput(classifier: String = "") = readInput(year, day, classifier)
    protected fun getInput(classifier: String = "") = parseInput(readInput(classifier))
    protected fun getTestInput(classifier: String = "test") = getInput(classifier)

    protected open fun testCases1(): List<TestCase<I, R1>> = emptyList()
    protected open fun testCases2(): List<TestCase<I, R2>> = emptyList()

    open fun part1(input: I, testArg: Any? = null): R1 = TODO("Not yet implemented")
    open fun part2(input: I, testArg: Any? = null): R2 = TODO("Not yet implemented")

    fun main() {
        val header = "AoC $year day $day"
        val separator = "-".repeat(header.length)
        println(header)
        println(separator)
        val testTime = measureTime {
            testCases1().forEach {
                assertEquals(it.expected, part1(it.input, it.testArg), "${it.input}")
            }
            testCases2().forEach {
                assertEquals(it.expected, part2(it.input, it.testArg), "${it.input}")
            }
        }
        val part1Time = printAndMeasure(::part1)
        val part2Time = printAndMeasure(::part2)
        println()
        println("Tests: $testTime")
        println("Part1: $part1Time")
        println("Part2: $part2Time")
    }

    private fun printAndMeasure(part: (I, Any?) -> Any): Duration {
        val input = getInput()
        return measureTime {
            part.invoke(input, null).let {
                if (it !is Unit) {
                    println(it)
                }
            }
        }
    }
}

open class UnparsedDay<R1 : Any, R2 : Any>(year: Int, day: Int) : AdventDay<List<String>, R1, R2>(year, day) {
    override fun parseInput(stringInput: List<String>) = stringInput
}

open class SingleStringDay<R1 : Any, R2 : Any>(year: Int, day: Int) : AdventDay<String, R1, R2>(year, day) {
    override fun parseInput(stringInput: List<String>) = stringInput.first()
}
