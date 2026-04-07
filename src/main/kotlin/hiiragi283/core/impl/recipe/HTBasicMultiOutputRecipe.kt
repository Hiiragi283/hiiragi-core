package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.base.HTMultiOutputRecipe
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

abstract class HTBasicMultiOutputRecipe<INPUT : RecipeInput>(val results: List<HTItemResult>, final override val time: Int) :
    HTMultiOutputRecipe.Serializable<INPUT> {
    final override fun assembleItems(input: INPUT, registries: HolderLookup.Provider): List<ItemStack> =
        results.map { it.getStackOrEmpty(registries) }.filterNot(ItemStack::isEmpty)
}
