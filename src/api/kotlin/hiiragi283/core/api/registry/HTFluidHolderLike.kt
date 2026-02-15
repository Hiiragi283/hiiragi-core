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

interface HTFluidHolderLike<FLUID : Fluid> : HTIdLike {
    fun asFluid(): FLUID

    fun getFluidHolder(): Holder<Fluid>

    /**
     * 保持しているバケツを取得します。
     */
    fun getBucket(): Item

    fun getBucketHolder(): HTItemHolderLike<*> = HTItemHolderLike.of(getBucket())

    /**
     * 保持している液体の[FluidType]を取得します。
     */
    fun getFluidType(): FluidType

    fun toStack(amount: Int): FluidStack = FluidStack(asFluid(), amount)

    fun toResource(): HTFluidResourceType? = asFluid().toResource()

    fun toResource(patch: DataComponentPatch): HTFluidResourceType? = asFluid().toResource(patch)

    companion object {
        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.FLUID)
            .xmap(Holder<Fluid>::value.andThen(::of), HTFluidHolderLike<*>::getFluidHolder)

        @JvmStatic
        fun of(holder: Holder<Fluid>): HTFluidHolderLike<Fluid> = object : Simple<Fluid> {
            override fun asFluid(): Fluid = holder.value()

            @Suppress("DEPRECATION")
            override fun getFluidHolder(): Holder<Fluid> = holder
        }

        @JvmStatic
        fun <FLUID : Fluid> of(fluid: FLUID): HTFluidHolderLike<FLUID> = object : Simple<FLUID> {
            override fun asFluid(): FLUID = fluid

            @Suppress("DEPRECATION")
            override fun getFluidHolder(): Holder<Fluid> = fluid.builtInRegistryHolder()
        }
    }

    interface Simple<FLUID : Fluid> : HTFluidHolderLike<FLUID> {
        override fun getId(): ResourceLocation = getFluidHolder().toLike().getId()

        override fun getBucket(): Item = asFluid().bucket

        override fun getFluidType(): FluidType = asFluid().fluidType
    }
}
