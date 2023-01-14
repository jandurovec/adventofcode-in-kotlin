data class Point(val x: Int, val y: Int) {
    fun neighbors() = (-1..1).flatMap { x ->
        (-1..1).mapNotNull { y ->
            if (x != 0 || y != 0) x to y else null
        }
    }.map { (dx, dy) -> Point(this.x + dx, this.y + dy) }
}

typealias Grid = LinkedHashSet<Point>
