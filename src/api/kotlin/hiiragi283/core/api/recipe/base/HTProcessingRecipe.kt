package hiiragi283.core.api.recipe.base

import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipe
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 処理時間と獲得経験値を保持する[HTRecipe]の拡張クラスです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTProcessingRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    companion object {
        @JvmStatic
        fun <RECIPE : HTProcessingRecipe<*>> timeCodec(): RecordCodecBuilder<RECIPE, Int> =
            HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.TIME).forGetter { it.time }
    }

    /**
     * レシピの処理時間を取得します。
     */
    val time: Int

    /**
     * シリアライズ可能な[HTProcessingRecipe]の拡張インターフェースです。
     * @param INPUT レシピの入力となるクラス
     * @author Hiiragi Tsubasa
     * @since 0.12.0
     */
    interface Serializable<INPUT : RecipeInput> :
        HTProcessingRecipe<INPUT>,
        HTSerializableRecipe<INPUT>
}
