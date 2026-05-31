package hiiragi283.lib.material

import com.mojang.serialization.Codec
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.translatableText
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @param id 素材の[ID][Identifier]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class HTMaterialKey private constructor(private val id: Identifier) :
    HTIdLike.Translatable,
    HTMaterialLike,
    Comparable<HTMaterialKey> {
    companion object {
        @JvmStatic
        fun of(key: ResourceKey<HTMaterialContents>): HTMaterialKey = of(key.identifier())

        /**
         * 指定した[id]から[HTMaterialKey]のインスタンスを返します。
         * @return キャッシュから取得した[HTMaterialKey]のインスタンス
         */
        @JvmStatic
        fun of(id: Identifier): HTMaterialKey = HTMaterialKey(id)

        /**
         * [HTMaterialKey]の[Codec]
         */
        @JvmField
        val CODEC: Codec<HTMaterialKey> = Identifier.CODEC.xmap(HTMaterialKey::of, HTMaterialKey::getId)

        /**
         * [HTMaterialKey]の[StreamCodec]
         */
        @JvmField
        val STREAM_CODEC: StreamCodec<ByteBuf, HTMaterialKey> = Identifier.STREAM_CODEC.map(HTMaterialKey::of, HTMaterialKey::getId)
    }

    override fun getId(): Identifier = id

    override fun asMaterialKey(): HTMaterialKey = this

    /**
     * @since 0.12.0
     */
    override val translationKey: String
        get() = getId().toLanguageKey("material")

    /**
     * @since 0.12.0
     */
    override fun getText(): Text = translatableText(translationKey)

    override fun compareTo(other: HTMaterialKey): Int = this.id.compareNamespaced(other.id)
}
