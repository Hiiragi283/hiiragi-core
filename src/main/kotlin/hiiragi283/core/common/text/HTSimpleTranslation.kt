package hiiragi283.core.common.text

import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.Util

@JvmInline
value class HTSimpleTranslation(override val translationKey: String) : HTTranslation {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTTranslation> =
            BiCodec.STRING.xmap(::HTSimpleTranslation, HTTranslation::translationKey)
    }

    constructor(type: String, modId: String, vararg path: String) : this(
        Util.makeDescriptionId(type, modId.toId(path.joinToString(separator = "."))),
    )
}
