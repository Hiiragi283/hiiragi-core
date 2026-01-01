package hiiragi283.core.api.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [Recipe]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTProcessingRecipe
 * @see mekanism.api.recipes.MekanismRecipe
 */
interface HTRecipe<INPUT : RecipeInput> : Recipe<INPUT> {
    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getRemainingItems(input: INPUT): NonNullList<ItemStack> = super.getRemainingItems(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getIngredients(): NonNullList<Ingredient> = super.getIngredients()

    override fun isSpecial(): Boolean = true
}
