@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
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

/**
 * この[ブロック][this]を[HTHolderLike]に変換します。
 * @param BLOCK [Block]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
fun <BLOCK : Block> BLOCK.toHolderLike(): HTHolderLike<Block, BLOCK> =
    object : HTHolderLike<Block, BLOCK>, HTKeyLike.HolderDelegate<Block> {
        override fun get(): BLOCK = this@toHolderLike

        override fun getHolder(): Holder<Block> = get().builtInRegistryHolder()
    }

/**
 * この[液体][this]を[HTHolderLike]に変換します。
 * @param FLUID [Fluid]を継承したクラス
 * @author Hiiragi Tsubasa
 * @since 0.3.0
 */
fun <FLUID : Fluid> FLUID.toHolderLike(): HTHolderLike<Fluid, FLUID> =
    object : HTHolderLike<Fluid, FLUID>, HTKeyLike.HolderDelegate<Fluid> {
        override fun get(): FLUID = this@toHolderLike

        override fun getHolder(): Holder<Fluid> = get().builtInRegistryHolder()
    }
