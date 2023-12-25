class Primes : Sequence<Int> {
    companion object {
        private val known = mutableSetOf(2, 3)
        private fun nextPrimes() = generateSequence(known.last()) { it + 2 }.filter { known.all { p -> it % p != 0 } }
            .map { it.also { known.add(it) } }

        private fun generateSequence() = known.asSequence() + nextPrimes()
    }

    override fun iterator() = generateSequence().iterator()
}

fun Int.factorize() = this.toLong().factorize().mapKeys { (k, _) -> k.toInt() }

fun Long.factorize(): Map<Long, Int> {
    val primes = Primes().iterator()
    var p = primes.next().toLong()
    var rem = this
    val result = mutableMapOf<Long, Int>()
    while (p * p <= rem) {
        var exponent = 0
        while (rem % p == 0L) {
            rem /= p
            exponent++
        }
        if (exponent > 0) {
            result[p] = exponent
        }
        if (p * p <= rem) {
            p = primes.next().toLong()
        }
    }
    if (rem > 1) {
        result[rem] = 1
    }
    return result
}
