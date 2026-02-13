package hiiragi283.core.api.registry

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

@Suppress("DEPRECATION")
interface HTFluidHolderLike<FLUID : Fluid> : HTIdLike {
    companion object {
        @JvmField
        val KEY_CODEC: BiCodec<ByteBuf, HTFluidHolderLike<*>> = VanillaBiCodecs
            .resourceKey(Registries.FLUID)
            .xmap(::of, HTFluidHolderLike<*>::getFluidKey)

        @JvmField
        val HOLDER_CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidHolderLike<*>> = VanillaBiCodecs
            .holder(Registries.FLUID)
            .xmap(Holder<Fluid>::value.andThen(::of), HTFluidHolderLike<*>::getFluidHolder)

        /**
         * 指定した[id]から[HTFluidHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(id: ResourceLocation): HTFluidHolderLike<*> = of(Registries.FLUID.createKey(id))

        /**
         * 指定した[key]から[HTFluidHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun of(key: ResourceKey<Fluid>): HTFluidHolderLike<*> = object : Delegated<Fluid> {
            private val holder: Holder<Fluid> by lazy { BuiltInRegistries.FLUID.getHolderOrThrow(key) }

            override fun getFluidHolder(): Holder<Fluid> = holder

            override fun getFluidKey(): ResourceKey<Fluid> = key

            override fun asFluid(): Fluid = getFluidHolder().value()
        }

        /**
         * 指定した[fluid]から[HTFluidHolderLike]の新しいインスタンスを作成します。
         */
        @JvmStatic
        fun <FLUID : Fluid> of(fluid: FLUID): HTFluidHolderLike<FLUID> = object : Delegated<FLUID> {
            override fun getFluidHolder(): Holder<Fluid> = asFluid().builtInRegistryHolder()

            override fun asFluid(): FLUID = fluid
        }
    }

    fun getFluidHolder(): Holder<Fluid>

    fun getFluidKey(): ResourceKey<Fluid> = getFluidHolder().unwrapKey().orElseThrow()

    fun asFluid(): FLUID

    /**
     * 保持しているバケツを取得します。
     */
    fun getBucket(): Item

    fun getBucketHolder(): HTItemHolderLike<*>

    /**
     * 保持している液体の[FluidType]を取得します。
     */
    fun getFluidType(): FluidType

    fun toStack(amount: Int = HTConst.DEFAULT_FLUID_AMOUNT): FluidStack = FluidStack(asFluid(), amount)

    fun toResource(patch: DataComponentPatch = DataComponentPatch.EMPTY): HTFluidResourceType? = asFluid().toResource(patch)

    interface Delegated<FLUID : Fluid> : HTFluidHolderLike<FLUID> {
        override fun getId(): ResourceLocation = getFluidKey().location()

        override fun getBucket(): Item = asFluid().bucket

        override fun getBucketHolder(): HTItemHolderLike<*> = HTItemHolderLike.of(getBucket())

        override fun getFluidType(): FluidType = asFluid().fluidType
    }
}
