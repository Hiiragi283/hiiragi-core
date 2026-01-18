@file:Suppress("UNCHECKED_CAST", "DEPRECATION")

package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.world.level.material.Fluid

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
