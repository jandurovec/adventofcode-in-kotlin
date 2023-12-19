import java.io.FileNotFoundException
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.MessageDigest
import java.util.stream.Collectors
import kotlin.io.path.*
import kotlin.math.absoluteValue

/**
 * Returns Manhattan distance of two points in 2D
 */
fun manhattanDist(x1: Int, y1: Int, x2: Int, y2: Int) = (x1 - x2).absoluteValue + (y1 - y2).absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

fun readInput(year: Int, day: Int, classifier: String = ""): List<String> {
    val mainInput = classifier == ""
    val suffix = if (mainInput) classifier else "_$classifier"
    val path = "src/aoc$year/Day${day.toString().padStart(2, '0')}$suffix.txt"
    val inputFile = Path(path)
    if (!inputFile.exists()) {
        if (mainInput) {
            val sessionFile = Path("session.txt")
            if (!sessionFile.exists()) {
                throw FileNotFoundException("Day input file not found and session.txt not present.")
            }
            val session = sessionFile.readText()
            val client = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .setHeader("cookie", "session=$session")
                .uri(URI.create("https://adventofcode.com/$year/day/$day/input"))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofLines())
            val data = response.body().collect(Collectors.joining(System.lineSeparator()))
            if (response.statusCode() == HttpURLConnection.HTTP_OK) {
                inputFile.writeText(data)
            } else {
                throw FileNotFoundException("Day input file not found and unable to retrieve the response from AOC website: (${response.statusCode()}) $data")
            }
        } else {
            throw FileNotFoundException("$path (The system cannot find the file specified)")
        }
    }
    return inputFile.readLines()
}

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16).padStart(32, '0')

/**
 * Converts string to list of ints
 */
fun String.toIntList(delimiter: Char = ',') = split(delimiter).map { it.toInt() }

/**
 * Checks if string is lowercase
 */
fun String.isLowerCase() = all(Char::isLowerCase)

fun IntProgression.size(): Int = if (this.isEmpty()) 0 else 1 + (last - first) / step
fun <T : Comparable<T>> ClosedRange<T>.overlaps(other: ClosedRange<T>) =
    this.endInclusive >= other.start && this.start <= other.endInclusive

private fun <T : Comparable<T>, S : ClosedRange<T>> Iterable<S>.subtractRange(
    range: S,
    openEndRange: (T, T) -> S,
    closedRange: (T, T) -> S,
    increment: (T, Int) -> T,
    maxVal: T
) = this.flatMap {
    if (it.overlaps(range))
        listOf(openEndRange(it.start, range.start)).let { res ->
            if (range.endInclusive < maxVal)
                res.plusElement(closedRange(increment(range.endInclusive, 1), it.endInclusive))
            else res
        }
    else
        listOf(it)
}.filter { !it.isEmpty() }

fun Iterable<LongRange>.subtractRange(range: LongRange) =
    this.subtractRange(range, Long::until, Long::rangeTo, Long::plus, Long.MAX_VALUE)

fun Iterable<IntRange>.subtractRange(range: IntRange) =
    this.subtractRange(range, Int::until, Int::rangeTo, Int::plus, Int.MAX_VALUE)

private fun <T : Comparable<T>, S : ClosedRange<T>> Iterable<S>.union(closedRange: (T, T) -> S): List<S> = this.sortedBy { it.start }
    .fold(mutableListOf()) { acc, current ->
        val previous = acc.lastOrNull()
        if (previous != null && current.start <= previous.endInclusive) {
            acc[acc.lastIndex] = closedRange(previous.start, maxOf(current.endInclusive, previous.endInclusive))
        } else {
            acc += current
        }
        acc
    }

@JvmName("unionIntRange")
fun Iterable<IntRange>.union() = this.union(Int::rangeTo)

@JvmName("unionLongRange")
fun Iterable<LongRange>.union() = this.union(Long::rangeTo)

tailrec fun gcd(a: Long, b: Long): Long {
    return if (a == 0L) {
        b
    } else if (b == 0L) {
        a
    } else {
        val h = maxOf(a.absoluteValue, b.absoluteValue)
        val l = minOf(a.absoluteValue, b.absoluteValue)
        gcd(l, h.mod(l))
    }
}

fun <T> Set<T>.permutations(): Sequence<List<T>> = sequence {
    if (isEmpty()) {
        yield(emptyList())
    } else {
        this@permutations.forEach { item ->
            yieldAll((this@permutations - item).permutations().map { listOf(item) + it })
        }
    }
}

fun <T> Iterable<T>.split(condition: (T) -> Boolean) :List<List<T>> = buildList {
    var cur = mutableListOf<T>().also { add(it) }
    this@split.forEach { element ->
        if (condition.invoke(element)) {
            cur = mutableListOf<T>().also { add(it) }
        } else {
            cur.add(element)
        }
    }
}
fun Iterable<String>.split() = split { it.isBlank() }
