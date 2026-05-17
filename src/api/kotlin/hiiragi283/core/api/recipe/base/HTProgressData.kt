package hiiragi283.core.api.recipe.base

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.DFUEither

/**
 * 処理時間または消費エネルギーを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
sealed interface HTProgressData : HTHasText {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProgressData> = Codec
            .mapEither(Time.CODEC, Energy.CODEC)
            .xmap(DFUEither<Time, Energy>::unwrap) { progressData: HTProgressData ->
                when (progressData) {
                    is Energy -> DFUEither.right(progressData)
                    is Time -> DFUEither.left(progressData)
                }
            }

        @JvmStatic
        fun time(value: Int): HTProgressData = Time(value)

        @JvmStatic
        fun energy(value: Int): HTProgressData = Energy(value)
    }

    fun getTotalEnergy(energyRate: Int): Int

    fun getProcessTime(energyRate: Int): Int

    @JvmInline
    value class Time(val value: Int) : HTProgressData {
        companion object {
            @JvmField
            val CODEC: MapCodec<Time> = HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.TIME).xmap(::Time, Time::value)
        }

        override fun getTotalEnergy(energyRate: Int): Int = value * energyRate

        override fun getProcessTime(energyRate: Int): Int = value

        override fun getText(): Text = HTCommonTranslation.SECONDS.translate(value, value / 20)
    }

    @JvmInline
    value class Energy(val value: Int) : HTProgressData {
        companion object {
            @JvmField
            val CODEC: MapCodec<Energy> = HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY).xmap(::Energy, Energy::value)
        }

        override fun getTotalEnergy(energyRate: Int): Int = value

        override fun getProcessTime(energyRate: Int): Int = value / energyRate

        override fun getText(): Text = HTCommonTranslation.STORED_FE.translate(value)
    }
}
