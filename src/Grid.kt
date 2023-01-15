data class Point(val x: Int, val y: Int) {
    fun neighbors() = (-1..1).flatMap { x ->
        (-1..1).mapNotNull { y ->
            if (x != 0 || y != 0) x to y else null
        }
    }.map { (dx, dy) -> Point(this.x + dx, this.y + dy) }

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun times(multiplier: Int) = Point(multiplier * x, multiplier * y)
}

operator fun Int.times(point: Point) = point * this

typealias Grid<T> = MutableMap<Point, T>

fun <T> Grid(): Grid<T> = mutableMapOf()
fun <T> Grid(other: Map<Point, T>): Grid<T> = other.toMutableMap()
