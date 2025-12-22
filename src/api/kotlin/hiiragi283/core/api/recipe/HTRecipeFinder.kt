package hiiragi283.core.api.recipe

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * 指定したインプットに一致する最初のレシピを取得する処理を表すインターフェース
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTRecipeFinder<INPUT : RecipeInput, RECIPE : Any> {
    /**
     * 指定した[入力][input]と[レベル][level]から最初に一致するレシピを返します。
     * @param lastRecipe 最後に一致したレシピのキャッシュ
     * @return 一致するレシピがない場合は`null`
     */
    fun getRecipeFor(input: INPUT, level: Level, lastRecipe: Pair<ResourceLocation, RECIPE>?): Pair<ResourceLocation, RECIPE>?

    /**
     * [Recipe]クラスに基づいた[HTRecipeFinder]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    fun interface Vanilla<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTRecipeFinder<INPUT, RECIPE> {
        override fun getRecipeFor(
            input: INPUT,
            level: Level,
            lastRecipe: Pair<ResourceLocation, RECIPE>?,
        ): Pair<ResourceLocation, RECIPE>? {
            val holder: RecipeHolder<RECIPE>? = lastRecipe?.let { RecipeHolder(it.first, it.second) }
            return getVanillaRecipeFor(input, level, holder)?.let { it.id to it.value }
        }

        /**
         * 指定した[入力][input]と[レベル][level]から最初に一致するレシピを返します。
         * @param lastRecipe 最後に一致したレシピのキャッシュ
         * @return 一致するレシピがない場合は`null`
         */
        fun getVanillaRecipeFor(input: INPUT, level: Level, lastRecipe: RecipeHolder<RECIPE>?): RecipeHolder<RECIPE>?
    }
}
