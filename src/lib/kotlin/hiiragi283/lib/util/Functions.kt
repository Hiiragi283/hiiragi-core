package hiiragi283.lib.util

/**
 * @see java.util.function.UnaryOperator
 */
typealias Identity<T> = (T) -> T

/**
 * 恒等操作を行うブロックを返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T> identity(): Identity<T> = { it }
