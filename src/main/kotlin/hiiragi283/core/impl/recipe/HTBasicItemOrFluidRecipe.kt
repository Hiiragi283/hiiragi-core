package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.HTItemOrFluidRecipe
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.base.HTProgressRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Predicate

open class HTBasicItemOrFluidRecipe(
    val ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
    val result: Ior<HTItemResult, HTFluidResult>,
    override val progressData: HTProgressData,
) : HTItemOrFluidRecipe,
    HTProgressRecipe.Simple<HTItemAndFluidRecipeInput> {
    override fun test(first: ItemStack, second: FluidStack): Boolean = ingredient.fold(
        { it.test(first) && second.isEmpty },
        { it.test(second) && first.isEmpty },
        { item: Predicate<ItemStack>, fluid: Predicate<FluidStack> -> item.test(first) && fluid.test(second) },
    )

    override fun getRequiredAmount(first: ItemStack, second: FluidStack): Pair<Int, Int> {
        val (item: HTItemIngredient?, fluid: HTFluidIngredient?) = ingredient.toPair()
        return (item?.getRequiredAmount(first) ?: 0) to (fluid?.getRequiredAmount(second) ?: 0)
    }

    override fun assemble(firstInput: ItemStack, secondInput: FluidStack): Ior<ItemStack, FluidStack> =
        result.mapLeft { it.getOrEmpty() }.mapRight { it.create() }
}
