package hiiragi283.core.api.material

import com.mojang.serialization.Codec
import hiiragi283.core.api.HCRegistries
import hiiragi283.core.api.property.HTPropertyGetter
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.HTKeyLike
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class HTMaterialKey private constructor(private val key: ResourceKey<HTPropertyGetter>) :
    HTKeyLike.SimpleTranslatable<HTPropertyGetter>,
    HTMaterialLike,
    Comparable<HTMaterialKey> {
    companion object {
        /**
         * [HTMaterialKey]の[Codec]
         */
        @JvmField
        val CODEC: Codec<HTMaterialKey> = ResourceLocation.CODEC.xmap(::HTMaterialKey, HTMaterialKey::getId)

        /**
         * [HTMaterialKey]の[StreamCodec]
         */
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> = ResourceLocation.STREAM_CODEC.map(::HTMaterialKey, HTMaterialKey::getId)
    }

    constructor(namespace: String, path: String) : this(HCRegistries.Keys.MATERIAL.createKey(namespace, path))

    constructor(id: ResourceLocation) : this(HCRegistries.Keys.MATERIAL.createKey(id))

    override fun getKey(): ResourceKey<HTPropertyGetter> = key

    override fun asMaterialKey(): HTMaterialKey = this

    override fun compareTo(other: HTMaterialKey): Int = this.getId().compareNamespaced(other.getId())
}
