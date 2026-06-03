package hiiragi283.lib.serialization.network

import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.tag.createTagKey
import hiiragi283.lib.text.Text
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.identity
import io.netty.buffer.ByteBuf
import java.util.UUID
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.util.ByIdMap
import org.apache.commons.lang3.math.Fraction

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[StreamCodec]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
data object HTStreamCodecs {
    @JvmField
    val FRACTION: StreamCodec<ByteBuf, Fraction> = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        Fraction::getNumerator,
        ByteBufCodecs.VAR_INT,
        Fraction::getDenominator,
        Fraction::getFraction,
    )

    @JvmField
    val TEXT: StreamCodec<RegistryFriendlyByteBuf, Text> = ComponentSerialization.STREAM_CODEC

    @JvmField
    val UUID: StreamCodec<ByteBuf, UUID> = UUIDUtil.STREAM_CODEC

    @JvmStatic
    inline fun <reified V : Enum<V>> enum(strategy: ByIdMap.OutOfBoundsStrategy = ByIdMap.OutOfBoundsStrategy.WRAP): StreamCodec<ByteBuf, V> = ByteBufCodecs.idMapper(
        ByIdMap.continuous(Enum<V>::ordinal, V::class.java.enumConstants, strategy),
        Enum<V>::ordinal,
    )

    /**
     * 指定した[left], [right]から，[Either]の[StreamCodec]を返します。
     * @param left [L]を対象とする[StreamCodec]
     * @param right [R]を対象とする[StreamCodec]
     * @return [Either]の[StreamCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, L : Any, R : Any> either(left: StreamCodec<in B, L>, right: StreamCodec<in B, R>): StreamCodec<B, Either<L, R>> = EitherCodec(left, right)

    private class EitherCodec<B : ByteBuf, L : Any, R : Any>(private val left: StreamCodec<in B, L>, private val right: StreamCodec<in B, R>) : StreamCodec<B, Either<L, R>> {
        override fun encode(output: B, value: Either<L, R>) {
            value.onLeft {
                output.writeBoolean(true)
                left.encode(output, it)
            }.onRight {
                output.writeBoolean(false)
                right.encode(output, it)
            }
        }

        override fun decode(input: B): Either<L, R> = when (input.readBoolean()) {
            true -> Either.Left(left.decode(input))
            false -> Either.Right(right.decode(input))
        }
    }

    /**
     * 指定した[left], [right]から，[Ior]の[StreamCodec]を返します。
     * @param left [L]を対象とする[StreamCodec]
     * @param right [R]を対象とする[StreamCodec]
     * @return [Ior]の[StreamCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, L : Any, R : Any> ior(left: StreamCodec<in B, L>, right: StreamCodec<in B, R>): StreamCodec<B, Ior<L, R>> = HTIorStreamCodec(left, right)

    private class HTIorStreamCodec<B : ByteBuf, L : Any, R : Any>(private val left: StreamCodec<in B, L>, private val right: StreamCodec<in B, R>) : StreamCodec<B, Ior<L, R>> {
        override fun decode(buffer: B): Ior<L, R> = when (buffer.readInt()) {
            1 -> Ior.Left(left.decode(buffer))
            2 -> Ior.Right(right.decode(buffer))
            else -> {
                val leftIn: L = left.decode(buffer)
                val rightIn: R = right.decode(buffer)
                Ior.Both(leftIn, rightIn)
            }
        }

        override fun encode(buffer: B, value: Ior<L, R>) {
            value.fold(
                {
                    buffer.writeInt(1)
                    left.encode(buffer, it)
                },
                {
                    buffer.writeInt(2)
                    right.encode(buffer, it)
                },
                { left: L, right: R ->
                    buffer.writeInt(0)
                    this.left.encode(buffer, left)
                    this.right.encode(buffer, right)
                },
            )
        }
    }

    @JvmStatic
    fun <B : ByteBuf, K : Any, V : Any> mapOf(keyCodec: StreamCodec<in B, K>, valueCodec: StreamCodec<in B, V>): StreamCodec<B, Map<K, V>> = ByteBufCodecs.map(::LinkedHashMap, keyCodec, valueCodec)

    //    Registry    //

    /**
     * 指定した[registryKey]から[ResourceKey]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> resourceKey(registryKey: RegistryKey<T>): StreamCodec<ByteBuf, ResourceKey<T>> = ResourceKey.streamCodec(registryKey)

    /**
     * 指定した[registryKey]から[TagKey]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     * @param withHash 変換後の文字列の先頭に'#'をつけるかどうか
     */
    @JvmStatic
    fun <T : Any> tagKey(registryKey: RegistryKey<T>, withHash: Boolean): StreamCodec<ByteBuf, TagKey<T>> = Identifier.STREAM_CODEC.map(registryKey::createTagKey, TagKey<T>::location)

    /**
     * 指定した[registryKey]から[Holder]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, Holder<T>> = ByteBufCodecs.holderRegistry(registryKey).map(Holder<T>::getDelegate, identity())

    /**
     * 指定した[registryKey]から[HolderSet]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> = ByteBufCodecs.holderSet(registryKey)
}
