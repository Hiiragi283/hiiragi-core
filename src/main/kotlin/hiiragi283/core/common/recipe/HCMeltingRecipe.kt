package hiiragi283.core.common.recipe

import hiiragi283.core.api.HTMinMaxRange
import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HCMeltingRecipe(
    val ingredient: HTItemIngredient,
    val result: HTFluidResult,
    val heatRange: HTMinMaxRange<Int>,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HCMeltingRecipe.Input>,
    HTFluidRecipe<HCMeltingRecipe.Input> {
    override fun test(input: Input): Boolean = ingredient.test(input.item) && input.temperature in heatRange

    override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.MELTING

    override fun getType(): RecipeType<*> = HCRecipeTypes.MELTING.get()

    override fun assembleFluid(input: Input, registries: HolderLookup.Provider): FluidStack = result.getStackOrEmpty(registries)

    @JvmRecord
    data class Input(val item: ItemStack, val temperature: Int) : RecipeInput {
        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> item
            else -> error("No item for index: $index")
        }

        override fun size(): Int = 1
    }
}
