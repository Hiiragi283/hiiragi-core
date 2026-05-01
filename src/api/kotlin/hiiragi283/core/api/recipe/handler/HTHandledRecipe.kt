package hiiragi283.core.api.recipe.handler

import hiiragi283.core.api.recipe.HTRecipeFactory
import hiiragi283.core.api.recipe.HTRecipePredicate
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [RecipeInput]と[HTRecipeFactory]を束ねたクラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.13.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTHandledRecipe<INPUT : RecipeInput, OUTPUT : Any, RECIPE : HTRecipeFactory<INPUT, OUTPUT>> private constructor(
    val input: INPUT,
    val recipe: RECIPE,
) {
    companion object {
        @JvmStatic
        fun <INPUT : RecipeInput, OUTPUT : Any, RECIPE> create(
            input: INPUT,
            recipe: RECIPE,
        ): HTHandledRecipe<INPUT, OUTPUT, RECIPE>? where RECIPE : HTRecipePredicate<INPUT>, RECIPE : HTRecipeFactory<INPUT, OUTPUT> = when {
            recipe.matches(input) -> HTHandledRecipe(input, recipe)
            else -> null
        }
    }

    /**
     * レシピの完成品を取得します。
     */
    fun assemble(): OUTPUT = recipe.assemble(input)

    /**
     * 保持している[input]と[recipe]を変換します。
     * @param T 変換後のクラス
     * @param transform 変換するブロック
     */
    inline fun <T> map(transform: (RECIPE, INPUT) -> T): T = transform(recipe, input)

    /**
     * @since 0.14.0
     */
    inline fun <T, C> map(context: C, transform: (RECIPE, INPUT, C) -> T): T = transform(recipe, input, context)
}
