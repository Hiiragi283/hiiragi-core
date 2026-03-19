package hiiragi283.core.api.collection

import net.minecraft.util.RandomSource
import kotlin.NoSuchElementException

/**
 * この[コレクション][this]から，[乱数][random]に基づいてランダムな要素を返します。
 * @throws NoSuchElementException コレクションが空の場合
 * @see kotlin.collections.random
 */
fun <T> Collection<T>.random(random: RandomSource): T {
    if (isEmpty()) throw NoSuchElementException("Collection is empty.")
    return this.elementAt(random.nextInt(this.size))
}

/**
 * この[コレクション][this]から，[乱数][randomOrNull]に基づいてランダムな要素を返します。
 * @return コレクションが空の場合は`null`
 * @see kotlin.collections.random
 */
fun <T> Collection<T>.randomOrNull(random: RandomSource): T? {
    if (isEmpty()) return null
    return this.elementAt(random.nextInt(this.size))
}

/**
 * この[コレクション][this]が空か判定します。
 * @param predicate 要素が空か判定するブロック
 * @return 空の場合は`true`
 * @since 0.9.0
 */
inline fun <T> Collection<T>.isEmpty(predicate: (T) -> Boolean): Boolean = this.isEmpty() || this.all(predicate)
