package hiiragi283.core.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import java.util.function.Predicate

/**
 * 最低限の機能だけを切り取ったレシピを表すインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.12.0
 * @see hiiragi283.core.api.recipe.base.HTProcessingRecipe
 */
interface HTRecipe<INPUT : RecipeInput> : Predicate<INPUT> {
    /**
     * 指定された[input]が，このレシピの条件を満たすか判定します。
     */
    abstract override fun test(input: INPUT): Boolean

    /**
     * 指定された[input]から完成品を作成します。
     */
    fun assemble(input: INPUT): ItemStack
}
