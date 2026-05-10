package hiiragi283.core.api.function

/**
 * 恒等操作を行うブロックを返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T> identity(): (T) -> T = { it }
