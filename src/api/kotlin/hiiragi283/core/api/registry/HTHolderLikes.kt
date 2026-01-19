package hiiragi283.core.api.registry

import net.minecraft.core.Holder

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <R : Any> Holder<R>.toLike(): HTHolderLike.HolderDelegate<R, R> =
    (this as? HTHolderLike.HolderDelegate<R, R>) ?: object : HTHolderLike.HolderDelegate<R, R> {
        override fun get(): R = this@toLike.value()

        override fun getHolder(): Holder<R> = this@toLike
    }
