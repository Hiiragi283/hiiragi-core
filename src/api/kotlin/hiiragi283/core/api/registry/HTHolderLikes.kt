@file:Suppress("UNCHECKED_CAST")

package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <R : Any> Holder<R>.toLike(): HTHolderLike<R, R> = (this as? HTHolderLike<R, R>) ?: object : HTHolderLike<R, R> {
    override fun getResourceKey(): ResourceKey<R> = this@toLike.unwrapKey().orElseThrow()

    override fun get(): R = this@toLike.value()
}

/**
 * この[DeferredHolder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @param T [R]を継承した値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTHolderLike<R, T> = (this as? HTHolderLike<R, T>) ?: object : HTHolderLike<R, T> {
    override fun getResourceKey(): ResourceKey<R> = this@toLike.key!!

    override fun get(): T = this@toLike.get()
}
