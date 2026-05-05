package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTComparators
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.item.alchemy.BottledPotionContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.util.Ior
import hiiragi283.core.mixin.PotionBrewingMixAccessor
import hiiragi283.core.util.HCPotionFluidHelper
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HCBrewingRecipe(val potionFrom: Holder<Potion>, val ingredient: Ingredient, val potionTo: Holder<Potion>) :
    HTItemOrFluidRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCBrewingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.holder(Registries.POTION).fieldOf("potion_from").forGetter(HCBrewingRecipe::potionFrom),
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCBrewingRecipe::ingredient),
                    HTCodecs.holder(Registries.POTION).fieldOf("potion_to").forGetter(HCBrewingRecipe::potionFrom),
                ).apply(instance, ::HCBrewingRecipe)
        }

        @JvmField
        val SORTER: Comparator<in HCBrewingRecipe> =
            compareBy<HCBrewingRecipe, ResourceLocation>(HTComparators.ID) { it.potionTo.toLike().getId() }
                .thenComparing({ it.potionFrom.toLike().getId() }, HTComparators.ID)
    }

    constructor(accessor: PotionBrewingMixAccessor<Potion>) : this(accessor.from, accessor.ingredient, accessor.to)

    override fun test(first: ItemStack, second: FluidStack): Boolean =
        ingredient.test(first) && HTPotionHelper.getPotion(second).`is`(potionFrom)

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> = when {
        test(first, second) -> 1 to HTConst.DEFAULT_FLUID_AMOUNT
        else -> 0 to 0
    }

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): Ior<ItemStack, FluidStack> =
        Ior.Right(BottledPotionContents(potionTo).let(HCPotionFluidHelper::createFluid))

    override val progressData: HTProgressData
        get() = HTProgressData.time(200)
}
