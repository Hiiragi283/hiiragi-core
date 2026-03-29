package hiiragi283.core.impl.recipe

import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.recipe.HTRecipeCache
import hiiragi283.core.api.recipe.HTRecipeLookup
import hiiragi283.core.api.recipe.RecipeKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [HTRecipeLookup]に基づいた[HTRecipeCache]の実装クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
 * @param predicate レシピが一致するかを判定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 */
class HTLookupRecipeCache<INPUT : RecipeInput, RECIPE : Any, HOLDER : Any>(
    val lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
    private val predicate: (RECIPE, INPUT, ServerLevel) -> Boolean,
) : HTRecipeCache<INPUT, RECIPE> {
    companion object {
        /**
         * 指定した[lookup]から，[Recipe.matches]に基づいた[HTLookupRecipeCache]の新しいインスタンスを作成します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE レシピのクラス
         */
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> forManager(
            lookup: HTRecipeLookup.Managed<INPUT, RECIPE>,
        ): HTLookupRecipeCache<INPUT, RECIPE, RecipeHolder<RECIPE>> = HTLookupRecipeCache(lookup, Recipe<INPUT>::matches)

        /**
         * 指定した[lookup]から，[HTRecipe.test]に基づいた[HTLookupRecipeCache]の新しいインスタンスを作成します。
         * @param INPUT レシピの入力となるクラス
         * @param RECIPE [HTRecipe]を実装したクラス
         * @param HOLDER [RecipeKey]と[RECIPE]を束ねたクラス
         */
        @JvmStatic
        fun <INPUT : RecipeInput, RECIPE : HTRecipe<INPUT>, HOLDER : Any> forRecipe(
            lookup: HTRecipeLookup<INPUT, RECIPE, HOLDER>,
        ): HTLookupRecipeCache<INPUT, RECIPE, HOLDER> =
            HTLookupRecipeCache(lookup) { recipe: RECIPE, input: INPUT, _ -> recipe.test(input) }
    }

    private var lastRecipe: HOLDER? = null

    override fun getFirstRecipe(input: INPUT, level: ServerLevel): RECIPE? {
        val holder: HOLDER = lastRecipe ?: return run {
            lookup.findFirst(level) { predicate(it, input, level) }.also(::lastRecipe::set)?.let(lookup::getRecipe)
        }
        val recipe: RECIPE = lookup.getRecipe(holder)
        if (predicate(recipe, input, level)) {
            return recipe
        } else {
            lastRecipe = null
            return null
        }
    }
}
