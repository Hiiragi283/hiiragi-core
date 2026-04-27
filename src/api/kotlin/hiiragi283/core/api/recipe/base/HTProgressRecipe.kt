package hiiragi283.core.api.recipe.base

import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 処理時間または消費エネルギーを保持する[HTRecipe]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
interface HTProgressRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    companion object {
        @JvmStatic
        fun <RECIPE : Ticking<*>> timeCodec(): RecordCodecBuilder<RECIPE, Int> =
            HTCodecs.POSITIVE_INT.fieldOf(HTConst.TIME).forGetter { it.time }

        @JvmStatic
        fun <RECIPE : Energized<*>> energyCodec(): RecordCodecBuilder<RECIPE, Int> =
            HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY).forGetter { it.energy }
    }

    fun getProgressData(input: INPUT): HTProgressData

    //    Ticking    //

    /**
     * 一定の処理時間のみを持つ[HTProgressRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.3
     */
    interface Ticking<INPUT : RecipeInput> : HTProgressRecipe<INPUT> {
        val time: Int

        override fun getProgressData(input: INPUT): HTProgressData = HTProgressData.time(time)
    }

    //    Energized    //

    /**
     * 一定の消費エネルギーのみを持つ[HTProgressRecipe]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.3
     */
    interface Energized<INPUT : RecipeInput> : HTProgressRecipe<INPUT> {
        val energy: Int

        override fun getProgressData(input: INPUT): HTProgressData = HTProgressData.energy(energy)
    }
}
