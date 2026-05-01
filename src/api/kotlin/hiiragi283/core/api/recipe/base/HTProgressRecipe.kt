package hiiragi283.core.api.recipe.base

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

    //    Ticking    //

    /**
     * 一定の処理時間のみを持つ[HTProgressRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.16.0
     */
    interface Ticking<INPUT : RecipeInput> : HTProgressRecipe<INPUT> {
        val time: Int

        override fun getProgressData(input: INPUT): HTProgressData = HTProgressData.time(time)
    }
}
