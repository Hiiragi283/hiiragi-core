package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe

/**
 * [Recipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTProcessingRecipe
 * @see mekanism.api.recipes.MekanismRecipe
 */
interface HTRecipe : Recipe<HTRecipeInput> {
    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    @Deprecated("Use `assemble(HTRecipeInput, HolderLookup.Provider) `instead", level = DeprecationLevel.ERROR)
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getRemainingItems(input: HTRecipeInput): NonNullList<ItemStack> = super.getRemainingItems(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getIngredients(): NonNullList<Ingredient> = super.getIngredients()

    override fun isSpecial(): Boolean = true
}
