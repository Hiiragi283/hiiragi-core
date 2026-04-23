package hiiragi283.core.api.registry

import com.mojang.serialization.Codec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

typealias HTSimpleFluidHolderLike = HTFluidHolderLike<Fluid>

/**
 * [液体][Fluid]向けの[HTHolderLike]の拡張インターフェースです。
 * @param FLUID 液体のクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
interface HTFluidHolderLike<FLUID : Fluid> : HTHolderLike<Fluid, FLUID> {
    fun getHolder(): Holder<Fluid> = getHolder(BuiltInRegistries.FLUID::getHolderOrThrow)

    /**
     * 保持している液体に対応するバケツを取得します。
     */
    fun getBucket(): HTSimpleItemHolderLike

    /**
     * 保持している液体に対応する[FluidType]を取得します。
     */
    fun getFluidType(): FluidType

    // FluidStack
    fun isOf(stack: FluidStack): Boolean = stack.`is`(this.get())

    /**
     * 指定した[量][amount]で[FluidStack]に変換します。
     */
    fun toStack(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack = FluidStack(this.get(), amount)

    // HTFluidResourceType

    /**
     * [HTFluidResourceType]に変換します。
     */
    fun toResource(): HTFluidResourceType? = toStack().toResource()

    /**
     * 指定した[patch]で[HTFluidResourceType]に変換します。
     */
    fun toResource(patch: DataComponentPatch): HTFluidResourceType? {
        val stack: FluidStack = toStack()
        stack.applyComponents(patch)
        return stack.toResource()
    }

    companion object {
        @JvmField
        val CODEC: Codec<HTSimpleFluidHolderLike> =
            HTCodecs.holderLike(Registries.FLUID).xmap(HTSimpleHolderLike<Fluid>::toFluidLike, identity())

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSimpleFluidHolderLike> =
            HTStreamCodecs.holderLike(Registries.FLUID).map(HTSimpleHolderLike<Fluid>::toFluidLike, identity())
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.13.0
     */
    interface Simple<FLUID : Fluid> : HTFluidHolderLike<FLUID> {
        override fun getBucket(): HTSimpleItemHolderLike = get().bucket.toItemLike()

        override fun getFluidType(): FluidType = get().fluidType
    }
}

//    Extensions    //

@Suppress("DEPRECATION")
fun <FLUID : Fluid> FLUID.toLike(): HTFluidHolderLike<FLUID> = object : HTFluidHolderLike.Simple<FLUID> {
    override fun unwrap(): Either<ResourceKey<Fluid>, Holder<Fluid>> = Either.Right(this@toLike.builtInRegistryHolder())

    override fun get(): FLUID = this@toLike
}

fun <FLUID : Fluid> HTHolderLike<Fluid, FLUID>.toFluidLike(): HTFluidHolderLike<FLUID> = object : HTFluidHolderLike.Simple<FLUID> {
    override fun unwrap(): Either<ResourceKey<Fluid>, Holder<Fluid>> = this@toFluidLike.unwrap()

    override fun get(): FLUID = this@toFluidLike.get()
}

fun FluidStack.getHolderLike(): HTSimpleFluidHolderLike = this.fluidHolder.toLike().toFluidLike()
