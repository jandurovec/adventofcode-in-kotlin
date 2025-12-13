package aoc2025

import AdventDay
import Point
import split


class Day12 : AdventDay<Day12.Input, Int, String>(2025, 12) {

    companion object {
        fun Set<Point>.transpose() = buildSet {
            this@transpose.forEach { (x, y) ->
                add(Point(y, x))
            }
        }

        fun Set<Point>.rotate() = buildSet {
            val maxY = this@rotate.maxOf { it.y }
            this@rotate.forEach { (x, y) ->
                add(Point(maxY - y, x))
            }
        }

        fun Set<Point>.normalize(): Set<Point> {
            val minX = minOf { it.x }
            val minY = minOf { it.y }
            return if (minX == 0 && minY == 0) {
                this
            } else {
                map { Point(it.x - minX, it.y - minY) }.toSet()
            }
        }
    }

    class Shape(points: Set<Point>) {
        val pointCount = points.size
        val variants = buildSet {
            val normalized = points.normalize()
            listOf(normalized, normalized.transpose()).forEach {
                var cur = it
                add(it)
                repeat(3) {
                    cur = cur.rotate()
                    add(cur)
                }
            }
        }
        val size = variants.maxOf { v -> v.maxOf { maxOf(it.x, it.y) } } + 1
    }

    data class Requirements(val w: Int, val h: Int, val shapeCount: Map<Int, Int>)

    data class Input(val shapes: List<Shape>, val requirements: List<Requirements>)

    override fun parseInput(stringInput: List<String>): Input {
        val parts = stringInput.split()

        val shapes = parts.dropLast(1).map { shape ->
            Shape(shape.drop(1).flatMapIndexed { y, line ->
                line.mapIndexedNotNull { x, c -> if (c == '#') Point(x, y) else null }
            }.toSet())
        }
        val requirements = parts.last().map { line ->
            val (head, tail) = line.split(": ")
            val (w, h) = head.split("x").map { it.toInt() }
            Requirements(
                w, h,
                tail.trim().split(" ").mapIndexed { i, v -> i to v.toInt() }.filter { it.second != 0 }.toMap()
            )
        }
        return Input(shapes, requirements)
    }

    override fun part1(input: Input, testArg: Any?) = input.requirements.count { req ->
        /*
        For the first time in AoC history, the task can (should?) be solved with an algorithm
        that does not even pass on sample input. What an anticlimax :(
         */
        val shapeSize = input.shapes.maxOf { s -> s.size }
        // what if we naively just considered square bounding boxes?
        val minShapes = (req.w / shapeSize) * (req.h / shapeSize)
        if (req.shapeCount.values.sum() <= minShapes) {
            true
        } else if (req.w * req.h < req.shapeCount.entries.sumOf { (idx, count) -> count * input.shapes[idx].pointCount }) {
            false
        } else {
            error("Bin packing algorithm implementation needed to decide for $req")
        }
    }

    override fun part2(input: Input, testArg: Any?) = ""
}

fun main() {
    Day12().main()
}
