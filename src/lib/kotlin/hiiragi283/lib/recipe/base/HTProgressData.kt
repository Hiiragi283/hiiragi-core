package hiiragi283.lib.recipe.base

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTHasText
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.unwrap

/**
 * 処理時間または消費エネルギーを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
sealed interface HTProgressData : HTHasText {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProgressData> = HTCodecs
            .either(Time.CODEC, Energy.CODEC)
            .xmap(Either<Time, Energy>::unwrap) { progressData: HTProgressData ->
                when (progressData) {
                    is Energy -> Either.Right(progressData)
                    is Time -> Either.Left(progressData)
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
            val CODEC: MapCodec<Time> = HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConstants.TIME).xmap(::Time, Time::value)
        }

        override fun getTotalEnergy(energyRate: Int): Int = value * energyRate

        override fun getProcessTime(energyRate: Int): Int = value

        override fun getText(): Text = HTCommonTranslation.SECONDS.translate(value, value / 20)
    }

    @JvmInline
    value class Energy(val value: Int) : HTProgressData {
        companion object {
            @JvmField
            val CODEC: MapCodec<Energy> = HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConstants.ENERGY).xmap(::Energy, Energy::value)
        }

        override fun getTotalEnergy(energyRate: Int): Int = value

        override fun getProcessTime(energyRate: Int): Int = value / energyRate

        override fun getText(): Text = HTCommonTranslation.STORED_FE.translate(value)
    }
}
