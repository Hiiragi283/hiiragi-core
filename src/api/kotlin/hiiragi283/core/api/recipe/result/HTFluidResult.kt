package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import hiiragi283.core.api.storage.fluid.toResource
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
class HTFluidResult(contents: Ior<HTFluidResourceType, TagKey<Fluid>>, amount: Int) :
    HTResourceRecipeResult<Fluid, HTFluidResourceType, FluidStack>(validate(contents), amount) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = BiCodec.composite(
            MapBiCodecs
                .ior(
                    HTFluidResourceType.CODEC.toOptional().toMap(),
                    VanillaBiCodecs.tagKey(Registries.FLUID, false).optionalFieldOf(HTConst.TAG),
                ).forGetter(HTFluidResult::contents),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.AMOUNT, HTConst.DEFAULT_FLUID_AMOUNT).forGetter(HTFluidResult::amount),
            ::HTFluidResult,
        )

        /**
         * [HTFluidResult]の新しいインスタンスを作成します。
         * @since 0.8.0
         */
        @HTBuilderMarker
        @JvmStatic
        fun create(builderAction: Builder.() -> Unit): HTFluidResult = Builder().apply(builderAction).build()

        /**
         * 液体流が指定されている場合，液体源に置き換える
         */
        @JvmStatic
        private fun validate(contents: Ior<HTFluidResourceType, TagKey<Fluid>>): Ior<HTFluidResourceType, TagKey<Fluid>> =
            contents.mapLeft { resource: HTFluidResourceType ->
                val (holder: Holder<Fluid>, patch: DataComponentPatch) = resource
                val fluid: Fluid = holder.value()
                if (!fluid.isSource(fluid.defaultFluidState()) && fluid is FlowingFluid) {
                    fluid.source.toResource(patch) ?: resource
                } else {
                    resource
                }
            }
    }

    override fun getEmptyStack(): FluidStack = FluidStack.EMPTY

    override fun createStack(resource: HTFluidResourceType, amount: Int): FluidStack = resource.toStack(amount)

    override fun createStack(holder: Holder<Fluid>, amount: Int): FluidStack = FluidStack(holder, amount)

    //    Builder    //

    /**
     * [HTFluidResult]向けのビルダークラスです。
     * @author Hiiragi Tsubasa
     * @since 0.8.0
     */
    class Builder {
        var fluid: HTFluidResourceType? = null
        var tagKey: TagKey<Fluid>? = null
        var amount: Int = HTConst.DEFAULT_FLUID_AMOUNT

        fun build(): HTFluidResult = HTFluidResult((fluid to tagKey).toIorOrThrow("Either fluid or tag required for result"), amount)
    }
}
