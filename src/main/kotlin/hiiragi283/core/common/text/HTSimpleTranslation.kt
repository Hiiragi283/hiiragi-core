package hiiragi283.core.common.text

import com.mojang.serialization.Codec
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmInline
value class HTSimpleTranslation(override val translationKey: String) : HTTranslation {
    companion object {
        @JvmField
        val CODEC: Codec<HTTranslation> = Codec.stringResolver(HTTranslation::translationKey, ::HTSimpleTranslation)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTTranslation> =
            ByteBufCodecs.STRING_UTF8.map(::HTSimpleTranslation, HTTranslation::translationKey)
    }

    constructor(type: String, modId: String, vararg path: String) : this(
        modId.toId(path.joinToString(separator = ".")).toDescriptionKey(type),
    )
}
