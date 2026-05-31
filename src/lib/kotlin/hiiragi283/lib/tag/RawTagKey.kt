package hiiragi283.lib.tag

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.lib.HTConstants
import hiiragi283.lib.registry.RegistryKey
import hiiragi283.lib.resource.toId
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey

/**
 * ジェネリクスのない[TagKey]を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.16.0
 */
@JvmInline
value class RawTagKey private constructor(val location: Identifier) {
    companion object {
        @JvmField
        val CODEC: Codec<RawTagKey> = Identifier.CODEC.xmap(::create, RawTagKey::location)

        @JvmField
        val HASHED_CODEC: Codec<RawTagKey> = Codec.STRING.comapFlatMap(
            { value: String ->
                when {
                    value.startsWith("#") ->
                        value
                            .substring(1)
                            .let(Identifier::read)
                            .map(::create)
                    else -> DataResult.error { "Not a tag id" }
                }
            },
            { "#${it.location}" },
        )

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, RawTagKey> = Identifier.STREAM_CODEC.map(::create, RawTagKey::location)

        @JvmStatic
        fun common(path: String): RawTagKey = create(HTConstants.COMMON.toId(path))

        @JvmStatic
        fun common(vararg path: String): RawTagKey = create(HTConstants.COMMON.toId(*path))

        @JvmStatic
        fun copy(parent: TagKey<*>): RawTagKey = create(parent.location())

        @JvmStatic
        fun create(location: Identifier): RawTagKey = RawTagKey(location)
    }

    fun withPrefix(prefix: String): RawTagKey = create(location.withPrefix(prefix))

    fun withSuffix(suffix: String): RawTagKey = create(location.withSuffix(suffix))

    fun withPath(transform: (String) -> String): RawTagKey = create(location.withPath(transform))

    fun <T : Any> create(key: RegistryKey<T>): TagKey<T> = TagKey.create(key, location)
}
