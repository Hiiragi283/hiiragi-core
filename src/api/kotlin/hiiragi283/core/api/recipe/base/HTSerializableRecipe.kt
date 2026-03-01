package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * シリアライズ可能なレシピを表す，[Recipe]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see mekanism.api.recipes.MekanismRecipe
 */
interface HTSerializableRecipe<INPUT : RecipeInput> :
    Recipe<INPUT>,
    HTRecipe<INPUT> {
    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun matches(input: INPUT, level: Level): Boolean = test(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getRemainingItems(input: INPUT): NonNullList<ItemStack> = super.getRemainingItems(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getIngredients(): NonNullList<Ingredient> = super.getIngredients()

    override fun isSpecial(): Boolean = true
}
