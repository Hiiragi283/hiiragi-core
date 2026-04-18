package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTTankEmptyingRecipe
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

class HCTankEmptyingRecipe(val ingredient: Ingredient, val fluidResult: HTFluidResult, val itemResult: Optional<HTItemResult>) :
    HTTankEmptyingRecipe.Serializable {
    override fun testContainer(stack: ItemStack): Boolean = ingredient.test(stack)

    override fun assemble(input: SingleRecipeInput, preview: Boolean): ItemStack = itemResult.map { it.getOrEmpty(preview) }.getOrEmpty()

    override fun assembleFluid(input: SingleRecipeInput): FluidStack = fluidResult.getOrEmpty()

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.EMPTYING

    override fun getType(): RecipeType<*> = HCRecipeTypes.EMPTYING.get()
}
