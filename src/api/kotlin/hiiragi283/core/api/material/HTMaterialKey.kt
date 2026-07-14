package hiiragi283.core.api.material

import com.mojang.serialization.Codec
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @param id 素材の[ID][ResourceLocation]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class HTMaterialKey private constructor(private val id: ResourceLocation) :
    HTIdLike.Translatable,
    HTMaterialLike,
    Comparable<HTMaterialKey> {
    companion object {
        /**
         * 指定した[id]から[HTMaterialKey]のインスタンスを返します。
         * @return キャッシュから取得した[HTMaterialKey]のインスタンス
         */
        @JvmStatic
        fun of(id: ResourceLocation): HTMaterialKey = HTMaterialKey(id)

        /**
         * [HTMaterialKey]の[Codec]
         */
        @JvmField
        val CODEC: Codec<HTMaterialKey> = ResourceLocation.CODEC.xmap(HTMaterialKey::of, HTMaterialKey::getId)

        /**
         * [HTMaterialKey]の[StreamCodec]
         */
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> = ResourceLocation.STREAM_CODEC.map(HTMaterialKey::of, HTMaterialKey::getId)
    }

    override fun getId(): ResourceLocation = id

    override fun asMaterialKey(): HTMaterialKey = this

    /**
     * @since 0.12.0
     */
    override val translationKey: String get() = getId().toLanguageKey("material")

    /**
     * @since 0.12.0
     */
    override fun getText(): Text = translatableText(translationKey)

    override fun compareTo(other: HTMaterialKey): Int = this.id.compareNamespaced(other.id)
}
