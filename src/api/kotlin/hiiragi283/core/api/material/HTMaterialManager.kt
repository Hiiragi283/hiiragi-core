package hiiragi283.core.api.material

import com.mojang.serialization.Codec
import hiiragi283.core.api.property.HTPropertyManager
import hiiragi283.core.internal.material.HTMaterialContentsRegister
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec

/**
 * 素材のプロパティを管理するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
interface HTMaterialManager : HTPropertyManager<HTMaterialKey, HTMaterial> {
    companion object {
        @JvmStatic
        fun getInstance(): HTMaterialManager = HTMaterialContentsRegister.materialManager

        private fun errorMessage(key: HTMaterialKey): String = "Missing material: $key"

        @JvmField
        val CODEC: Codec<HTMaterial> = HTPropertyManager.codec(HTMaterialKey.CODEC, ::getInstance, ::errorMessage)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterial> = HTPropertyManager.streamCodec(HTMaterialKey.STREAM_CODEC, ::getInstance, ::errorMessage)
    }
}
