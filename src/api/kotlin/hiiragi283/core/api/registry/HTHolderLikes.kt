@file:Suppress("UNCHECKED_CAST")

package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.DeferredHolder

fun <R : Any> Holder<R>.toLike(): HTHolderLike<R, R> = (this as? HTHolderLike<R, R>) ?: object : HTHolderLike<R, R> {
    override fun getResourceKey(): ResourceKey<R> = this@toLike.unwrapKey().orElseThrow()

    override fun get(): R = this@toLike.value()
}

fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTHolderLike<R, T> = (this as? HTHolderLike<R, T>) ?: object : HTHolderLike<R, T> {
    override fun getResourceKey(): ResourceKey<R> = this@toLike.key!!

    override fun get(): T = this@toLike.get()
}
