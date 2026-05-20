package hiiragi283.core.api.function

/**
 * @see java.util.function.UnaryOperator
 */
typealias Identity<T> = (T) -> T

/**
 * 恒等操作を行うブロックを返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T> identity(): (T) -> T = { it }
