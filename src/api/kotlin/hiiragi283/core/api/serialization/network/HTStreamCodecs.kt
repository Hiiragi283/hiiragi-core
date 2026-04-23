package hiiragi283.core.api.serialization.network

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.fraction
import hiiragi283.core.api.function.identity
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.tag.createTagKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.util.Ior
import hiiragi283.core.impl.serialization.network.HTHolderLikeStreamCodec
import hiiragi283.core.impl.serialization.network.HTIorStreamCodec
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.UUIDUtil
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs
import org.apache.commons.lang3.math.Fraction
import java.util.UUID
import kotlin.Int
import kotlin.String

data object HTStreamCodecs {
    @JvmField
    val FRACTION: StreamCodec<ByteBuf, Fraction> = ByteBufCodecs
        .either(ByteBufCodecs.STRING_UTF8, ByteBufCodecs.VAR_INT)
        .map(
            { either: Either<String, Int> -> either.map(Fraction::getFraction, ::fraction) },
            { fraction: Fraction ->
                when (fraction.denominator) {
                    1 -> Either.right(fraction.numerator)
                    else -> Either.left(fraction.toString())
                }
            },
        )

    @JvmField
    val TEXT: StreamCodec<RegistryFriendlyByteBuf, Text> = ComponentSerialization.STREAM_CODEC

    @JvmField
    val UUID: StreamCodec<ByteBuf, UUID> = UUIDUtil.STREAM_CODEC

    @JvmStatic
    inline fun <B : FriendlyByteBuf, reified V : Enum<V>> enum(): StreamCodec<B, V> = NeoForgeStreamCodecs.enumCodec(V::class.java)

    /**
     * 指定した[left], [right]から，[Ior]の[StreamCodec]を返します。
     * @param left [L]を対象とする[StreamCodec]
     * @param right [R]を対象とする[StreamCodec]
     * @return [Ior]の[StreamCodec]
     */
    @JvmStatic
    fun <B : ByteBuf, L : Any, R : Any> ior(left: StreamCodec<in B, L>, right: StreamCodec<in B, R>): StreamCodec<B, Ior<L, R>> =
        HTIorStreamCodec(left, right)

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
    fun <T : Any> tagKey(registryKey: RegistryKey<T>, withHash: Boolean): StreamCodec<ByteBuf, TagKey<T>> =
        ResourceLocation.STREAM_CODEC.map(registryKey::createTagKey, TagKey<T>::location)

    /**
     * 指定した[registryKey]から[Holder]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holder(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, Holder<T>> =
        ByteBufCodecs.holderRegistry(registryKey).map(Holder<T>::getDelegate, identity())

    /**
     * 指定した[registryKey]から[HolderSet]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     */
    @JvmStatic
    fun <T : Any> holderSet(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, HolderSet<T>> =
        ByteBufCodecs.holderSet(registryKey)

    /**
     * 指定した[registryKey]から[HTSimpleHolderLike]の[StreamCodec]を返します。
     * @param T レジストリの要素のクラス
     * @since 0.13.0
     */
    @JvmStatic
    fun <T : Any> holderLike(registryKey: RegistryKey<T>): StreamCodec<RegistryFriendlyByteBuf, HTSimpleHolderLike<T>> =
        HTHolderLikeStreamCodec(registryKey)
}
