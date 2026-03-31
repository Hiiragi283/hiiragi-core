package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.base.HTItemToItemRecipe
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.neoforged.neoforge.common.crafting.SizedIngredient

/**
 * @see net.minecraft.world.item.crafting.SingleItemRecipe
 */
abstract class HTBasicItemToItemRecipe(val ingredient: SizedIngredient, val result: HTItemResult, final override val time: Int) :
    HTItemToItemRecipe.Serializable {
    final override fun getRequiredAmount(input: SingleRecipeInput): Int = ingredient.count()

    final override fun test(input: SingleRecipeInput): Boolean = ingredient.test(input.item())

    final override fun assemble(input: SingleRecipeInput): ItemStack = result.create()
}
