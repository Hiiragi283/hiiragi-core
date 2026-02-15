package hiiragi283.core.api.registry

import net.minecraft.core.Holder

typealias HTSimpleHolderLike<R> = HTHolderLike<R, R>

typealias HTSimpleHolderLikeDelegate<R> = HTHolderLike.HolderDelegate<R, R>

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <R : Any> Holder<R>.toLike(): HTSimpleHolderLikeDelegate<R> =
    (this as? HTSimpleHolderLikeDelegate<R>) ?: object : HTSimpleHolderLikeDelegate<R> {
        override fun get(): R = this@toLike.value()

        override fun getHolder(): Holder<R> = this@toLike
    }
