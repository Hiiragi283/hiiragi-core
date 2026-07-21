package hiiragi283.core.api.recipe

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
@Suppress("DeprecatedCallableAddReplaceWith")
interface HTSerializableRecipe<INPUT : RecipeInput> :
    Recipe<INPUT>,
    HTRecipePredicate<INPUT> {
    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun matches(input: INPUT, level: Level): Boolean = matches(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun assemble(input: INPUT, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getRemainingItems(input: INPUT): NonNullList<ItemStack> = super.getRemainingItems(input)

    @Deprecated("Not used in Hiiragi Series", level = DeprecationLevel.ERROR)
    override fun getIngredients(): NonNullList<Ingredient> = super.getIngredients()

    override fun isSpecial(): Boolean = true

    abstract override fun isIncomplete(): Boolean
}
