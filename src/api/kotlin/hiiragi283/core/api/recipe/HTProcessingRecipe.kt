package hiiragi283.core.api.recipe

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

abstract class HTProcessingRecipe(val time: Int, val exp: Fraction) : HTRecipe {
    companion object {
        @JvmField
        val TIME_CODEC: MapBiCodec<ByteBuf, Int> = BiCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.TIME)

        @JvmField
        val EXP_CODEC: MapBiCodec<ByteBuf, Fraction> = BiCodecs.NON_NEGATIVE_FRACTION.fieldOf(HTConst.EXP)
    }
}
