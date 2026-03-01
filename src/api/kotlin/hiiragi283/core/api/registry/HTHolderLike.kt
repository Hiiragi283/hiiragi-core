package hiiragi283.core.api.registry

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import java.util.function.Supplier

/**
 * [ResourceKey]と値を保持する[HTKeyLike]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTHolderLike<R : Any, T : R> :
    HTKeyLike<R>,
    Supplier<T> {
    /**
     * [Holder]を保持する[HolderDelegate]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.6.0
     * @see HTDeferredHolder
     * @see HTFluidContent
     */
    interface HolderDelegate<R : Any, T : R> :
        HTHolderLike<R, T>,
        HTKeyLike.HolderDelegate<R>
}

//    Extensions    //

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

        override fun getHolder(): Holder<R> = this@toLike.delegate
    }

//    Block    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
typealias HTBlockHolderLike<BLOCK> = HTHolderLike<Block, BLOCK>

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
@Suppress("DEPRECATION")
fun Block.toLike(): HTBlockHolderLike<Block> = this.builtInRegistryHolder().toLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTBlockHolderLike<*>.getDefaultState(): BlockState = this.get().defaultBlockState()

//    Fluid    //

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
typealias HTFluidHolderLike<FLUID> = HTHolderLike<Fluid, FLUID>

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
@Suppress("DEPRECATION")
fun Fluid.toLike(): HTFluidHolderLike<Fluid> = this.builtInRegistryHolder().toLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.getBucket(): Item = this.get().bucket

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.getBucketHolder(): HTItemHolderLike<*> = HTItemHolderLike.of(this.getBucket())

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.getFluidType(): FluidType = this.get().fluidType

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.toStack(amount: Int): FluidStack = FluidStack(this.get(), amount)

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.toResource(): HTFluidResourceType? = this.get().toResource()

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.toResource(patch: DataComponentPatch): HTFluidResourceType? = this.get().toResource(patch)
