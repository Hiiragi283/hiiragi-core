package hiiragi283.core.api.material.part

import com.mojang.serialization.Codec
import hiiragi283.core.api.property.HTPropertyManager
import hiiragi283.core.internal.material.HTMaterialContentsRegister
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

interface HTPartManager : HTPropertyManager<HTPartKey, HTPart> {
    companion object {
        @JvmStatic
        fun getInstance(): HTPartManager = HTMaterialContentsRegister.partManager

        private fun errorMessage(key: HTPartKey): String = "Missing part: $key"

        @JvmField
        val CODEC: Codec<HTPart> = HTPropertyManager.codec(HTPartKey.CODEC, ::getInstance, ::errorMessage)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTPart> = HTPropertyManager.streamCodec(HTPartKey.STREAM_CODEC, ::getInstance, ::errorMessage)
    }
}
