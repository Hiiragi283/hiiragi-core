package hiiragi283.core.api.recipe

import net.minecraft.world.item.crafting.RecipeInput

/**
 * レシピの判定部分を切り出したインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
interface HTRecipePredicate<INPUT : RecipeInput> {
    /**
     * 指定された[input]が，このレシピの条件を満たすか判定します。
     */
    fun matches(input: INPUT): Boolean

    fun isIncomplete(): Boolean
}
