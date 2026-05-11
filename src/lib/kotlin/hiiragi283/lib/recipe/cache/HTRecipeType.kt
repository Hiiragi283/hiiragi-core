package hiiragi283.lib.recipe.cache

import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText

/**
 * [HTRecipeLookup]の拡張インターフェースです。
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTRecipeType<RECIPE : Any> :
    HTRecipeLookup<RECIPE>,
    HTIdLike.Translatable {
    override val translationKey: String
        get() = getId().toLanguageKey("recipe_type")

    override fun getText(): Text = translatableText(translationKey)
}
