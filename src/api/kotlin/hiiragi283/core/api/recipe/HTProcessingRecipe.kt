package hiiragi283.core.api.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.world.item.crafting.RecipeInput
import org.apache.commons.lang3.math.Fraction

/**
 * 処理時間と獲得経験値を保持する[HTRecipe]の拡張クラスです。
 * @param INPUT レシピの入力となるクラス
 * @param time レシピの処理時間
 * @param exp レシピの実行時にもらえる経験値量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTProcessingRecipe<INPUT : RecipeInput>(val parameters: SubParameters) : HTRecipe<INPUT> {
    val time: Int = parameters.time
    val exp: Fraction = parameters.exp

    /**
     * @since 0.9.0
     */
    data class SubParameters(val time: Int, val exp: Fraction) {
        companion object {
            @JvmField
            val CODEC: MapBiCodec<ByteBuf, SubParameters> = MapBiCodec.composite(
                BiCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.TIME).forGetter(SubParameters::time),
                BiCodecs.NON_NEGATIVE_FRACTION.optionalFieldOf(HTConst.EXP, Fraction.ZERO).forGetter(SubParameters::exp),
                ::SubParameters,
            )
        }
    }
}
