data class Point(val x: Int, val y: Int) {
    companion object {
        val UP = Point(0,0).up()
        val DOWN = Point(0,0).down()
        val LEFT = Point(0,0).left()
        val RIGHT = Point(0,0).right()
    }
    fun neighbors(includeDiagonal: Boolean = true) = (-1..1).flatMap { x ->
        (-1..1).mapNotNull { y ->
            if (x != 0 || y != 0) x to y else null
        }
    }.filter { (dx, dy) -> includeDiagonal || dx == 0 || dy == 0 }
        .map { (dx, dy) -> Point(this.x + dx, this.y + dy) }

    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun unaryMinus() = Point(-x, -y)
    operator fun times(multiplier: Int) = Point(multiplier * x, multiplier * y)

    fun right() = Point(x + 1, y)
    fun left() = Point(x - 1, y)
    fun up() = Point(x, y - 1)
    fun down() = Point(x, y + 1)
}

operator fun Int.times(point: Point) = point * this

typealias Grid<T> = MutableMap<Point, T>

fun <T> Grid(): Grid<T> = mutableMapOf()
fun <T> Grid(other: Map<Point, T>): Grid<T> = other.toMutableMap()
