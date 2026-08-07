package hiiragi283.core.common.recipe

import hiiragi283.core.api.recipe.base.HTItemToItemRecipe
import hiiragi283.core.api.recipe.ingredient.getMatchingStack
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.progress.HTProgressProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.Ingredient

@JvmRecord
data class HCCookingRecipe(private val ingredient: Ingredient, private val result: ItemStack, override val progressData: HTProgressData) :
    HTItemToItemRecipe,
    HTProgressProvider.Simple<ItemStack> {
    constructor(recipe: AbstractCookingRecipe) : this(recipe.ingredient, recipe.result, HTProgressData.time(recipe.cookingTime))

    override fun test(input: ItemStack): Boolean = ingredient.test(input)

    override fun getMatchingStack(input: ItemStack): ItemStack = ingredient.getMatchingStack(input)

    override fun assemble(input: ItemStack): ItemStack = result.copy()

    override fun isIncomplete(): Boolean = ingredient.hasNoItems()
}
