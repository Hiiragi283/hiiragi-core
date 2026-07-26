package hiiragi283.core.api.material

import com.mojang.serialization.Codec
import hiiragi283.core.api.resource.toId
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class HTMaterialKey(val name: String) : Comparable<HTMaterialKey> {
    companion object {
        /**
         * [HTMaterialKey]の[Codec]
         */
        @JvmField
        val CODEC: Codec<HTMaterialKey> = Codec.STRING.xmap(::HTMaterialKey, HTMaterialKey::name)

        /**
         * [HTMaterialKey]の[StreamCodec]
         */
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> = ByteBufCodecs.STRING_UTF8.map(::HTMaterialKey, HTMaterialKey::name)
    }

    fun toId(namespace: String): ResourceLocation = namespace.toId(this.name)

    fun toId(namespace: String, vararg path: String): ResourceLocation = namespace.toId(this.name, *path)

    override fun compareTo(other: HTMaterialKey): Int = this.name.compareTo(other.name)
}
