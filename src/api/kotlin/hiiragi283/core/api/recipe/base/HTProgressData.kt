package hiiragi283.core.api.recipe.base

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import java.util.Optional

/**
 * 処理時間または消費エネルギーを保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
@JvmInline
value class HTProgressData private constructor(private val content: Either<Int, Int>) : HTHasText {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTProgressData> = Codec
            .mapEither(HTCodecs.POSITIVE_INT.fieldOf(HTConst.TIME), HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY))
            .xmap(::HTProgressData, HTProgressData::content)

        @JvmStatic
        fun empty(): HTProgressData = energy(0)

        @JvmStatic
        fun time(value: Int): HTProgressData = HTProgressData(Either.left(value))

        @JvmStatic
        fun energy(value: Int): HTProgressData = HTProgressData(Either.right(value))
    }

    val time: Optional<Int> get() = content.left()
    val energy: Optional<Int> get() = content.right()

    fun getTotalEnergy(energyRate: Int): Int = content.map({ it * energyRate }, identity())

    fun getProcessTime(energyRate: Int): Int = content.map(identity()) { it / energyRate }

    override fun getText(): Text = content.map(
        { HTCommonTranslation.SECONDS.translate(it, it / 20) },
        { HTCommonTranslation.STORED_FE.translate(it) },
    )
}
