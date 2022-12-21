class Rational(val n: Long, val d: Long = 1) {

    operator fun plus(other: Rational) = Rational(n * other.d + other.n * d, d * other.d).simplify()
    operator fun unaryMinus() = Rational(-n, d)
    operator fun minus(other: Rational) = this + (-other)
    operator fun times(other: Rational): Rational {
        val gcd1 = gcd(n, other.d)
        val gcd2 = gcd(other.n, d)
        val newN = (n / gcd1) * (other.n / gcd2)
        val newD = (d / gcd2) * (other.d / gcd1)
        return if (newD >= 0) Rational(newN, newD) else Rational(-newN, -newD)
    }

    operator fun div(other: Rational) = this * Rational(other.d, other.n)

    private fun simplify(): Rational {
        val gcd = gcd(n, d)
        return Rational(n / gcd, d / gcd)
    }

    override fun toString() = if (d == 1L) n.toString() else "$n/$d"
}
