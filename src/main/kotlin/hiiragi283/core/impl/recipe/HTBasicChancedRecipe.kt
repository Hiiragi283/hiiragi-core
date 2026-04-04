package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.HTChancedRecipe
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.recipe.result.getStackOrNull
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.core.api.util.mapNotNull
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import java.util.Optional

abstract class HTBasicChancedRecipe<INPUT : RecipeInput>(
    val result: HTItemResult,
    val extraResult: Optional<HTChancedItemResult>,
    final override val time: Int,
) : HTChancedRecipe.Serializable<INPUT> {
    final override fun assembleExtraItem(input: INPUT, registries: HolderLookup.Provider, chance: Float): ItemStack =
        extraResult.mapNotNull { it.getStackOrNull(registries, chance) }.getOrEmpty()

    final override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = result.getStackOrEmpty(registries)
}
