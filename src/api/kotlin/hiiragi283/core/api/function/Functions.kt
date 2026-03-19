package hiiragi283.core.api.function

/**
 * 指定された引数からハッシュ値を生成します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun generateHash(vararg obj: Any?): Int = arrayOf(*obj).fold(0) { result: Int, obj: Any? -> 31 * result + (obj?.hashCode() ?: 0) }

/**
 * 恒等操作を行うブロックを返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <T> identity(): (T) -> T = { it }

/**
 * @since 0.12.0
 */
fun <T> identityLeft(): (T, T) -> T = { left: T, _: T -> left }

/**
 * @since 0.12.0
 */
fun <T> identityRight(): (T, T) -> T = { _: T, right: T -> right }
