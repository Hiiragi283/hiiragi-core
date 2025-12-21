package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * レシピを保持するキャッシュを表すインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun interface HTRecipeCache<INPUT : RecipeInput, RECIPE : Any> {
    /**
     * 指定した[入力][input]と[レベル][level]から最初に一致するレシピを返します。
     * @return 見つからなかった場合は`null`
     */
    fun getFirstRecipe(input: INPUT, level: Level): RECIPE?
}
