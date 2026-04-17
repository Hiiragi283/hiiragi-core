package hiiragi283.core.api

import org.apache.commons.lang3.math.Fraction

fun fraction(value: Double): Fraction = Fraction.getFraction(value)

fun Double.toFraction(): Fraction = fraction(this)

operator fun Fraction.plus(other: Fraction): Fraction = this.add(other)

operator fun Fraction.minus(other: Fraction): Fraction = this.subtract(other)

operator fun Fraction.times(other: Fraction): Fraction = this.multiplyBy(other)

operator fun Fraction.div(other: Fraction): Fraction = this.divideBy(other)

/**
 * @since 0.8.0
 */
fun Fraction.split(): Pair<Int, Fraction> {
    val whole: Int = this.properWhole
    return whole to (this - whole)
}

/**
 * @since 0.8.0
 */
operator fun Fraction.component1(): Int = this.numerator

/**
 * @since 0.8.0
 */
operator fun Fraction.component2(): Int = this.denominator

// Int
fun fraction(numerator: Int, denominator: Int): Fraction = Fraction.getFraction(numerator, denominator)

/**
 * 指定した引数から[分数][Fraction]の新しいインスタンスを作成します。
 * @param amount 分子となる値
 * @param capacity 分母となる値
 * @param loop `true`の場合は[amount]を[capacity]で割った剰余を，`false`の場合は[capacity]以下の値を分子に使用します
 */
fun fixedFraction(amount: Int, capacity: Int, loop: Boolean = false): Fraction {
    if (capacity <= 0) return Fraction.ZERO
    val fixedAmount: Int = when (loop) {
        true -> amount % capacity
        false -> minOf(amount, capacity)
    }
    return when (fixedAmount) {
        capacity -> Fraction.ONE
        else -> fraction(fixedAmount, capacity)
    }
}

fun fraction(numerator: Int): Fraction = fraction(numerator, 1)

fun Int.toFraction(): Fraction = this.toFraction(1)

fun Int.toFraction(denominator: Int): Fraction = fraction(this, denominator)

operator fun Fraction.plus(other: Int): Fraction = this.add(other.toFraction())

operator fun Fraction.minus(other: Int): Fraction = this.subtract(other.toFraction())

operator fun Fraction.times(other: Int): Fraction = this.multiplyBy(other.toFraction())

operator fun Fraction.div(other: Int): Fraction = this.divideBy(other.toFraction())

operator fun Fraction.compareTo(other: Int): Int = this.compareTo(other.toFraction())

operator fun Int.plus(other: Fraction): Fraction = this.toFraction().add(other)

operator fun Int.minus(other: Fraction): Fraction = this.toFraction().subtract(other)

operator fun Int.times(other: Fraction): Fraction = this.toFraction().multiplyBy(other)

operator fun Int.div(other: Fraction): Fraction = this.toFraction().divideBy(other)

operator fun Int.compareTo(other: Fraction): Int = this.toFraction().compareTo(other)

// Long
fun fixedFraction(amount: Long, capacity: Long): Fraction {
    if (capacity <= 0) return Fraction.ZERO
    val fixedAmount: Long = minOf(amount, capacity)
    return fraction(fixedAmount / capacity.toDouble())
}

// Float
fun fraction(value: Float): Fraction = fraction(value.toDouble())

fun Float.toFraction(): Fraction = fraction(this)

operator fun Fraction.plus(other: Float): Fraction = this.add(other.toFraction())

operator fun Fraction.minus(other: Float): Fraction = this.subtract(other.toFraction())

operator fun Fraction.times(other: Float): Fraction = this.multiplyBy(other.toFraction())

operator fun Fraction.div(other: Float): Fraction = this.divideBy(other.toFraction())

operator fun Fraction.compareTo(other: Float): Int = this.compareTo(other.toFraction())

operator fun Float.plus(other: Fraction): Fraction = this.toFraction().add(other)

operator fun Float.minus(other: Fraction): Fraction = this.toFraction().subtract(other)

operator fun Float.times(other: Fraction): Fraction = this.toFraction().multiplyBy(other)

operator fun Float.div(other: Fraction): Fraction = this.toFraction().divideBy(other)

operator fun Float.compareTo(other: Fraction): Int = this.toFraction().compareTo(other)
