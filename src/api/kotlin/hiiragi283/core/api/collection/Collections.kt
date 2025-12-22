package hiiragi283.core.api.collection

import net.minecraft.util.RandomSource
import kotlin.NoSuchElementException

/**
 * この[コレクション][this]から，[乱数][random]に基づいてランダムな要素を返します。
 * @see kotlin.collections.random
 * @throws NoSuchElementException コレクションが空の場合
 */
fun <T> Collection<T>.random(random: RandomSource): T {
    if (isEmpty()) throw NoSuchElementException("Collection is empty.")
    return this.elementAt(random.nextInt(this.size))
}

/**
 * この[コレクション][this]から，[乱数][randomOrNull]に基づいてランダムな要素を返します。
 * @see kotlin.collections.random
 * @return コレクションが空の場合は`null`
 */
fun <T> Collection<T>.randomOrNull(random: RandomSource): T? {
    if (isEmpty()) return null
    return this.elementAt(random.nextInt(this.size))
}
