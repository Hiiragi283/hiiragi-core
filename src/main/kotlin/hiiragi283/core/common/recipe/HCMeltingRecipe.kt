package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.setup.HCRecipeBookCategories
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.advancements.criterion.MinMaxBounds
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HCMeltingRecipe(
    val ingredient: Ingredient,
    val result: HTFluidResult,
    val heatRange: MinMaxBounds.Ints,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HCMeltingRecipe.Input>,
    HTFluidRecipe<HCMeltingRecipe.Input> {
    override fun test(input: Input): Boolean = ingredient.test(input.item) && heatRange.matches(input.temperature)

    override fun assemble(input: Input): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<HCMeltingRecipe> = HCRecipeSerializers.MELTING

    override fun getType(): RecipeType<HCMeltingRecipe> = HCRecipeTypes.MELTING.get()

    override fun recipeBookCategory(): RecipeBookCategory = HCRecipeBookCategories.MELTING

    override fun assembleFluid(input: Input): FluidStack = result.create()

    @JvmRecord
    data class Input(val item: ItemStack, val temperature: Int) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> item
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 1
    }
}
