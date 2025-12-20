package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.stack.ImmutableFluidStack
import hiiragi283.core.api.stack.toImmutable
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.FluidStack

class HTFluidResult(entry: HTHolderOrTagKey<Fluid>, amount: Int, components: DataComponentPatch) :
    HTBasicRecipeResult<Fluid, ImmutableFluidStack>(entry, amount, components) {
    companion object {
        @JvmStatic
        private val ENTRY_CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTHolderOrTagKey<Fluid>> = HTHolderOrTagKey
            .codec(Registries.FLUID)
            .validate { entry: HTHolderOrTagKey<Fluid> ->
                check(entry.getId() != vanillaId("empty")) { "Fluid must not be minecraft:empty" }
                entry
            }

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = BiCodec.composite(
            ENTRY_CODEC.forGetter(HTFluidResult::entry),
            BiCodecs.POSITIVE_INT.fieldOf(HTConst.AMOUNT).forGetter(HTFluidResult::amount),
            VanillaBiCodecs.COMPONENT_PATCH.forGetter(HTFluidResult::components),
            ::HTFluidResult,
        )
    }

    override fun createStack(holder: Holder<Fluid>, amount: Int, components: DataComponentPatch): ImmutableFluidStack? =
        FluidStack(holder, amount, components).toImmutable()

    override fun toString(): String = "HTFluidResult(entry=$entry, amount=$amount, components=$components)"
}
