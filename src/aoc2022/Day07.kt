package aoc2022

import readInput
import java.util.function.Predicate

fun main() {
    class FsNode {
        var size = 0
            private set
        var parent: FsNode? = null
            private set
        val children = mutableMapOf<String, FsNode>()

        fun addDir(name: String) = children.computeIfAbsent(name) { _ ->
            val child = FsNode()
            child.parent = this
            child
        }

        fun addFile(size: Int) {
            var cur: FsNode? = this
            while (cur != null) {
                cur.size += size
                cur = cur.parent
            }
        }

        fun find(predicate: Predicate<FsNode>): Sequence<FsNode> = sequence {
            val toExplore = mutableListOf(this@FsNode)
            while (toExplore.isNotEmpty()) {
                val cur = toExplore.removeFirst()
                if (predicate.test(cur)) {
                    yield(cur)
                }
                toExplore.addAll(cur.children.values)
            }
        }
    }

    fun parseInput(classifier: String = ""): FsNode = readInput(2022, 7, classifier).let { input ->
        var curNode = FsNode()
        val root = curNode
        for (cmd in input) {
            when {
                cmd.startsWith("$ cd") -> {
                    val dirName = cmd.substring(5)
                    curNode = when (dirName) {
                        ".." -> curNode.parent!!
                        "/" -> root
                        else -> curNode.addDir(dirName)
                    }
                }

                cmd.matches("\\d+ .*".toRegex()) -> {
                    val parts = cmd.split(" ")
                    val size = parts[0].toIntOrNull()
                    if (size != null) {
                        curNode.addFile(size)
                    }
                }
            }
        }
        return root
    }

    fun part1(root: FsNode) =
        root.find { it.size <= 100000 }.sumOf { it.size }

    fun part2(root: FsNode): Int {
        val toFree = 30000000 - (70000000 - root.size)
        return root.find { it.size >= toFree }.minOf { it.size }
    }

    val testInput = parseInput("test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = parseInput()
    println(part1(input))
    println(part2(input))
}
