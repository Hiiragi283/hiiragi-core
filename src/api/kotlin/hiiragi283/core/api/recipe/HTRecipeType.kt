package hiiragi283.core.api.recipe

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.translatableText
import net.minecraft.network.chat.Component
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTRecipeLookup]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
interface HTRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
    HTHasTranslationKey,
    HTHasText,
    HTIdLike,
    HTRecipeLookup<INPUT, RECIPE> {
    override val translationKey: String
        get() = getId().toLanguageKey("recipe_type")

    override fun getText(): Component = translatableText(translationKey)
}
