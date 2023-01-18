package aoc2016

import TestCase
import UnparsedDay

class Day07 : UnparsedDay<Int, Int>(2016, 7) {
    private fun String.supports(check: (CharArray, List<Boolean>) -> Boolean): Boolean {
        val chars = toCharArray()
        val hypernet =
            chars.runningFold(0) { acc, c -> if (c == '[') acc + 1 else if (c == ']') acc - 1 else acc }
                .drop(1).map { it > 0 }
        return check.invoke(chars, hypernet)
    }

    private fun String.supportsTls() = supports { chars, hypernet ->
        fun hasAbba(hypernetSelector: (Boolean) -> Boolean) =
            (0..chars.size - 4).any { pos ->
                (pos..pos + 3).all { hypernetSelector.invoke(hypernet[it]) } &&
                        chars[pos] != chars[pos + 1] && chars[pos] == chars[pos + 3] && chars[pos + 1] == chars[pos + 2]
            }
        hasAbba { !it } && !hasAbba { it }
    }

    private fun String.supportsSsl() = supports { chars, hypernet ->
        (0..chars.size - 3).any { pos ->
            (pos..pos + 2).all { !hypernet[it] } && // outside hypernet
                    chars[pos] != chars[pos + 1] && chars[pos] == chars[pos + 2] && // aba
                    (0..chars.size - 3).map { it..it + 2 }.any { rng ->
                        rng.all { hypernet[it] } && substring(rng) == "${chars[pos + 1]}${chars[pos]}${chars[pos + 1]}"
                    }
        }
    }

    override fun part1(input: List<String>, testArg: Any?) = input.count { it.supportsTls() }
    override fun part2(input: List<String>, testArg: Any?) = input.count { it.supportsSsl() }

    override fun testCases1() = listOf(
        TestCase(listOf("abba[mnop]qrst"), 1),
        TestCase(listOf("abcd[bddb]xyyx"), 0),
        TestCase(listOf("aaaa[qwer]tyui"), 0),
        TestCase(listOf("ioxxoj[asdfgh]zxcvbn"), 1)
    )

    override fun testCases2() = listOf(
        TestCase(listOf("aba[bab]xyz"), 1),
        TestCase(listOf("xyx[xyx]xyx"), 0),
        TestCase(listOf("aaa[kek]eke"), 1),
        TestCase(listOf("zazbz[bzb]cdb"), 1)
    )

}

fun main() {
    Day07().main()
}
