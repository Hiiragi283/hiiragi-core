package hiiragi283.lib.recipe.cache

import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.lookup.HTRecipeLookup
import hiiragi283.lib.recipe.lookup.HTRecipeLookupContext
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.context.ContextMap
import net.neoforged.neoforge.common.util.TriPredicate

/**
 * 3種類の入力を受け取る[HTBasicRecipeCache]の拡張クラスです。
 *
 * 参照 : [Mekanism - TripleInputRecipeCache](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/recipe/lookup/cache/TripleInputRecipeCache.java)
 * @param INPUT_A 1番目のレシピの入力となるクラス
 * @param INPUT_B 2番目のレシピの入力となるクラス
 * @param INPUT_C 3番目のレシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTTripleInputRecipeCache<INPUT_A : Any, INPUT_B : Any, INPUT_C : Any, RECIPE : TriPredicate<INPUT_A, INPUT_B, INPUT_C>>(lookup: HTRecipeLookup<RECIPE>) : HTBasicRecipeCache<RECIPE>(lookup) {
    fun findFirstRecipe(
        firstInput: INPUT_A,
        secondInput: INPUT_B,
        thirdInput: INPUT_C,
        level: ServerLevel,
    ): RECIPE? = findFirstRecipe(firstInput, secondInput, thirdInput, HTRecipeLookupContext.create(level))

    fun findFirstRecipe(
        firstInput: INPUT_A,
        secondInput: INPUT_B,
        thirdInput: INPUT_C,
        context: ContextMap,
    ): RECIPE? = findFirstHolder(firstInput, secondInput, thirdInput, context)?.recipe

    fun findFirstHolder(
        firstInput: INPUT_A,
        secondInput: INPUT_B,
        thirdInput: INPUT_C,
        context: ContextMap,
    ): HTRecipeHolder<RECIPE>? {
        if (isEmpty(firstInput, secondInput, thirdInput)) return null
        if (lastRecipe != null && lastRecipe!!.recipe.test(firstInput, secondInput, thirdInput)) {
            return lastRecipe
        }
        lastRecipe = lookup
            .getAllRecipes(context)
            .firstOrNull { (_, recipe: RECIPE) -> recipe.test(firstInput, secondInput, thirdInput) }
            ?: return null
        return lastRecipe
    }

    protected abstract fun isEmpty(firstInput: INPUT_A, secondInput: INPUT_B, thirdInput: INPUT_C): Boolean
}
