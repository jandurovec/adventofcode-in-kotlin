package aoc2021

import readInput

fun main() {
    data class Node(
        var left: Node? = null,
        var right: Node? = null,
        var value: Int? = null,
        var depth: Int = 0,
        var parent: Node? = null,
        var isLeft: Boolean = true
    ) {
        init {
            listOf(left, right).forEach { n ->
                n?.let {
                    it.parent = this
                    it.increaseDepth()
                }
            }
            right?.let { it.isLeft = false }
        }

        override fun toString() = if (value != null) {
            value.toString()
        } else {
            "[" + left.toString() + "," + right.toString() + "]"
        }

        private fun increaseDepth() {
            depth++
            left?.increaseDepth()
            right?.increaseDepth()
        }

        fun reduce(): Node {
            while (true) {
                if (explode()) continue
                if (split()) continue
                break
            }
            return this
        }

        fun values(): Sequence<Node> = sequence {
            if (value != null) {
                yield(this@Node)
            } else {
                yieldAll(left!!.values())
                yieldAll(right!!.values())
            }
        }

        fun explode(): Boolean {
            // left number from exploding pair
            val toExplodeLeft = values().firstOrNull { it.depth == 5 }
            if (toExplodeLeft != null) {
                val toExplode = toExplodeLeft.parent!!
                var biggestSmaller: Node? = toExplode
                while (biggestSmaller != null && biggestSmaller.isLeft) {
                    biggestSmaller = biggestSmaller.parent
                }
                if (biggestSmaller != null) {
                    biggestSmaller = biggestSmaller.parent!!.left
                    while (biggestSmaller!!.value == null) {
                        biggestSmaller = biggestSmaller.right
                    }
                    biggestSmaller.value = biggestSmaller.value?.plus(toExplodeLeft.value!!)
                }

                var smallestBigger: Node? = toExplode
                while (smallestBigger != null && (!smallestBigger.isLeft || smallestBigger.parent == null)) {
                    smallestBigger = smallestBigger.parent
                }
                if (smallestBigger != null) {
                    smallestBigger = smallestBigger.parent!!.right
                    while (smallestBigger!!.value == null) {
                        smallestBigger = smallestBigger.left
                    }
                    smallestBigger.value = smallestBigger.value?.plus(toExplode.right!!.value!!)
                }
                toExplode.left = null
                toExplode.right = null
                toExplode.value = 0
                return true
            } else {
                return false
            }
        }

        fun split(): Boolean {
            val toSplit = values().firstOrNull { it.value != null && it.value!! > 9 }
            return if (toSplit != null) {
                toSplit.left = Node(value = toSplit.value!! / 2, depth = toSplit.depth + 1, parent = toSplit)
                toSplit.right = Node(
                    value = (toSplit.value!! + 1) / 2,
                    depth = toSplit.depth + 1,
                    parent = toSplit,
                    isLeft = false
                )
                toSplit.value = null
                true
            } else {
                false
            }
        }

        fun plus(another: Node) = Node(this, another).reduce()

        fun magnitude(): Int = if (value != null) {
            value!!
        } else {
            3 * left!!.magnitude() + 2 * right!!.magnitude()
        }
    }

    fun parseNode(it: Iterator<String>): Node {
        val cur = it.next()
        return if (cur == "[") {
            val left = parseNode(it)
            it.next() //consume ,
            val right = parseNode(it)
            it.next() // consume ]
            Node(left, right)
        } else {
            Node(value = cur.toInt())
        }
    }

    // split input on right boundaries of [ or , or left boundaries of ] or ,
    fun parseNum(s: String) = s.split("""(?<=[,\[])|(?=[,\]])""".toRegex()).iterator().let { parseNode(it) }

    fun part1(numbers: List<String>) = numbers.map { parseNum(it) }.reduce { a, b -> a.plus(b) }.magnitude()

    fun part2(numbers: List<String>): Int {
        val arr = arrayListOf<String>()
        arr.addAll(numbers)
        return (0 until arr.lastIndex).maxOf { first ->
            (first + 1..arr.lastIndex).maxOf { second ->
                maxOf(
                    parseNum(arr[first]).plus(parseNum(arr[second])).magnitude(),
                    parseNum(arr[second]).plus(parseNum(arr[first])).magnitude()
                )
            }
        }
    }

    // check explosion
    check(parseNum("[[[[[9,8],1],2],3],4]").reduce().toString() == "[[[[0,9],2],3],4]")
    check(parseNum("[7,[6,[5,[4,[3,2]]]]]").reduce().toString() == "[7,[6,[5,[7,0]]]]")

    //check magnitude
    check(parseNum("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude() == 3488)

    val testInput = readInput("aoc2021/Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("aoc2021/Day18")
    println(part1(input))
    println(part2(input))
}
