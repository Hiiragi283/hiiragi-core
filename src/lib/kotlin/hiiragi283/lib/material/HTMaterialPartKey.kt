package hiiragi283.lib.material

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * 素材の部品を表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTMaterialPartKey(val name: String) : Comparable<HTMaterialPartKey> {
    companion object {
        @JvmField
        val CODEC: Codec<HTMaterialPartKey> = Codec.STRING.xmap(::HTMaterialPartKey, HTMaterialPartKey::name)

        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialPartKey> = ByteBufCodecs.STRING_UTF8.map(::HTMaterialPartKey, HTMaterialPartKey::name)
    }

    override fun compareTo(other: HTMaterialPartKey): Int = this.name.compareTo(other.name)
}
