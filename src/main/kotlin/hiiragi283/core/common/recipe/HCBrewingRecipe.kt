package hiiragi283.core.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.FluidAmount
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.ItemAmount
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

data class HCBrewingRecipe(val potionFrom: HTFluidIngredient, val ingredient: Ingredient, val potionTo: HTFluidResult) :
    HTItemOrFluidRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HCBrewingRecipe> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    HTFluidIngredient.CODEC.fieldOf("potion_from").forGetter(HCBrewingRecipe::potionFrom),
                    HTCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCBrewingRecipe::ingredient),
                    HTFluidResult.CODEC.fieldOf("potion_to").forGetter(HCBrewingRecipe::potionTo),
                ).apply(instance, ::HCBrewingRecipe)
        }
    }

    override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> = Ior.Both(ingredient, Predicate(potionFrom::test))

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> = Ior.Both(1, potionFrom.amount)

    override val time: Int = 100

    override fun assemble(input: HTItemAndFluidRecipeInput, preview: Boolean): ItemStack = ItemStack.EMPTY

    override fun assembleFluid(input: HTItemAndFluidRecipeInput): FluidStack = potionTo.getOrEmpty()
}
