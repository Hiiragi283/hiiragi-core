package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.FluidAmount
import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.ItemAmount
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

abstract class HTBasicItemOrFluidRecipe(
    val ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
    val result: Ior<HTItemResult, HTFluidResult>,
    final override val time: Int,
) : HTItemOrFluidRecipe.Serializable {
    final override fun getPredicate(): Ior<Predicate<ItemStack>, Predicate<FluidStack>> =
        ingredient.mapLeft { Predicate(it::test) }.mapRight { Predicate(it::test) }

    final override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Ior<ItemAmount, FluidAmount> =
        ingredient.mapLeft { it.getRequiredAmount(input.item) }.mapRight { it.getRequiredAmount(input.fluid) }

    final override fun assemble(input: HTItemAndFluidRecipeInput, preview: Boolean): ItemStack =
        result.getLeft()?.getOrEmpty(preview) ?: ItemStack.EMPTY

    final override fun assembleFluid(input: HTItemAndFluidRecipeInput): FluidStack = result.getRight()?.getOrEmpty() ?: FluidStack.EMPTY
}
