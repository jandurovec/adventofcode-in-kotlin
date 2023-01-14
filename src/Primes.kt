class Primes : Sequence<Int> {
    companion object {
        private val known = mutableSetOf(2, 3)
        private fun nextPrimes() = generateSequence(known.last()) { it + 2 }.filter { known.all { p -> it % p != 0 } }
            .map { it.also { known.add(it) } }

        private fun generateSequence() = known.asSequence() + nextPrimes()
    }

    override fun iterator() = generateSequence().iterator()
}

fun Int.factorize(): Map<Int, Int> =
    Primes().takeWhile { it * it <= this }.fold(this to emptyMap<Int, Int>()) { (remaining, exp), p ->
        var exponent = 0
        var rem = remaining
        while (rem % p == 0) {
            rem /= p
            exponent++
        }
        rem to if (exponent > 0) exp + (p to exponent) else exp
    }.let { (remaining, result) ->
        if (remaining > 1) result + (remaining to 1) else result
    }
