package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.resource.HTKeyLike
import net.minecraft.world.item.crafting.RecipeType

/**
 * [HTRecipeLookup]の拡張インターフェースです。
 *
 * 参照 : [Mekanism - IMekanismRecipeTypeProvider](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/recipe/IMekanismRecipeTypeProvider.java)
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTRecipeType<out RECIPE> :
    HTRecipeLookup<RECIPE>,
    HTKeyLike.SimpleTranslatable<RecipeType<*>>
