package hiiragi283.core.api.registry

import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * [液体][Fluid]向けの[HTIdLike]の拡張インターフェースです。
 * @param FLUID 液体のクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTFluidHolderLike<FLUID : Fluid> : HTIdLike {
    /**
     * 保持している[液体][FLUID]を取得します。
     */
    fun asFluid(): FLUID

    /**
     * 保持している液体の[Holder]を取得します。
     */
    fun getFluidHolder(): Holder<Fluid>

    /**
     * 保持しているバケツを取得します。
     */
    fun getBucket(): Item

    /**
     * 保持しているバケツを[HTItemHolderLike]として取得します
     */
    fun getBucketHolder(): HTItemHolderLike<*> = HTItemHolderLike.of(getBucket())

    /**
     * 保持している液体の[FluidType]を取得します。
     */
    fun getFluidType(): FluidType

    /**
     * 指定した[amount]で[FluidStack]に変換します。
     */
    fun toStack(amount: Int): FluidStack = FluidStack(asFluid(), amount)

    /**
     * [HTFluidResourceType]に変換します。
     */
    fun toResource(): HTFluidResourceType? = asFluid().toResource()

    /**
     * 指定した[patch]で[HTFluidResourceType]に変換します。
     */
    fun toResource(patch: DataComponentPatch): HTFluidResourceType? = asFluid().toResource(patch)

    companion object {
        /**
         * [Holder]に基づいた[HTFluidHolderLike]の[BiCodec]
         */
        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.FLUID)
            .xmap(Holder<Fluid>::value.andThen(::of), HTFluidHolderLike<*>::getFluidHolder)

        /**
         * 指定した[holder]から[HTFluidHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(holder: Holder<Fluid>): HTFluidHolderLike<Fluid> = object : Simple<Fluid> {
            override fun asFluid(): Fluid = holder.value()

            @Suppress("DEPRECATION")
            override fun getFluidHolder(): Holder<Fluid> = holder
        }

        /**
         * 指定した[fluid]から[HTFluidHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun <FLUID : Fluid> of(fluid: FLUID): HTFluidHolderLike<FLUID> = object : Simple<FLUID> {
            override fun asFluid(): FLUID = fluid

            @Suppress("DEPRECATION")
            override fun getFluidHolder(): Holder<Fluid> = fluid.builtInRegistryHolder()
        }
    }

    /**
     * @author Hiiragi Tsubasa
     * @since 0.11.0
     */
    interface Simple<FLUID : Fluid> : HTFluidHolderLike<FLUID> {
        override fun getId(): ResourceLocation = getFluidHolder().toLike().getId()

        override fun getBucket(): Item = asFluid().bucket

        override fun getFluidType(): FluidType = asFluid().fluidType
    }
}
