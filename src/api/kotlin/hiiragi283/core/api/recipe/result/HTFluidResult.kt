package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.storage.fluid.HTFluidResourceType
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

/**
 * [液体][FluidStack]の[完成品][HTRecipeResult]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
class HTFluidResult(contents: Ior<HTFluidResourceType, TagKey<Fluid>>, amount: Int) :
    HTResourceRecipeResult<Fluid, HTFluidResourceType, FluidStack>(contents, amount) {
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
    }

    override fun getEmptyStack(): FluidStack = FluidStack.EMPTY

    override fun createStack(resource: HTFluidResourceType, amount: Int): FluidStack = resource.toStack(amount)

    override fun createStack(holder: Holder<Fluid>, amount: Int): FluidStack = FluidStack(holder, amount)
}
