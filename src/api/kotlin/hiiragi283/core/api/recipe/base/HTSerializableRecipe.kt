package hiiragi283.core.api.recipe.base

import hiiragi283.core.api.recipe.HTRecipe
import net.minecraft.world.item.crafting.PlacementInfo
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

    override fun isSpecial(): Boolean = true

    override fun showNotification(): Boolean = true

    override fun group(): String = ""

    override fun placementInfo(): PlacementInfo = PlacementInfo.NOT_PLACEABLE
}
