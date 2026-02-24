package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTSerializableRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

abstract class HCSingleItemRecipe<INPUT : RecipeInput>(val ingredient: HTItemIngredient, val result: HTItemResult) :
    HTSerializableRecipe<INPUT> {
    final override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
