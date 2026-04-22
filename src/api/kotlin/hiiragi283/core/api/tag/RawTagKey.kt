package hiiragi283.core.api.tag

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.RegistryKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.serialization.codec.BiCodec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey

/**
 * ジェネリクスのない[TagKey]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.3
 */
@JvmInline
value class RawTagKey private constructor(val location: ResourceLocation) {
    companion object {
        @JvmField
        val CODEC: Codec<RawTagKey> = ResourceLocation.CODEC.xmap(::create, RawTagKey::location)

        @JvmField
        val HASHED_CODEC: Codec<RawTagKey> = Codec.STRING.comapFlatMap(
            { value: String ->
                when {
                    value.startsWith("#") ->
                        value
                            .substring(1)
                            .let(ResourceLocation::read)
                            .map(::create)
                    else -> DataResult.error { "Not a tag id" }
                }
            },
            { "#${it.location}" },
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, RawTagKey> = ResourceLocation.STREAM_CODEC.map(::create, RawTagKey::location)

        /**
         * @see TagKey.hashedCodec
         */
        @JvmStatic
        fun codec(withHash: Boolean): BiCodec<ByteBuf, RawTagKey> = BiCodec.of(
            when (withHash) {
                true -> HASHED_CODEC
                false -> CODEC
            },
            STREAM_CODEC,
        )

        @JvmStatic
        fun common(path: String): RawTagKey = create(HTConst.COMMON.toId(path))

        @JvmStatic
        fun common(vararg path: String): RawTagKey = create(HTConst.COMMON.toId(*path))

        @JvmStatic
        fun copy(parent: TagKey<*>): RawTagKey = create(parent.location())

        @JvmStatic
        fun create(location: ResourceLocation): RawTagKey = RawTagKey(location)
    }

    fun withPrefix(prefix: String): RawTagKey = create(location.withPrefix(prefix))

    fun withSuffix(suffix: String): RawTagKey = create(location.withSuffix(suffix))

    fun withPath(transform: (String) -> String): RawTagKey = create(location.withPath(transform))

    fun <T : Any> create(key: RegistryKey<T>): TagKey<T> = TagKey.create(key, location)
}
