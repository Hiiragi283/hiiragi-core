package hiiragi283.lib.recipe.base

import net.minecraft.world.item.crafting.RecipeInput

/**
 * 処理時間または消費エネルギーを保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
interface HTProgressRecipe<INPUT : RecipeInput> {
    fun getProgressData(input: INPUT): HTProgressData

    //    Simple    //

    interface Simple<INPUT : RecipeInput> : HTProgressRecipe<INPUT> {
        val progressData: HTProgressData

        override fun getProgressData(input: INPUT): HTProgressData = progressData
    }
}
