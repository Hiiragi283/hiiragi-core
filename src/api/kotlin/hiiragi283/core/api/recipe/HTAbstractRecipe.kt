package hiiragi283.core.api.recipe

import hiiragi283.core.api.recipe.input.HTRecipeInput
import hiiragi283.core.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level

/**
 * 入力の一致と完成品の出力を行うインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTRecipe
 */
interface HTAbstractRecipe {
    /**
     * 指定した[入力][input]と[レベル][level]に一致するか判定します。
     * @see Recipe.matches
     */
    fun matches(input: HTRecipeInput, level: Level): Boolean

    /**
     * 指定した[入力][input]と[レジストリ][provider]から完成品を返します。
     * @return 完成品がない場合は`null`
     * @see Recipe.assemble
     */
    fun assembleItem(input: HTRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack?

    //    Modifiable    //

    /**
     * 個数を変えられるレシピを表す[HTAbstractRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     */
    interface Modifiable<RECIPE : HTAbstractRecipe> : HTAbstractRecipe {
        /**
         * 指定した[倍率][multiplier]からレシピを返します。
         * @return 個数が倍増したレシピのコピー
         */
        fun copyAndMultiply(multiplier: Int): RECIPE
    }
}
