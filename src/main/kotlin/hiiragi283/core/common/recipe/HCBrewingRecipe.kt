package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.FluidAmount
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.ItemAmount
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.FluidIngredient
import java.util.function.Predicate

data class HCBrewingRecipe(val potionFrom: FluidIngredient, val ingredient: Ingredient, val potionTo: HTFluidResult) :
    HTItemOrFluidRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCBrewingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTCodecs.FLUID_INGREDIENT.forGetter(HCBrewingRecipe::potionFrom),
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCBrewingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCBrewingRecipe::potionTo),
                ).apply(instance, ::HCBrewingRecipe)
        }
    }

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Both(ingredient, potionFrom)

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> =
        Ior.Both(1, HTConst.DEFAULT_FLUID_AMOUNT)

    override val time: Int = 100

    override fun assemble(input: HTItemAndFluidRecipeInput, preview: Boolean): ItemStack = ItemStack.EMPTY

    override fun assembleFluid(input: HTItemAndFluidRecipeInput): FluidStack = potionTo.getOrEmpty()
}
