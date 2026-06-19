package hiiragi283.lib.recipe.lookup

import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText

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
    HTIdLike.Translatable {
    override val translationKey: String get() = getId().toLanguageKey("recipe_type")

    override fun getText(): Text = translatableText(translationKey)
}
