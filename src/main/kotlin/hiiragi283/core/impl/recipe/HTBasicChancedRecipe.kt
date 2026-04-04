package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.getOrEmpty
import hiiragi283.core.common.recipe.base.HTChancedRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import java.util.Optional

abstract class HTBasicChancedRecipe<INPUT : RecipeInput>(
    val result: HTItemResult,
    val extraResult: Optional<HTItemResult>,
    final override val time: Int,
) : HTChancedRecipe.Serializable<INPUT> {
    final override fun assembleExtraItem(input: INPUT, chance: Float): ItemStack = extraResult.map { it.create(chance) }.getOrEmpty()

    final override fun assemble(input: INPUT): ItemStack = result.create()
}
