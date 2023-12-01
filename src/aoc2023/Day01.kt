package aoc2023

import TestCase
import UnparsedDay

class Day01 : UnparsedDay<Int, Int>(2023, 1) {

    private fun calibration(input: List<List<Char>>) = input.sumOf {
        buildString {
            append(it.first())
            append(it.last())
        }.toInt()
    }

    override fun part1(input: List<String>, testArg: Any?) = calibration(input.map { l ->
        l.toList().filter { it.isDigit() }
    })

    override fun part2(input: List<String>, testArg: Any?): Int {
        val digits = mapOf(
            "one" to '1',
            "two" to '2',
            "three" to '3',
            "four" to '4',
            "five" to '5',
            "six" to '6',
            "seven" to '7',
            "eight" to '8',
            "nine" to '9'
        )
        return calibration(input.map { s ->
            s.indices.mapNotNull { i ->
                if (s[i].isDigit()) {
                    s[i]
                } else {
                    digits.toList().filter { (k, _) -> s.substring(i).startsWith(k) }.map { it.second }.firstOrNull()
                }
            }
        })
    }


    override fun testCases1() = listOf(
        TestCase(
            listOf(
                "1abc2",
                "pqr3stu8vwx",
                "a1b2c3d4e5f",
                "treb7uchet"
            ), 142
        ),
    )

    override fun testCases2() = listOf(
        TestCase(
            listOf(
                "two1nine",
                "eightwothree",
                "abcone2threexyz",
                "xtwone3four",
                "4nineeightseven2",
                "zoneight234",
                "7pqrstsixteen",
            ), 281
        ),
    )

}

fun main() {
    Day01().main()
}
