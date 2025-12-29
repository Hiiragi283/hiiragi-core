package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction

class HCLightningChargingRecipe(
    val ingredient: HTItemIngredient,
    val result: HTItemResult,
    val energy: Int,
    val exp: Fraction,
) : HTRecipe {
    companion object {
        const val DEFAULT_ENERGY = 1_000_000
    }

    override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testItem(0, ingredient)

    override fun assemble(input: HTRecipeInput, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)

    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CHARGING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CHARGING.get()
}
