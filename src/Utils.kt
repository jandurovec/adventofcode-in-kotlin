import java.io.File
import java.io.FileNotFoundException
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.MessageDigest
import java.util.stream.Collectors
import kotlin.math.absoluteValue

/**
 * Returns Manhattan distance of two points in 2D
 */
fun manhattanDist(x1: Int, y1: Int, x2: Int, y2: Int) = (x1 - x2).absoluteValue + (y1 - y2).absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

fun readInput(year: Int, day: Int, classifier: String = ""): List<String> {
    val mainInput = classifier == ""
    val suffix = if (mainInput) classifier else "_$classifier"
    val path = "src/aoc$year/Day${day.toString().padStart(2, '0')}$suffix.txt"
    val inputFile = File(path)
    if (!inputFile.exists()) {
        if (mainInput) {
            val sessionFile = File("session.txt")
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
fun IntRange.overlaps(other: IntRange) = this.last >= other.first && this.first <= other.last
fun Iterable<IntRange>.subtractRange(range: IntRange) = this.flatMap {
    if (it.overlaps(range))
        listOf(it.first until range.first).let { res ->
            if (range.last < Int.MAX_VALUE) res.plusElement(range.last + 1..it.last) else res
        }
    else
        listOf(it)
}.filter { !it.isEmpty() }

fun Iterable<IntRange>.union(): List<IntRange> = this.sortedBy { it.first }
    .fold(mutableListOf()) { acc, current ->
        val previous = acc.lastOrNull()
        if (previous != null && current.first <= previous.last) {
            acc[acc.lastIndex] = previous.first..maxOf(current.last, previous.last)
        } else {
            acc += current
        }
        acc
    }

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
