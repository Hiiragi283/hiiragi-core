package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.util.Either
import hiiragi283.core.api.util.unwrap
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.function.Supplier

typealias HTSimpleHolderLike<R> = HTHolderLike<R, R>

/**
 * [ResourceKey]と値を保持する[HTKeyLike]の拡張インターフェースです。
 * @param R レジストリの要素のクラス
 * @param T 保持している値のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTHolderLike<R : Any, T : R> :
    HTKeyLike<R>,
    Supplier<T> {
    fun unwrap(): Either<ResourceKey<R>, Holder<R>>

    fun getHolder(holderGetter: (ResourceKey<R>) -> Holder<R>): Holder<R> = unwrap().mapLeft(holderGetter).unwrap()

    override fun getResourceKey(): ResourceKey<R> = unwrap().mapRight(Holder<R>::unwrapKey.andThen { it.orElseThrow() }).unwrap()
}

//    Extensions    //

/**
 * この[Holder][this]を[HTHolderLike]に変換します。
 * @param R レジストリの要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
fun <R : Any> Holder<R>.toLike(): HTSimpleHolderLike<R> = object : HTSimpleHolderLike<R> {
    override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.Right(this@toLike.delegate)

    override fun get(): R = this@toLike.value()

    override fun toString(): String = this@toLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun <R : Any, T : R> DeferredHolder<R, T>.toLike(): HTHolderLike<R, T> = object : HTHolderLike<R, T> {
    override fun unwrap(): Either<ResourceKey<R>, Holder<R>> = Either.Right(this@toLike.delegate)

    override fun get(): T = this@toLike.get()

    override fun toString(): String = this@toLike.toString()
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
fun <BLOCK : Block> BLOCK.toLike(): HTBlockHolderLike<BLOCK> = object : HTBlockHolderLike<BLOCK> {
    @Suppress("DEPRECATION")
    override fun unwrap(): Either<ResourceKey<Block>, Holder<Block>> = Either.Right(get().builtInRegistryHolder())

    override fun get(): BLOCK = this@toLike

    override fun toString(): String = this@toLike.toString()
}

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
 * @since 0.13.0
 */
typealias HTSimpleFluidHolderLike = HTFluidHolderLike<Fluid>

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun <FLUID : Fluid> FLUID.toLike(): HTFluidHolderLike<FLUID> = object : HTFluidHolderLike<FLUID> {
    @Suppress("DEPRECATION")
    override fun unwrap(): Either<ResourceKey<Fluid>, Holder<Fluid>> = Either.Right(get().builtInRegistryHolder())

    override fun get(): FLUID = this@toLike

    override fun toString(): String = this@toLike.toString()
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.getBucket(): Item = this.get().bucket

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.getBucketHolder(): HTItemHolderLike<*> = this.getBucket().toLike()

/**
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
fun HTFluidHolderLike<*>.getFluidType(): FluidType = this.get().fluidType

// FluidStack
fun HTFluidHolderLike<*>.isOf(stack: FluidStack): Boolean = stack.`is`(this.get())

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

/**
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
fun FluidStack.getHolderLike(): HTSimpleFluidHolderLike = this.fluidHolder.toLike()
