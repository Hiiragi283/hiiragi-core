package hiiragi283.core.api.recipe

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTRecipeLookup]の拡張インターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.11.0
 */
sealed interface HTRecipeType<INPUT : RecipeInput, RECIPE : Any> :
    HTHasTranslationKey,
    HTHasText,
    HTIdLike {
    override val translationKey: String
        get() = getId().toLanguageKey("recipe_type")

    override fun getText(): Text = translatableText(translationKey)

    interface Managed<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> :
        HTRecipeType<INPUT, RECIPE>,
        HTRecipeLookup.Managed<INPUT, RECIPE>

    interface Fake<INPUT : RecipeInput, RECIPE : Any> :
        HTRecipeType<INPUT, RECIPE>,
        HTRecipeLookup.Fake<INPUT, RECIPE>
}
