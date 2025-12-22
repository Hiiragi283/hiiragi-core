package hiiragi283.core.api.serialization.codec

import hiiragi283.core.api.HTConst
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

object HTRecipeBiCodecs {
    @JvmField
    val ENERGY: MapBiCodec<ByteBuf, Int> = BiCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY)

    /**
     * 経験値量の[MapBiCodec]
     */
    @JvmField
    val EXP: MapBiCodec<ByteBuf, Fraction> = BiCodecs.NON_NEGATIVE_FRACTION.optionalFieldOf(HTConst.EXP, Fraction.ZERO)

    /**
     * 処理時間の[MapBiCodec]
     */
    @JvmField
    val TIME: MapBiCodec<ByteBuf, Int> = BiCodecs.NON_NEGATIVE_INT.optionalFieldOf(HTConst.TIME, 200)
}
