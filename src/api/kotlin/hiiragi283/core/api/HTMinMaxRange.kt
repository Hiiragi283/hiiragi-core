package hiiragi283.core.api

import com.mojang.serialization.Codec
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.api.serialization.network.HTStreamCodecs
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Ior
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * 区間を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 * @see net.minecraft.advancements.critereon.MinMaxBounds
 */
@JvmInline
value class HTMinMaxRange<T : Comparable<T>> private constructor(private val content: Ior<T, T>) : HTHasText {
    companion object {
        @JvmStatic
        fun <T : Comparable<T>> codec(codec: Codec<T>): Codec<HTMinMaxRange<T>> = HTCodecs
            .ior(codec.fieldOf("min"), codec.fieldOf("max"))
            .codec()
            .xmap(::HTMinMaxRange, HTMinMaxRange<T>::content)

        @JvmStatic
        fun <B : ByteBuf, T : Comparable<T>> streamCodec(codec: StreamCodec<in B, T>): StreamCodec<B, HTMinMaxRange<T>> = HTStreamCodecs
            .ior(codec, codec)
            .map(::HTMinMaxRange, HTMinMaxRange<T>::content)

        /**
         * 下限をもつ区間を作成します。
         */
        @JvmStatic
        fun <T : Comparable<T>> atLeast(min: T): HTMinMaxRange<T> = HTMinMaxRange(Ior.Left(min))

        /**
         * 上限をもつ区間を作成します。
         */
        @JvmStatic
        fun <T : Comparable<T>> atMost(max: T): HTMinMaxRange<T> = HTMinMaxRange(Ior.Right(max))

        /**
         * 閉区間を作成します。
         */
        @JvmStatic
        fun <T : Comparable<T>> between(range: ClosedRange<T>): HTMinMaxRange<T> {
            check(!range.isEmpty()) { "Range $range must be not empty" }
            return HTMinMaxRange(Ior.Both(range.start, range.endInclusive))
        }

        /**
         * 閉区間を作成します。
         */
        @JvmStatic
        fun <T : Comparable<T>> between(min: T, max: T): HTMinMaxRange<T> {
            check(min < max) { "Maximum value $max must be larger than minimum value $min" }
            return HTMinMaxRange(Ior.Both(min, max))
        }
    }

    /**
     * 区間の最小値
     */
    val min: T? get() = content.getLeft()

    /**
     * 区間の最大値
     */
    val max: T? get() = content.getRight()

    operator fun contains(value: T): Boolean = content.fold(
        { min: T -> value >= min },
        { max: T -> value <= max },
        { min: T, max: T -> value in (min..max) },
    )

    override fun getText(): Text = content.fold(
        { min: T -> HTCommonTranslation.RANGE_MIN.translate(min) },
        { max: T -> HTCommonTranslation.RANGE_MAX.translate(max) },
        { min: T, max: T -> HTCommonTranslation.RANGE_MIN_MAX.translate(min, max) },
    )
}
