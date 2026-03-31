package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.util.Either
import net.minecraft.core.Holder
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.fluids.FluidType

typealias HTSimpleFluidHolderLike = HTFluidHolderLike<Fluid>

/**
 * [液体][Fluid]向けの[HTHolderLike]の拡張インターフェースです。
 * @param FLUID 液体のクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
interface HTFluidHolderLike<FLUID : Fluid> : HTHolderLike<Fluid, FLUID> {
    fun getHolder(): Holder<Fluid> = getHolder(BuiltInRegistries.FLUID)

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

    // FluidStackTemplate

    /**
     * 指定した[量][amount]で[FluidStackTemplate]に変換します。
     */
    fun toTemplate(amount: Int = FluidType.BUCKET_VOLUME): FluidStackTemplate = FluidStackTemplate(this.getHolder(), amount)

    /**
     * 指定した[量][amount]と[patch]で[FluidStackTemplate]に変換します。
     */
    fun toTemplate(amount: Int = FluidType.BUCKET_VOLUME, patch: DataComponentPatch): FluidStackTemplate =
        FluidStackTemplate(this.getHolder(), amount, patch)

    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTSimpleFluidHolderLike> =
            VanillaBiCodecs.holderLike(Registries.FLUID).xmap(HTSimpleHolderLike<Fluid>::toFluidLike, identity())
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

fun TypedInstance<Fluid>.fluidHolderLike(): HTSimpleFluidHolderLike = this.holderLike().toFluidLike()
