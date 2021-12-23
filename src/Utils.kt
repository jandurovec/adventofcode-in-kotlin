import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue

/**
 * Returns Manhattan distance of two points in 2D
 */
fun manhattanDist(x1: Int, y1: Int, x2: Int, y2:Int) = (x1 - x2).absoluteValue + (y1 - y2).absoluteValue

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Reads lines from the given input txt file as numbers.
 */
fun readInputAsIntList(name: String) = readInput(name).map { it.toInt() }

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * Converts string to list of ints
 */
fun String.toIntList(delimiter: Char = ',') = split(delimiter).map { it.toInt() }

/**
 * Checks if string is lowercase
 */
fun String.isLowerCase() = all(Char::isLowerCase)
