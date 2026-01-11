@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredHolder

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <R : Any> Holder<R>.toLike(): HTHolderLike.HolderDelegate<R, R> =
    (this as? HTHolderLike.HolderDelegate<R, R>) ?: object : HTHolderLike.HolderDelegate<R, R> {
        override fun get(): R = this@toLike.value()

        override fun getHolder(): Holder<R> = this@toLike

        override fun getResourceKey(): ResourceKey<R> = this@toLike.unwrapKey().orElseThrow()
    }

/**
 * この[DeferredHolder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @param T [R]を継承した値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTHolderLike.HolderDelegate<R, T> =
    (this as? HTHolderLike.HolderDelegate<R, T>) ?: object : HTHolderLike.HolderDelegate<R, T> {
        override fun get(): T = this@toLike.get()

        override fun getHolder(): Holder<R> = this@toLike.delegate

        override fun getResourceKey(): ResourceKey<R> = this@toLike.key!!
    }

/**
 * この[ブロック][this]を[HTHolderLike]に変換します。
 * @param BLOCK [Block]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
fun <BLOCK : Block> BLOCK.toHolderLike(): HTHolderLike.HolderDelegate<Block, BLOCK> = object : HTHolderLike.HolderDelegate<Block, BLOCK> {
    override fun get(): BLOCK = this@toHolderLike

    override fun getHolder(): Holder<Block> = get().builtInRegistryHolder()
}

/**
 * この[液体][this]を[HTHolderLike]に変換します。
 * @param FLUID [Fluid]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
fun <FLUID : Fluid> FLUID.toHolderLike(): HTHolderLike.HolderDelegate<Fluid, FLUID> = object : HTHolderLike.HolderDelegate<Fluid, FLUID> {
    override fun get(): FLUID = this@toHolderLike

    override fun getHolder(): Holder<Fluid> = get().builtInRegistryHolder()
}
