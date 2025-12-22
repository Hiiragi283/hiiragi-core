package hiiragi283.core.api.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

/**
 * 処理時間と獲得経験値を保持する[HTRecipe]の拡張クラスです。
 * @param time レシピの処理時間
 * @param exp レシピの実行時にもらえる経験値量
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTProcessingRecipe(val time: Int, val exp: Fraction) : HTRecipe {
    companion object {
        /**
         * 処理時間の[MapBiCodec]
         */
        @JvmField
        val TIME_CODEC: MapBiCodec<ByteBuf, Int> = BiCodecs.NON_NEGATIVE_INT.optionalFieldOf(HTConst.TIME, 200)

        /**
         * 経験値量の[MapBiCodec]
         */
        @JvmField
        val EXP_CODEC: MapBiCodec<ByteBuf, Fraction> = BiCodecs.NON_NEGATIVE_FRACTION.optionalFieldOf(HTConst.EXP, Fraction.ZERO)
    }
}
