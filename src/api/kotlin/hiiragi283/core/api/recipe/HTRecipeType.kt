package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.cache.HTRecipeLookup
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTRecipeLookup]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTRecipeType<INPUT : RecipeInput, RECIPE : Any> :
    HTRecipeLookup<INPUT, RECIPE>,
    HTIdLike.Translatable {
    override val translationKey: String
        get() = getId().toLanguageKey("recipe_type")

    override fun getText(): Text = translatableText(translationKey)
}
